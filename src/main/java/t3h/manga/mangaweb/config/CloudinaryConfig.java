package t3h.manga.mangaweb.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "drvdo3lm7",
                "api_key", "722123836616451",
                "api_secret", "I8dXEUVhlPe4ogIVE0WI0e9dDpM"
        );
        return new Cloudinary(config);
    }
}
