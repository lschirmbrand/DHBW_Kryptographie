import java.util.ArrayList;
import java.util.List;

//-------------------
//  Author: 4775194
//-------------------

public class ShiftCracker {
    // static instance
    private static final ShiftCracker instance = new ShiftCracker();

    // port
    public final Port port;

    // private constructor
    private ShiftCracker() {
        port = new Port();
    }

    // static method getInstance
    public static ShiftCracker getInstance() {
        return instance;
    }

    // inner methods

    //Decrypt Message with Key from JSON File
    private String innerDecrypt(String encryptedMessage) {

        if (encryptedMessage.equals("")) {
            System.exit(0);
        }

        char[] sourceText = new char[encryptedMessage.length()];
        int[] unicode = new int[encryptedMessage.length()];
        int[] unicodeCopy = new int[encryptedMessage.length()];

        for (int count = 0; count < encryptedMessage.length(); count++) {
            sourceText[count] = encryptedMessage.charAt(count);
        }

        String hex;
        int dec;

        for (int count = 0; count < sourceText.length; count++) {
            hex = Integer.toHexString(sourceText[count]);
            dec = Integer.parseInt(hex, 16);
            unicode[count] = dec;
            unicodeCopy[count] = dec;
        }

        List<String> possibilities = new ArrayList<>();

        for (int shift = 1; shift <= 25; shift++) {
            String possibility = smartShift(shift, unicode, unicodeCopy);
            if (!possibility.equals("")) {
                possibilities.add(possibility);
            }
        }

        return String.join("\n   | ", possibilities);
    }

    private String smartShift(int shift, int[] unicode, int[] unicodeCopy) {
        for (int x = 0; x <= unicode.length - 1; x++) {
            unicodeCopy[x] = unicode[x];

            if (unicode[x] >= 65 && unicode[x] <= 90) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 90) {
                    unicodeCopy[x] -= 26;
                }
            }
        }

        String[] processed = new String[unicode.length];
        char[] finalProcess = new char[unicode.length];

        for (int count = 0; count < processed.length; count++) {
            processed[count] = Integer.toHexString(unicodeCopy[count]);
            int hexToInt = Integer.parseInt(processed[count], 16);
            char intToChar = (char) hexToInt;
            finalProcess[count] = intToChar;
        }

        double frequency = 0;
        double aFrequency = 0;
        double eFrequency = 0;
        double iFrequency = 0;
        double oFrequency = 0;
        double uFrequency = 0;

        for (char c : finalProcess) {
            frequency++;

            switch (c) {
                case 'A':
                    aFrequency++;
                    break;
                case 'E':
                    eFrequency++;
                    break;
                case 'I':
                    iFrequency++;
                    break;
                case 'O':
                    oFrequency++;
                    break;
                case 'U':
                    uFrequency++;
                    break;
                default:
                    break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char character : finalProcess) {
            stringBuilder.append(character);
        }

        if (eFrequency / frequency >= 0.05 || aFrequency / frequency >= 0.05 || iFrequency / frequency >= 0.05 || oFrequency / frequency >= 0.05 || uFrequency / frequency >= 0.05) {
            return (stringBuilder.toString().toLowerCase());
        }

        return "";
    }

    // inner class port
    public class Port implements IShiftCracker {
        @Override
        public String decrypt(String encryptedMessage) {
            return innerDecrypt(encryptedMessage);
        }
    }
}