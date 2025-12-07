package database;

import model.CoordinateEntry;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFunctions2 {

    private final DatabaseConnection2 connectionProvider = new DatabaseConnection2();

    public List<CoordinateEntry> loadEntries() {
        List<CoordinateEntry> list = new ArrayList<>();

        String query = "SELECT * FROM coordinates";

        try (Connection conn = connectionProvider.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(map(rs));
            }
            return list;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<CoordinateEntry> readByLabel(String label) {
        List<CoordinateEntry> list = new ArrayList<>();
        String query = "SELECT * FROM coordinates WHERE label = ?";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setString(1, label);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addEntry(String type, double A, double B, String label) {
        Coordinates c = Coordinates.from(type, A, B);

        String query = """
                INSERT INTO coordinates
                (xcoordinate, ycoordinate, rcoordinate, thetacoordinate, timestamp, label)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setDouble(1, c.x);
            st.setDouble(2, c.y);
            st.setDouble(3, c.r);
            st.setDouble(4, c.theta);
            st.setString(5, now());
            st.setString(6, label);

            st.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateEntry(int id, String type, double A, double B, String label) {
        Coordinates c = Coordinates.from(type, A, B);

        String query = """
                UPDATE coordinates
                SET xcoordinate=?, ycoordinate=?, rcoordinate=?, thetacoordinate=?, timestamp=?, label=?
                WHERE id=?
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setDouble(1, c.x);
            st.setDouble(2, c.y);
            st.setDouble(3, c.r);
            st.setDouble(4, c.theta);
            st.setString(5, now());
            st.setString(6, label);
            st.setInt(7, id);

            st.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteEntry(int id) {
        String query = "DELETE FROM coordinates WHERE id=?";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private CoordinateEntry map(ResultSet rs) throws SQLException {
        return new CoordinateEntry(
                rs.getInt("id"),
                rs.getString("label"),
                rs.getDouble("xcoordinate"),
                rs.getDouble("ycoordinate"),
                rs.getDouble("rcoordinate"),
                rs.getDouble("thetacoordinate"),
                rs.getString("timestamp")
        );
    }

    private String now() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static class Coordinates {
        double x, y, r, theta;

        static Coordinates from(String type, double A, double B) {
            Coordinates c = new Coordinates();

            if (type.equals("CARTESIAN")) {
                c.x = A; c.y = B;
                c.r = Math.sqrt(A*A + B*B);
                c.theta = Math.atan2(B, A);
            } else {
                c.r = A;
                c.theta = B;
                c.x = A * Math.cos(Math.toRadians(B));
                c.y = A * Math.sin(Math.toRadians(B));
            }
            return c;
        }
    }
}
