package application;

import java.util.List;

public class Renderer {
    FractalImage render(
            FractalImage canvas,
            Rect world,
            List<Transformation> variations,
            int samples,
            short iterPerSample,
            long seed
    ) {
        for (int num = 0; num < samples; ++num) {
            Point pw = random(world);

            for (short step = 0; step < iterPerSample; ++step) {
                Transformation variation = random(variations, ...);

                pw = transform(pw, ...);

                double theta2 = 0.0;
                for (int s = 0; s < symmetry; theta2 += Math.PI * 2 / symmetry, ++s) {
                    var pwr = rotate(pw, theta2);
                    if (!world.contains(pwr)) continue;

                    Pixel pixel = map_range(world, pwr, canvas);
                    if (pixel == null) continue;

                    // 1. делаем лок на время работы с пикселем
                    // 2. подмешиваем цвет и увеличиваем hit count
                }
            }
        }
    }
}
