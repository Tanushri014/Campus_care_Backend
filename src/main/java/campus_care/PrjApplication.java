package campus_care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

//just open Campus-care ->demo then run button will be unabled .
@SpringBootApplication
@EnableAsync
@ConfigurationPropertiesScan
public class PrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrjApplication.class, args);
	}

}

