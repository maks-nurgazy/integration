package kg.rsk.integrationmpc.ws;

import lombok.Data;

@Data
public class AssignPinSoapRequest {
    private OperationConnectionInfo connectionInfo;
    private RowTypeAssignPINRequest parameters;

    public String toXml() {
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
                """.formatted(
                connectionInfo.getBankC(),
                connectionInfo.getGroupC(),
                parameters.getCard(),
                parameters.getExpiry(),
                parameters.getPinblock(),
                parameters.getStan());
    }
}
