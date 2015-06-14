package de.tt.zpaqgui.execution;

public enum Command {
    LIST("l"), EXTRACT("x"), ADD("a");

    private final String s;

    Command(String s) {
        this.s = s;
    }

    public String getString() {
        return s;
    }
}
