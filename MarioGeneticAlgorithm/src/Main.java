import java.util.Arrays;

public class Main {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static void main(String[] args) {
        byte[] bruh = bytesFromHex(bytesToHex(new byte[]{
                (byte) -1, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
                (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111
        }));
        System.out.println(Arrays.toString(bruh));
    }

    /**
     * https://stackoverflow.com/a/2197650/12861567
     */
    public static String bytesToHex(byte[] bytes) {
        //char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int i = 0; i < bytes.length; i++) {
            v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];// v/16
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F]; // v%16
        }
        return new String(hexChars);
    }

    public static byte[] bytesFromHex(String hexString) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = hexString.toCharArray();
        byte[] result = new byte[hexChars.length / 2];
        for (int j = 0; j < hexChars.length; j += 2) {
            result[j / 2] = (byte) (Arrays.binarySearch(hexArray, hexChars[j]) * 16 + Arrays.binarySearch(hexArray, hexChars[j + 1]));
        }
        return result;
    }

    public static byte getBit(byte b, int position) { return (byte) ((b >> position) & 1); }
}
