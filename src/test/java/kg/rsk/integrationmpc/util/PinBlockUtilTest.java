package kg.rsk.integrationmpc.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PinBlockUtilTest {

    @Test
    void deriveZpkXorComponents() {
        String result = PinBlockUtil.deriveZpk("0123456789ABCDEF", "FEDCBA9876543210");
        assertEquals("FFFFFFFFFFFFFFFF", result);
    }

    @Test
    void buildEncryptedPinBlockExample() throws Exception {
        String encrypted = PinBlockUtil.buildEncryptedPinBlock("1234", "4000001234567899", "0123456789ABCDEF");
        assertEquals("A1B8278CD8BB66F9", encrypted);
    }
}
