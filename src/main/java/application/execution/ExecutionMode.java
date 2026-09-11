package application.execution;

public enum ExecutionMode {
    SINGLE_THREAD("Однопоточный режим"),
    MULTI_THREAD("Многопоточный режим");

    private final String displayName;

    ExecutionMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
