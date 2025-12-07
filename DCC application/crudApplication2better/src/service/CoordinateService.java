package service;

import database.DatabaseFunctions2;
import model.CoordinateEntry;

import java.util.List;

public class CoordinateService {

    private final DatabaseFunctions2 repository = new DatabaseFunctions2();

    public List<CoordinateEntry> loadAll() {
        return repository.loadEntries();
    }

    public List<CoordinateEntry> readByLabel(String label) {
        return repository.readByLabel(label);
    }

    public void add(String type, double a, double b, String label) {
        repository.addEntry(type, a, b, label);
    }

    public void update(int id, String type, double a, double b, String label) {
        repository.updateEntry(id, type, a, b, label);
    }

    public void delete(int id) {
        repository.deleteEntry(id);
    }
}
