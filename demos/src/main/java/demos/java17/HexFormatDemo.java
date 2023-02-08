package demos.java17;

import java.util.Arrays;
import java.util.HexFormat;

public class HexFormatDemo {

    public static void main(String[] args) {
        var hexFormat = HexFormat.of().withUpperCase();
        System.out.println(hexFormat.toHexDigits((byte) 14));
        System.out.println(hexFormat.toHexDigits(14));

        System.out.println(Arrays.toString(hexFormat.parseHex("CAFEBABE")));

        System.out.println(hexFormat.formatHex(new byte[]{-54, -2, -70, -66}));

        hexFormat = HexFormat.of().withUpperCase().withDelimiter(" ").withPrefix("0x");
        System.out.println(hexFormat.formatHex(new byte[]{-54, -2, -70, -66}));
    }
}
