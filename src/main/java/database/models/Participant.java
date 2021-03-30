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

    public static enum Type {
        NORMAL("normal"), INTRUDER("intruder");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
