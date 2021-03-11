import java.io.File;

public interface IRSABase {
    String version();

    String encrypt(String plainMessage, File publicKeyfile);

    String decrypt(String encryptedMessage, File privateKeyfile);
}