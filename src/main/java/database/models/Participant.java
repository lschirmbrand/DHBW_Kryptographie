package database.models;

public class Participant {
    private final String name;
    private final String type;

    public Participant(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
