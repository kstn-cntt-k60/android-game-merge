package kstn.game.app.network;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestNetworkUtil {

    @Test
    public void littleToBigEndian() {
        int result = NetworkUtil.littleToBigEndian(0x12345678);
        assertEquals(result, 0x78563412);
    }

    @Test
    public void bigEndianToByte() {
        byte[] addr = new byte[4];
        NetworkUtil.bigEndianToByte(0x12345678, addr);
        assertEquals(addr[0], 0x12);
        assertEquals(addr[1], 0x34);
        assertEquals(addr[2], 0x56);
        assertEquals(addr[3], 0x78);
    }

    @Test
    public void intToInetAddress() {
        int addr = 0xC0A80101;
        byte[] tmp = new byte[4];
        NetworkUtil.bigEndianToByte(addr, tmp);
        InetAddress ip = null;
        try {
            ip = InetAddress.getByAddress(tmp);
        } catch (UnknownHostException e) {
            assertEquals(e.getMessage(), "");
        }
        assertEquals(ip.getHostAddress(), "192.168.1.1");
    }

    @Test
    public void ipStringToInt() {
        int ip = NetworkUtil.ipStringToInt("192.168.1.174");
        assertEquals(ip, 0xC0A801AE);
    }

    @Test
    public void byteArrayOutputStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] array = new byte[3];
        array[0] = 0x12;
        array[1] = 0x34;
        array[2] = 0x56;
        try {
            out.write(array);
        } catch (IOException e) {
            assertTrue(false);
        }

        byte[] result = out.toByteArray();
        assertArrayEquals(result, array);
        assertEquals(result.length, 3);
    }

    @Test
    public void bytesToInt() {
        byte[] data = new byte[4];
        data[0] = 0x12;
        data[1] = 0x34;
        data[2] = (byte)0xab;
        data[3] = (byte)0x98;

        assertEquals(0x1234ab98, NetworkUtil.bytesToInt(data));
    }
}
