package kg.rsk.integrationmpc.service;

import kg.rsk.integrationmpc.config.IssuingServiceProperties;
import kg.rsk.integrationmpc.util.PinBlockUtil;
import kg.rsk.integrationmpc.ws.AssignPinSoapRequest;
import kg.rsk.integrationmpc.ws.OperationConnectionInfo;
import kg.rsk.integrationmpc.ws.RowTypeAssignPINRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class IssuingServiceImpl implements IssuingService {

    private final WebClient webClient;
    private final IssuingServiceProperties issuingServiceProperties;
    private static final Logger log = LoggerFactory.getLogger(IssuingServiceImpl.class);

    @Override
    public String assignPin(String card, String expiry, String pin, String pan, String stan) throws Exception {
        log.info("Assigning PIN for card [{}]", card);
        String zpk = PinBlockUtil.deriveZpk(
                issuingServiceProperties.getZpkComponent1(),
                issuingServiceProperties.getZpkComponent2());
        String pinBlock = PinBlockUtil.buildEncryptedPinBlock(pin, pan, zpk);

        OperationConnectionInfo connectionInfo = new OperationConnectionInfo();
        connectionInfo.setBankC(issuingServiceProperties.getBankC());
        connectionInfo.setGroupC(issuingServiceProperties.getGroupC());

        RowTypeAssignPINRequest params = new RowTypeAssignPINRequest();
        params.setCard(card);
        params.setExpiry(expiry);
        params.setPinblock(pinBlock);
        params.setStan(stan);

        AssignPinSoapRequest request = new AssignPinSoapRequest();
        request.setConnectionInfo(connectionInfo);
        request.setParameters(params);
        String body = request.toXml();

        try {
            return webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception ex) {
            log.error("Error during assignPIN request", ex);
            throw ex;
        }
    }
}
