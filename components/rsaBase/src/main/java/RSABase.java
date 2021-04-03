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
    private final static RSABase instance = new RSABase();
    public final Port port;
    private Key key;

    private RSABase() {
        port = new Port();
    }

    public static RSABase getInstance() {
        return instance;
    }

    private String encryptMessage(String plainMessage, File publicKeyfile) throws FileNotFoundException {
        readPublicKeyFile(publicKeyfile);
        System.out.println("Encrypting using rsa");
        System.out.println("plain message: \"" + plainMessage + "\"");
        System.out.println("n: " + key.getN());
        System.out.println("e: " + key.getE());

        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        StringBuilder sb = new StringBuilder("message in bytes: ");
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb);

        System.out.println("m ^ e mod n = c");

        byte[] encrypted = crypt(new BigInteger(bytes), key).toByteArray();
        sb = new StringBuilder("cypher in bytes: ");
        for (byte b : encrypted) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb);

        String cypherBase64 = Base64.getEncoder().encodeToString(encrypted);
        System.out.println("cypher in Base64: " + cypherBase64);
        return cypherBase64;
    }

    private String decryptMessage(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
        readPrivateKeyFile(privateKeyfile);
        System.out.println("Decrypting using rsa");
        System.out.println("cypher: \"" + encryptedMessage + "\"");
        System.out.println("n: " + key.getN());
        System.out.println("d: " + key.getE());

        byte[] cipher = Base64.getDecoder().decode(encryptedMessage);
        StringBuilder sb = new StringBuilder("cypher in bytes: ");
        for (byte b : cipher) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb);

        System.out.println("c ^ d mod n = e");
        byte[] msg = crypt(new BigInteger(cipher), key).toByteArray();
        sb = new StringBuilder("message in bytes: ");
        for (byte b : msg) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb);

        String msgStr = new String(msg);
        System.out.println("message in Text: " + msgStr);
        return msgStr;
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
        public String encrypt(String plainMessage, File publicKeyfile) throws FileNotFoundException {
            return encryptMessage(plainMessage, publicKeyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
            return decryptMessage(encryptedMessage, privateKeyfile);
        }
    }
}