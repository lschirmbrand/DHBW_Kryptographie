package gui;

import java.util.function.Consumer;

public class GUILogger {

    private final Consumer<String> logFunction;

    public GUILogger(Consumer<String> logFunction) {
        this.logFunction = logFunction;
    }

    public void log(String text) {
        logFunction.accept(text);
    }
}
