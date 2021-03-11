import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class RSABase {
    // static instance
    private static final RSABase instance = new RSABase();

    // port
    public Port port;

    int key;
    String plainMessage;
    String encryptedMessage;

    // private constructor
    private RSABase() {
        port = new Port();
    }

    // static method getInstance
    public static RSABase getInstance() {
        return instance;
    }

    // inner methods
    public String innerVersion() {
        return "RSABase";
    }


    //Encrypt Message with Key from JSON File
    private String innerEncrypt(String plainMessage, File keyfile){
        this.key = readJsonFile(keyfile);

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i< plainMessage.length(); i++){
            char character = (char) (plainMessage.codePointAt(i) + key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    //Decrypt Message with Key from JSON File
    private String innerDecrypt(String encryptedMessage, File keyfile){
        this.key = readJsonFile(keyfile);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = (char) (encryptedMessage.codePointAt(i) - key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }


    //Read JSON File into Integer Key
    private int readJsonFile(File keyfile){
        int key;
        try{
            FileReader reader = new FileReader(keyfile);
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            key = Integer.parseInt(jsonObject.get("key").toString());

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }

        return key;
    }


    // inner class port
    public class Port implements IRSABase {
        @Override
        public String version() {
            return innerVersion();
        }

        @Override
        public String encrypt(String plainMessage, File keyfile) {
            return innerDecrypt(plainMessage, keyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File keyfile) {
            return innerEncrypt(encryptedMessage, keyfile);
        }
    }
}