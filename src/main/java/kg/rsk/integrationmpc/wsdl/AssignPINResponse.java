package kg.rsk.integrationmpc.wsdl;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import kg.rsk.integrationmpc.wsdl.pin.OperationResponseInfo;
import kg.rsk.integrationmpc.wsdl.pin.RowTypeAssignPINResponse;
import lombok.Data;

@Data
@XmlRootElement(name = "assignPINResponse", namespace = "urn:IssuingWS")
public class AssignPINResponse {
    @XmlElement(name = "ResponseInfo", namespace = "urn:IssuingWS")
    private OperationResponseInfo responseInfo;
    @XmlElement(name = "Details", namespace = "urn:IssuingWS")
    private RowTypeAssignPINResponse details;
}