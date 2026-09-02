package application.transformation.factory;

import application.transformation.Transformation;

import java.util.List;
import java.util.Map;

public interface TransformationFactory {
    String getName();

    List<ParameterDefinition> getRequiredParameters();

    Transformation create(Map<String, Double> parameterValues);
}
