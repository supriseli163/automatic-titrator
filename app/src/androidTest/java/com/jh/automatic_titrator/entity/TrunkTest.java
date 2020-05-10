package com.jh.automatic_titrator.entity;

import com.jh.automatic_titrator.common.trunk.TrunkUtil;

import org.junit.Test;

public class TrunkTest {
    @Test
    public void testComputeSumCheck() {
        byte[] b = new byte[7];
        b[0] = (byte) 0xfd;
        b[1] = (byte) 0xfc;
        b[2] = (byte) 0x08;
        b[3] = (byte) 0x80;
        b[4] = (byte) 0x02;
        b[5] = (byte) 0x00;
        b[6] = (byte) 0x0a;

        byte result = TrunkUtil.sumCheck(b, 7);
        System.out.printf("%x", result);//正确的结果应该是8d
        System.out.printf(String.valueOf(result));//正确的结果应该是8d
    }

    @Test
    public void parseCmd() {
        String cmd = "AA 55 00 19 80 15 00 00 19 CB 00 00 00 00 27 12 00 B0 00 00 00 02 7B CC 33";
        String start = cmd.substring(4);
        System.out.println(start);
    }

    public static void main(String[] args) {
        String cmd = "AA 55 00 19 80 15 00 00 19 CB 00 00 00 00 27 12 00 B0 00 00 00 02 7B CC 33";
        String start = cmd.substring(4);
        System.out.println(start);

        byte[] b = new byte[7];
        b[0] = (byte) 0xfd;
        b[1] = (byte) 0xfc;
        b[2] = (byte) 0x08;
        b[3] = (byte) 0x80;
        b[4] = (byte) 0x02;
        b[5] = (byte) 0x00;
        b[6] = (byte) 0x0a;

        byte result = TrunkUtil.sumCheck(b, 7);
        System.out.printf("%x", result);//正确的结果应该是8d
        System.out.printf(String.valueOf(result));//正确的结果应该是8d
    }
}
