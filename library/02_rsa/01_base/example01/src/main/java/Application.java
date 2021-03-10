import java.util.Base64;

public class Application {
    public static void main(String... args) {
        String plainMessage = "morpheus";
        RSA rsa = new RSA(48);

        Cipher cipher = new Cipher();
        byte[] encryptedMessage = cipher.encrypt(plainMessage, rsa.getPublicKey());

        System.out.println("plainMessage     : " + plainMessage);
        System.out.println("encryptedMessage : " + Base64.getEncoder().encodeToString(encryptedMessage));
        System.out.println("decryptedMessage : " + cipher.decrypt(encryptedMessage, rsa.getPrivateKey()));
    }
}