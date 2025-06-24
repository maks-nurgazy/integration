package kg.rsk.integrationmpc.service;

import kg.rsk.integrationmpc.util.PinBlockUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class IssuingService {

    private final WebClient webClient;
    private final String component1;
    private final String component2;
    private final String bankC;
    private final String groupC;

    public IssuingService(@Value("${issuing.url}") String url,
                          @Value("${issuing.username}") String username,
                          @Value("${issuing.password}") String password,
                          @Value("${issuing.zpk-component1}") String component1,
                          @Value("${issuing.zpk-component2}") String component2,
                          @Value("${issuing.bank-c}") String bankC,
                          @Value("${issuing.group-c}") String groupC) {
        this.component1 = component1;
        this.component2 = component2;
        this.bankC = bankC;
        this.groupC = groupC;
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(h -> h.setBasicAuth(username, password))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                .build();
    }

    public String assignPin(String card, String expiry, String pin, String pan, String stan) throws Exception {
        String zpk = PinBlockUtil.deriveZpk(component1, component2);
        String pinBlock = PinBlockUtil.buildEncryptedPinBlock(pin, pan, zpk);
        String body = createRequestBody(card, expiry, pinBlock, stan);
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String createRequestBody(String card, String expiry, String pinBlock, String stan) {
        return """
                <soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:bin=\"urn:IssuingWS/binding\">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <bin:assignPIN soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">
                            <ConnectionInfo xsi:type=\"urn:OperationConnectionInfo\" xmlns:urn=\"urn:issuing_v_01_02_xsd\">
                                <BANK_C xsi:type=\"xsd:string\">%s</BANK_C>
                                <GROUPC xsi:type=\"xsd:string\">%s</GROUPC>
                            </ConnectionInfo>
                            <Parameters xsi:type=\"urn:RowType_AssignPIN_Request\" xmlns:urn=\"urn:issuing_v_01_02_xsd\">
                                <CARD xsi:type=\"xsd:string\">%s</CARD>
                                <EXPIRY xsi:type=\"xsd:string\">%s</EXPIRY>
                                <PINBLOCK xsi:type=\"xsd:string\">%s</PINBLOCK>
                                <STAN xsi:type=\"xsd:string\">%s</STAN>
                            </Parameters>
                        </bin:assignPIN>
                    </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(bankC, groupC, card, expiry, pinBlock, stan);
    }
}
