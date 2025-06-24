package kg.rsk.integrationmpc.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility methods for deriving ZPK keys and building PIN blocks.
 */
public final class PinBlockUtil {

    private PinBlockUtil() {}

    /**
     * Derive the final ZPK by XORing two components.
     *
     * @param component1 first ZPK component in hex
     * @param component2 second ZPK component in hex
     * @return resulting ZPK in hex
     */
    public static String deriveZpk(String component1, String component2) {
        // Remove spaces for safe parsing
        component1 = component1.replaceAll("\\s+", "");
        component2 = component2.replaceAll("\\s+", "");
        long c1 = Long.parseUnsignedLong(component1, 16);
        long c2 = Long.parseUnsignedLong(component2, 16);
        long result = c1 ^ c2;
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
        String pinBlock = createPinBlock(pin);
        String panBlock = createPanBlock(pan);
        byte[] clearPinBlockBytes = xor(hexToBytes(pinBlock), hexToBytes(panBlock));

        byte[] keyBytes = expandZpkFor3Des(zpk);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(clearPinBlockBytes);
        return bytesToHex(encrypted);
    }

    /**
     * Calculate the KCV for a given ZPK.
     * KCV is the first 6 hex digits of 3DES(ZPK, 8 zero bytes).
     */
    public static String calculateKcv(String zpk) throws Exception {
        byte[] keyBytes = expandZpkFor3Des(zpk);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] zeros = new byte[8];
        byte[] encrypted = cipher.doFinal(zeros);
        return bytesToHex(encrypted).substring(0, 6);
    }

    /**
     * Expands ZPK (hex string) to a 24-byte key for 3DES encryption.
     * Supports single, double, and triple-length ZPKs.
     */
    private static byte[] expandZpkFor3Des(String zpk) {
        byte[] zpkBytes = hexToBytes(zpk);
        byte[] keyBytes = new byte[24];
        if (zpkBytes.length == 8) {
            // Single-length: K1 | K1 | K1
            System.arraycopy(zpkBytes, 0, keyBytes, 0, 8);
            System.arraycopy(zpkBytes, 0, keyBytes, 8, 8);
            System.arraycopy(zpkBytes, 0, keyBytes, 16, 8);
        } else if (zpkBytes.length == 16) {
            // Double-length: K1 | K2 | K1
            System.arraycopy(zpkBytes, 0, keyBytes, 0, 8);
            System.arraycopy(zpkBytes, 8, keyBytes, 8, 8);
            System.arraycopy(zpkBytes, 0, keyBytes, 16, 8);
        } else if (zpkBytes.length == 24) {
            // Triple-length: K1 | K2 | K3
            System.arraycopy(zpkBytes, 0, keyBytes, 0, 8);
            System.arraycopy(zpkBytes, 8, keyBytes, 8, 8);
            System.arraycopy(zpkBytes, 16, keyBytes, 16, 8);
        } else {
            throw new IllegalArgumentException("ZPK must be 16, 32, or 48 hex digits (8, 16, or 24 bytes)");
        }
        return keyBytes;
    }

    private static String createPinBlock(String pin) {
        StringBuilder block = new StringBuilder("0" + pin.length() + pin);
        while (block.length() < 16) {
            block.append("F");
        }
        return block.toString();
    }

    private static String createPanBlock(String pan) {
        String twelve = pan.substring(pan.length() - 13, pan.length() - 1);
        return "0000" + twelve;
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private static byte[] xor(byte[] first, byte[] second) {
        byte[] result = new byte[first.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = (byte) (first[i] ^ second[i]);
        }
        return result;
    }
}
