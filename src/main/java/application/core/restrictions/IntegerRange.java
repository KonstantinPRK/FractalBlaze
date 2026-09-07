package application.core.restrictions;

public record IntegerRange(int minimum, int maximum) {
    public boolean contains(int value) {
        return value >= minimum && value <= maximum;
    }
}
