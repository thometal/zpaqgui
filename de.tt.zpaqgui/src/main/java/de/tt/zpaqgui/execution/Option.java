package de.tt.zpaqgui.execution;

public enum Option {
    THREADS("-threads"),
    ALL("-all"),
    UNTIL("-until"),
    TO("-to"),
    FORCE("-force"),
    TEST("-test"),
    SUMMARY("-summary"),

    //add
    METHOD("-method"),
    FRAGMENT("-fragment"),
    NODELETE("-nodelete"), 
    ONLY("-only");

    private final String s;

    Option(final String s) {
        this.s = s;
    }

    public String getString() {
        return s;
    }
}
