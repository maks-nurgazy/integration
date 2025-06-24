package kg.rsk.integrationmpc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PinBlockUtilTest {

    @Test
    void deriveZpkXorTripleComponents() throws Exception {
        String comp1 = "4034B6791FA2549D";
        String comp2 = "9164EF29385107D9";
        String zpk = PinBlockUtil.deriveZpk(comp1, comp2);
        assertEquals("D150595027F35344", zpk);
        String kcv = PinBlockUtil.calculateKcv(zpk);
        System.out.println("ZPK: " + zpk + " | KCV: " + kcv);
        assertTrue(kcv.startsWith("734E00".substring(0, 6)));
    }

    @Test
    void buildEncryptedPinBlockExample() throws Exception {
        String comp1 = "4034 B679 1FA2 549D";
        String comp2 = "9164 EF29 3851 07D9";
        String pan = "43219876543210987";
        String pin = "1234";
        String expectedKcv = "9FEEDA";
        String zpk = PinBlockUtil.deriveZpk(comp1, comp2);
        String kcv = PinBlockUtil.calculateKcv(zpk);

        System.out.println("Final ZPK: " + zpk);
        System.out.println("KCV: " + kcv);
//        assertTrue(kcv.startsWith(expectedKcv.substring(0, 6)));

        String encryptedPinBlock = PinBlockUtil.buildEncryptedPinBlock(pin, pan, zpk);
        System.out.println("Encrypted PIN block: " + encryptedPinBlock);
    }
}
