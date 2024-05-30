package t3h.manga.mangaweb.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomProperties {
    @Value("${app.base-url}")
    private String baseUrl;

    public String getBaseUrl() {
        return this.baseUrl;
    }
}
