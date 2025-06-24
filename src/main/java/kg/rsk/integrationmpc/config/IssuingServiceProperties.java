package kg.rsk.integrationmpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "issuing")
public class IssuingServiceProperties {
    private String url;
    private String username;
    private String password;
    private String zpkComponent1;
    private String zpkComponent2;
    private String bankC;
    private String groupC;
}
