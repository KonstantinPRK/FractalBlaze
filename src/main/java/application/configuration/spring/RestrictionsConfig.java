package application.configuration.spring;

import application.configuration.restriction.GenerationInputRestrictions;
import application.configuration.restriction.ImageSizeRestrictions;
import application.configuration.restriction.IntegerRange;
import application.configuration.restriction.TransformationInputRestrictions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestrictionsConfig {
    @Bean
    public ImageSizeRestrictions imageSizeRestrictions() {
        return new ImageSizeRestrictions(new IntegerRange(1280, 1920), new IntegerRange(1280, 1920));
    }

    @Bean
    public GenerationInputRestrictions generationInputRestrictions() {
        return new GenerationInputRestrictions(new IntegerRange(50, 200), new IntegerRange(10, 50), new IntegerRange(0, 100));
    }

    @Bean
    public TransformationInputRestrictions transformationInputRestrictions() {
        return new TransformationInputRestrictions(new IntegerRange(10, 200), new IntegerRange(-180, 180), new IntegerRange(-200, 200), new IntegerRange(-200, 200));
    }
}
