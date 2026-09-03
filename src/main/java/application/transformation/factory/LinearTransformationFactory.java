package application.transformation.factory;

import application.transformation.LinearTransformation;
import application.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LinearTransformationFactory implements TransformationFactory {
    private static final String SCALE = "scale";
    private static final String SHIFT_X = "shiftX";
    private static final String SHIFT_Y = "shiftY";

    @Override
    public String getName() {
        return "LINEAR";
    }

    @Override
    public List<ParameterDefinition> getRequiredParameters() {
        return List.of(
                new ParameterDefinition(
                        SCALE,
                        "Введите масштаб в процентах",
                        10,
                        200,
                        100.0
                ),
                new ParameterDefinition(
                        SHIFT_X,
                        "Введите горизонтальное смещение в сотых долях",
                        -200,
                        200,
                        100.0
                ),
                new ParameterDefinition(
                        SHIFT_Y,
                        "Введите вертикальное смещение в сотых долях",
                        -200,
                        200,
                        100.0
                )
        );
    }

    @Override
    public Transformation create(Map<String, Double> parameterValues) {
        return new LinearTransformation(
                parameterValues.get(SCALE),
                parameterValues.get(SHIFT_X),
                parameterValues.get(SHIFT_Y)
        );
    }
}
