package com.jh.automatic_titrator.common.utils;

public class HexUtil {
    /**
     * 字节转16进制String
     *
     * @param b 字节
     * @return
     */
    public static String toHexString(byte b) {
        return toHexString(toByteArray(b));
    }

    /**
     * 字节数组转16进制String，无分隔，如：FE00120F0E
     *
     * @param array 字节数组
     * @return
     */
    public static String toHexString(byte[] array) {
        return toHexString(array, 0, array.length);
    }

    /**
     * 字节数组转16进制String，无分隔，如：FE00120F0E
     *
     * @param array  字节数组
     * @param offset 起始
     * @param length 长度
     * @return
     */
    public static String toHexString(byte[] array, int offset, int length) {
        char[] buf = new char[length * 2];

        int bufIndex = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = array[i];
            buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
            buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
        }

        return new String(buf);
    }


    private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};//十六进制的组成元素


    /**
     * int转16进制String
     *
     * @param i
     * @return
     */
    public static String toHexString(int i) {
        return toHexString(toByteArray(i));
    }

    /**
     * 字节转数组
     *
     * @param b
     * @return
     */
    public static byte[] toByteArray(byte b) {
        byte[] array = new byte[1];
        array[0] = b;
        return array;
    }

    /**
     * int转字节数组
     *
     * @param i
     * @return
     */
    public static byte[] toByteArray(int i) {
        byte[] array = new byte[4];

        array[3] = (byte) (i & 0xFF);
        array[2] = (byte) ((i >> 8) & 0xFF);
        array[1] = (byte) ((i >> 16) & 0xFF);
        array[0] = (byte) ((i >> 24) & 0xFF);

        return array;
    }

    /**
     * 十六进制转int
     *
     * @param c
     * @return
     */
    private static int toByte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);

        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexString 如：FE00120F0E
     * @return
     */
    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i + 1)));
        }

        return buffer;
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexString 如：FE00120F0E
     * @return
     */
    public static byte[] hexStringToByteArrayWithSpiliter(String hexString) {
        String afterSpilit = hexString.replace(" ", "");
        int length = afterSpilit.length();
        byte[] buffer = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(afterSpilit.charAt(i)) << 4) | toByte(afterSpilit.charAt(i + 1)));
        }

        return buffer;
    }

    /**
     * 字节数组以String形式输出 字节间以空格分隔
     *
     * @param array  字节数组
     * @param offset 起始位置
     * @param length 长度
     * @return
     */
    public static String dumpHexString(byte[] array, int offset, int length) {
        StringBuilder result = new StringBuilder();

        byte[] line = new byte[16];
        int lineIndex = 0;

        result.append("\n0x");
        result.append(toHexString(offset));

        for (int i = offset; i < offset + length; i++) {
            if (lineIndex == 16) {
                result.append(" ");

                for (int j = 0; j < 16; j++) {
                    if (line[j] > ' ' && line[j] < '~') {
                        result.append(new String(line, j, 1));
                    } else {
                        result.append(".");
                    }
                }

                result.append("\n0x");
                result.append(toHexString(i));
                lineIndex = 0;
            }

            byte b = array[i];
            result.append(" ");
            result.append(HEX_DIGITS[(b >>> 4) & 0x0F]);
            result.append(HEX_DIGITS[b & 0x0F]);

            line[lineIndex++] = b;
        }

        if (lineIndex != 16) {
            int count = (16 - lineIndex) * 3;
            count++;
            for (int i = 0; i < count; i++) {
                result.append(" ");
            }

            for (int i = 0; i < lineIndex; i++) {
                if (line[i] > ' ' && line[i] < '~') {
                    result.append(new String(line, i, 1));
                } else {
                    result.append(".");
                }
            }
        }

        return result.toString();
    }

//    /**
//     * int 转换为固定位数的16进制
//     * @param value
//     * @param size
//     * @return
//     */
//    public static byte convertToHex(int value, int size) {
//
//    }


    /**
     * 将字节数组前4字节转换为整型数值
     *
     * @param bytes
     * @return
     */
    public static int getInt(byte[] bytes) {
        return (0xff000000 & (bytes[0] << 24) | (0xff0000 & (bytes[1] << 16))
                | (0xff00 & (bytes[2] << 8)) | (0xff & bytes[3]));
    }

    public static String intToHex(int n,int size) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        a  = add_zore(a,size);
        return a;
    }

    public static String add_zore(String str, int size){
        if (str.length()<size){
            str= "0"+str;
            str=add_zore(str,size);
            return str;
        }else {
            return str;
        }
    }

    /**
     * 求校验和的算法
     * @param b 需要求校验和的字节数组
     * @return 校验和
     */
    public static byte sumCheck(byte[] b, int len){
        int sum = 0;
        for(int i = 0; i < len; i++){
            sum = sum + b[i];
        }
        if(sum > 0xff){ //超过了255，使用补码（补码 = 原码取反 + 1）
            sum = ~sum;
            sum = sum + 1;
        }
        return (byte) (sum & 0xff);
    }

    /**
     * 校验和
     *
     * @param msg 需要计算校验和的byte数组
     * @param length 校验和位数
     * @return 计算出的校验和数组
     */
    public static byte[] SumCheck(byte[] msg, int length) {
        long mSum = 0;
        byte[] mByte = new byte[length];

        /** 逐Byte添加位数和 */
        for (byte byteMsg : msg) {
            long mNum = ((long)byteMsg >= 0) ? (long)byteMsg : ((long)byteMsg + 256);
            mSum += mNum;
        } /** end of for (byte byteMsg : msg) */

        /** 位数和转化为Byte数组 */
        for (int liv_Count = 0; liv_Count < length; liv_Count++) {
            mByte[length - liv_Count - 1] = (byte)(mSum >> (liv_Count * 8) & 0xff);
        } /** end of for (int liv_Count = 0; liv_Count < length; liv_Count++) */

        return mByte;
    }

    /**
     * 将byte数组转换为字符串形式表示的十六进制数方便查看
     */
    public static StringBuffer bytesToString(byte[] bytes)
    {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            String s = Integer.toHexString(bytes[i] & 0xFF);
            if (s.length() < 2)
                sBuffer.append('0');
            sBuffer.append(s + " ");
        }
        return sBuffer;
    }
}
