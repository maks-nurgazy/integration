package kg.rsk.integrationmpc.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

/**
 * Utility methods for deriving ZPK keys and building PIN blocks.
 */
public final class PinBlockUtil {

    private PinBlockUtil() {
    }

    /**
     * Derive the final ZPK by XORing two components.
     *
     * @param component1 first ZPK component in hex
     * @param component2 second ZPK component in hex
     * @return resulting ZPK in hex
     */
    public static String deriveZpk(String component1, String component2) {
        BigInteger c1 = new BigInteger(component1, 16);
        BigInteger c2 = new BigInteger(component2, 16);
        BigInteger result = c1.xor(c2);
        return String.format("%016X", result);
    }

    /**
     * Build a PIN block using ISO-0 format and encrypt it with the provided ZPK.
     *
     * @param pin the clear PIN
     * @param pan the card PAN
     * @param zpk final ZPK in hex
     * @return encrypted PIN block in hex
     */
    public static String buildEncryptedPinBlock(String pin, String pan, String zpk) throws Exception {
        String pinBlock = formatPinBlock(pin);
        String panBlock = formatPanBlock(pan);
        BigInteger block = new BigInteger(pinBlock, 16).xor(new BigInteger(panBlock, 16));
        byte[] blockBytes = toBytes(String.format("%016X", block));

        // 3DES key uses the ZPK twice to form a 16 byte key
        byte[] keyBytes = toBytes(zpk + zpk);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(blockBytes);
        return toHex(encrypted);
    }

    private static String formatPinBlock(String pin) {
        StringBuilder block = new StringBuilder("0" + pin.length() + pin);
        while (block.length() < 16) {
            block.append("F");
        }
        return block.toString();
    }

    private static String formatPanBlock(String pan) {
        String twelve = pan.substring(pan.length() - 13, pan.length() - 1);
        return "0000" + twelve;
    }

    private static byte[] toBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
