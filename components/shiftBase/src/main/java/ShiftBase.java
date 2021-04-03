import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class ShiftBase {
    // static instance
    private static final ShiftBase instance = new ShiftBase();

    // port
    public final Port port;

    int key;

    // private constructor
    private ShiftBase() {
        port = new Port();
    }

    // static method getInstance
    public static ShiftBase getInstance() {
        return instance;
    }

    // inner methods

    //Encrypt Message with Key from JSON File
    private String innerEncrypt(String plainMessage, File keyfile) {
        this.key = readJsonFile(keyfile);

        System.out.println("Encrypting using shift");
        System.out.println("plain message: \"" + plainMessage + "\"");
        System.out.println("key: " + key);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < plainMessage.length(); i++) {
            char character = plainMessage.charAt(i);

            System.out.print("'" + character + "' -> '");

            if (character >= 'a' && character <= 'z') {
                character += key;
                while (character > 'z') {
                    character -= (26);
                }
                while (character < 'a') {
                    character += (26);
                }
            }
            System.out.println(character + "'");

            stringBuilder.append(character);
        }
        System.out.println("cypher: \"" + stringBuilder.toString() + "\"");

        return stringBuilder.toString();
    }

    //Decrypt Message with Key from JSON File
    private String innerDecrypt(String encryptedMessage, File keyfile) {
        this.key = readJsonFile(keyfile);

        System.out.println("Decrypting using shift");
        System.out.println("cypher: \"" + encryptedMessage + "\"");
        System.out.println("key: " + key);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = encryptedMessage.charAt(i);

            System.out.print("'" + character + "' -> '");

            if (character >= 'a' && character <= 'z') {
                character -= key;
                while (character > 'z') {
                    character -= (26);
                }
                while (character < 'a') {
                    character += (26);
                }
            }
            System.out.println(character + "'");


            stringBuilder.append(character);
        }
        System.out.println("plain message: \"" + stringBuilder.toString() + "\"");


        return stringBuilder.toString();
    }


    //Read JSON File into Integer Key
    private int readJsonFile(File keyfile) {
        int key;
        try {
            FileReader reader = new FileReader(keyfile);
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            key = Integer.parseInt(jsonObject.get("key").toString());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return key;
    }


    // inner class port
    public class Port implements IShiftBase {
        @Override
        public String encrypt(String plainMessage, File keyfile) {
            return innerEncrypt(plainMessage.toLowerCase(), keyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File keyfile) {
            return innerDecrypt(encryptedMessage.toLowerCase(), keyfile);
        }
    }
}