import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Scanner;

//-------------------
//  Author: 4775194
//-------------------

public class RSABase {
    private static RSABase instance = new RSABase();
    public Port port;
    private Key key;

    private RSABase() {
        port = new Port();
    }

    public static RSABase getInstance() {
        return instance;
    }

    private String encryptMessage(String plainMessage, File publicKeyfile) throws FileNotFoundException {
        readPublicKeyFile(publicKeyfile);

        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        byte[] encrypted = crypt(new BigInteger(bytes), key).toByteArray();
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decryptMessage(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
        readPrivateKeyFile(privateKeyfile);

        System.out.println("got here");

        byte[] cipher = Base64.getDecoder().decode(encryptedMessage);
        byte[] msg = crypt(new BigInteger(cipher), key).toByteArray();
        return new String(msg);
    }

    private BigInteger crypt(BigInteger message, Key key) {
        return message.modPow(key.getE(), key.getN());
    }

    private void readPrivateKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFromFile(keyfile, "d");
    }

    private void readPublicKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFromFile(keyfile);
    }

    private void readKeyFromFile(File keyfile) throws FileNotFoundException {
        readKeyFromFile(keyfile, "e");
    }

    private void readKeyFromFile(File keyfile, String eReplacement) throws FileNotFoundException {
        BigInteger n = null;
        BigInteger e = null;

        Scanner scanner = new Scanner(keyfile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("\"n\":")) {
                n = getParameter(line);
            } else if (line.contains("\"" + eReplacement + "\":")) {
                e = getParameter(line);
            }
        }

        this.key = new Key(n, e);
    }

    private BigInteger getParameter(String input) {
        String[] lineParts = input.split(":");
        String line = lineParts[1];
        line = line.replace(",", "").trim();
        return new BigInteger(line);
    }

    public class Port implements IRSABase {
        @Override
        public String version() {
            return null;
        }

        @Override
        public String encrypt(String plainMessage, File publicKeyfile) throws FileNotFoundException {
            return encryptMessage(plainMessage, publicKeyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
            return decryptMessage(encryptedMessage, privateKeyfile);
        }
    }
}