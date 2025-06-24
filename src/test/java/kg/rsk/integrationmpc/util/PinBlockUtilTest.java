package kg.rsk.integrationmpc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinBlockUtilTest {

    @Test
    void deriveZpkShouldXorComponents() {
        String zpk = PinBlockUtil.deriveZpk("4034B6791FA2549D", "9164EF29385107D9");
        assertEquals("D150595027F35344", zpk);
    }

    @Test
    void buildEncryptedPinBlock() throws Exception {
        String zpk = "D150595027F35344";
        String block = PinBlockUtil.buildEncryptedPinBlock("1234", "9417089192319169", zpk);
        assertNotNull(block);
        assertEquals(16, block.length());
    }
}
