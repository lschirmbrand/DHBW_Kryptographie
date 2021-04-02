package gui;

import java.util.function.Consumer;
import java.util.function.Function;

public class GUILogger {

    Consumer<String> logFunction;

    public GUILogger(Consumer<String> logFunction) {
        this.logFunction = logFunction;
    }

    public void log(String text) {
        logFunction.accept(text);
    }
}
