package kstn.game.app.network;

public class NetworkUtil {

    public static int littleToBigEndian(int addr) {
        int result = 0;
        result |= (addr & 0xff) << 24;
        addr >>>= 8;
        result |= (addr & 0xff) << 16;
        addr >>>= 8;
        result |= (addr & 0xff) << 8;
        addr >>>= 8;
        result |= (addr & 0xff);
        return result;
    }

    public static void bigEndianToByte(int addr, byte[] out) {
        out[3] = (byte) (addr & 0xff);
        addr >>>= 8;
        out[2] = (byte) (addr & 0xff);
        addr >>>= 8;
        out[1] = (byte) (addr & 0xff);
        addr >>>= 8;
        out[0] = (byte) (addr & 0xff);
    }

    public static int ipStringToInt(String addr) {
        String[] parts = addr.split("\\.");
        int result = 0;
        int shift = 24;
        for (int i = 0; i < 4; i++) {
            result |= (Integer.parseInt(parts[i]) << shift);
            shift -= 8;
        }
        return result;
    }

}
