package migemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class CompactHiraganaString {
    public static String decode(byte[] bytes) {
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = decode(bytes[i]);
        }
        return new String(chars);
    }

    public static char decode(byte b) {
        char c = (char)Byte.toUnsignedInt(b);
        if (0x20 <= c && c <= 0x7e) {
            return c;
        }
        if (0xa1 <= c && c <= 0xf6) {
            return (char) (c + 0x3040 - 0xa0);
        }
        throw new IllegalArgumentException();
    }

    public static byte[] encode(String string) {
        Objects.requireNonNull(string);
        byte[] result = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            result[i] = encode(string.charAt(i));
        }
        return result;
    }

    public static byte encode(char c) {
        if (0x20 <= c && c <= 0x7e) {
            return (byte)c;
        }
        if (0x3041 <= c && c <= 0x3096) {
            return (byte)(c - 0x3040 + 0xa0);
        }
        if (0x30fc == c) {
            return (byte)(c - 0x3040 + 0xa0);
        }
        throw new IllegalArgumentException();
    }
}
