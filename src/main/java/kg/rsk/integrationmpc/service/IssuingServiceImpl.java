package kg.rsk.integrationmpc.service;

import kg.rsk.integrationmpc.config.IssuingServiceProperties;
import kg.rsk.integrationmpc.util.PinBlockUtil;
import kg.rsk.integrationmpc.wsdl.AssignPINRequest;
import kg.rsk.integrationmpc.wsdl.AssignPINResponse;
import kg.rsk.integrationmpc.wsdl.pin.OperationConnectionInfo;
import kg.rsk.integrationmpc.wsdl.pin.RowTypeAssignPINRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class IssuingServiceImpl implements IssuingService {

    private final WebServiceTemplate webServiceTemplate;
    private final IssuingServiceProperties issuingServiceProperties;

    @Override
    public String assignPin(String card, String expiry, String pin, String pan, String stan) throws Exception {
        log.info("Assigning PIN for card [{}]", card);
        String zpk = PinBlockUtil.deriveZpk(
                issuingServiceProperties.getZpkComponent1(),
                issuingServiceProperties.getZpkComponent2());
        String pinBlock = PinBlockUtil.buildEncryptedPinBlock(pin, pan, zpk);

        OperationConnectionInfo connectionInfo = new OperationConnectionInfo();
        connectionInfo.setBANKC(issuingServiceProperties.getBankC());
        connectionInfo.setGROUPC(issuingServiceProperties.getGroupC());

        RowTypeAssignPINRequest params = new RowTypeAssignPINRequest();
        params.setCARD(card);
        params.setEXPIRY(expiry);
        params.setPINBLOCK(pinBlock);
        params.setSTAN(stan);

        AssignPINRequest assignPINRequest = new AssignPINRequest();
        assignPINRequest.setConnectionInfo(connectionInfo);
        assignPINRequest.setParameters(params);

        try {
            AssignPINResponse response = (AssignPINResponse) webServiceTemplate.marshalSendAndReceive(assignPINRequest);

            return response.toString();
        } catch (Exception ex) {
            log.error("Error during assignPIN request", ex);
            throw ex;
        }
    }
}
