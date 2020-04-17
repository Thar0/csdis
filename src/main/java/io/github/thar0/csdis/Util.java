package io.github.thar0.csdis;

class Util {

    static byte firstByte(int data) {
        return (byte)((data >> 0) & 0xFF);
    }

    static byte secondByte(int data) {
        return (byte)((data >> 8) & 0xFF);
    }

    static byte thirdByte(int data) {
        return (byte)((data >> 16) & 0xFF);
    }

    static byte fourthByte(int data) {
        return (byte)((data >> 24) & 0xFF);
    }

    static short firstShort(int data) {
        return (short)((data >> 0) & 0xFFFF);
    }

    static short secondShort(int data) {
        return (short)((data >> 16) & 0xFFFF);
    }

    static String formatHex(byte b) {
        return "0x"+pad(Integer.toHexString(b),'0',2,true).toUpperCase();
    }

    static String formatHex(short s) {
        return "0x"+pad(Integer.toHexString(s),'0',4,true).toUpperCase();
    }

    static String formatHex(int i) {
        return "0x"+pad(Integer.toHexString(i),'0',8,true).toUpperCase();
    }

    static String pad(String s, char pad, int len, boolean front) {
        while(s.length() < len) {
            s = (front) ? pad+s : s+pad;
        }
        if(s.length() > len) {
            s = (front) ? s.substring(s.length()-len, s.length()) : s.substring(0, len);
        }
        return s;
    }

    static void print(String s) {
        System.out.println(s);
    }

    static String enumNameByOrdinal(Class<? extends Enum<?>> e, int ordinal) {
        for (Enum<?> dest : e.getEnumConstants()) {
            if (ordinal == dest.ordinal())
                return dest.name();
        }
        return null;
    }
}
