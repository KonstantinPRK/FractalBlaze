package application.transformation.factory;

public record ParameterDefinition(
        String name,
        String requestMessage,
        int minimumInputValue,
        int maximumInputValue,
        double inputDivisor
) {
    public double convertInputValue(int inputValue) {
        return inputValue / inputDivisor;
    }
}
