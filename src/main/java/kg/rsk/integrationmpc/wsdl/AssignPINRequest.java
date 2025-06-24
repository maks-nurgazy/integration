package kg.rsk.integrationmpc.wsdl;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import kg.rsk.integrationmpc.wsdl.pin.OperationConnectionInfo;
import kg.rsk.integrationmpc.wsdl.pin.RowTypeAssignPINRequest;
import lombok.Data;

@Data
@XmlRootElement(name = "assignPINRequest", namespace = "urn:IssuingWS")
public class AssignPINRequest {
    @XmlElement(name = "ConnectionInfo", namespace = "urn:IssuingWS")
    private OperationConnectionInfo connectionInfo;

    @XmlElement(name = "Parameters", namespace = "urn:IssuingWS")
    private RowTypeAssignPINRequest parameters;
}
