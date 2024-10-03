import Controllers.MealController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(MealController.class, args);

		//add @Bean annotation to create some sample Meals?
	}

}
