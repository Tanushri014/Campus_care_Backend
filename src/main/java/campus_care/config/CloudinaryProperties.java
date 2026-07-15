package campus_care.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
//automatically binds the values from .env who are prefixed with cloudinary .
@Getter
@Setter
public class CloudinaryProperties {
//pojo holds the key
    private String cloudName;

    private String apiKey;

    private String apiSecret;

}