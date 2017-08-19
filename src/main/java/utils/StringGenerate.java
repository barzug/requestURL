package utils;

public class StringGenerate {
    private final static String symbols = "abcdefghijklmnopqrstuvwxyz123456789";

    public static String Generate(int stringLength) {
        StringBuilder randString = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            randString.append(symbols.charAt((int) (Math.random() * symbols.length())));
        }

        return randString.toString();
    }
}
