package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args)) {
            FractalBlazeApplication application = applicationContext.getBean(FractalBlazeApplication.class);
            application.start();

        } catch (NoSuchElementException exception) {
            System.out.println();
            System.out.println("Ввод завершён.");

        } catch (IllegalStateException exception) {
            System.err.println("Ошибка памяти: " + exception.getMessage());


        } catch (UncheckedIOException exception) {
            System.err.println("Ошибка сохранения: " + exception.getMessage());

        }
    }
}
