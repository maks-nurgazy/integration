package kg.rsk.integrationmpc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final IssuingServiceProperties issuingServiceProperties;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(issuingServiceProperties.getUrl())
                .defaultHeaders(h -> h.setBasicAuth(issuingServiceProperties.getUsername(), issuingServiceProperties.getPassword()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                .build();
    }
}
