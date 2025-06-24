package kg.rsk.integrationmpc.service;

public interface IssuingService {
    String assignPin(String card, String expiry, String pin, String pan, String stan) throws Exception;
}
