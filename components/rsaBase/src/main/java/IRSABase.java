import java.io.File;
import java.io.FileNotFoundException;

public interface IRSABase {
    String encrypt(String plainMessage, File publicKeyfile) throws FileNotFoundException;

    String decrypt(String encryptedMessage, File privateKeyfile) throws FileNotFoundException;
}