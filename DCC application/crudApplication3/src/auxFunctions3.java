public class auxFunctions3 {

    public double[] calculateCoordinates(String coordinateType, Double coordinateA, Double coordinateB) {
        double xCoordinate = 0.0;
        double yCoordinate = 0.0;
        double rCoordinate = 0.0;
        double thetaCoordinate = 0.0;

        if (coordinateType.equals("CARTESIAN")) {
            xCoordinate = coordinateA;
            yCoordinate = coordinateB;
            rCoordinate = Math.sqrt(coordinateA * coordinateA + coordinateB * coordinateB);
            thetaCoordinate = Math.atan2(yCoordinate, xCoordinate);
        } else if (coordinateType.equals("POLAR")) {
            xCoordinate = coordinateA * Math.cos(Math.toRadians(coordinateB));
            yCoordinate = coordinateA * Math.sin(Math.toRadians(coordinateB));
            rCoordinate = coordinateA;
            thetaCoordinate = coordinateB;
        }

        return new double[] {xCoordinate, yCoordinate, rCoordinate, thetaCoordinate};
    }
}
