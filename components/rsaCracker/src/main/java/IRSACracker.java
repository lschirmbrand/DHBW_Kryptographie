import java.io.File;
import java.io.FileNotFoundException;

public interface IRSACracker {
    String version();

    String decrypt(String encryptedMessage, File publicKeyfile) throws FileNotFoundException;
}