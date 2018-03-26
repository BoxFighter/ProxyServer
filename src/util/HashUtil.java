package util;

/**
 * hash
 * @author ����
 * @since 16/12/17
 */
public class HashUtil {

    /**
     * ����url����ȡhex string representation
     * @param hash
     * @return String
     */
    public static String bytesToHex(byte[] hash) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                stringBuilder.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                stringBuilder.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return stringBuilder.toString();
    }
}
