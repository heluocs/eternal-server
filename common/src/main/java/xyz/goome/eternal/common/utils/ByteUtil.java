package xyz.goome.eternal.common.utils;

/**
 * Created by matrix on 16/3/27.
 */
public class ByteUtil {

    public static byte[] int2byte(int value) {
        byte[] arr = new byte[4];
        arr[0] = (byte) ( value & 0xff );
        arr[1] = (byte) ( (value >> 8) & 0xff );
        arr[2] = (byte) ( (value >> 16) & 0xff );
        arr[3] = (byte) ( (value >> 24) & 0xff );
        return arr;
    }

    public static byte[] short2byte(short value) {
        byte[] arr = new byte[2];
        arr[0] = (byte) ( value & 0xff );
        arr[1] = (byte) ( (value >> 8) & 0xff );
        return arr;
    }

    public static int byte2int(byte[] arr) {

        return (arr[3] << 24) | (arr[2] << 16) | (arr[1] << 8) | (arr[0] & 0xff);
    }

    public static short byte2short(byte[] arr) {
        return (short) ((arr[1] << 8) | (arr[0] & 0xff));
    }
}
