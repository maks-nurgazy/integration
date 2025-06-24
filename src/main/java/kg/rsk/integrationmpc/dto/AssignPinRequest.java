package kg.rsk.integrationmpc.dto;

import lombok.Data;

@Data
public class AssignPinRequest {
    private String card;
    private String expiry;
    private String pin;
    private String pan;
    private String stan;
}
