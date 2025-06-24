package kg.rsk.integrationmpc.ws;

import lombok.Data;

@Data
public class RowTypeAssignPINRequest {
    private String card;
    private String expiry;
    private String pinblock;
    private String stan;
}
