import java.io.File;

public interface IShiftBase {
    String version();

    String encrypt(String plainMessage, File keyfile);

    String decrypt(String encryptedMessage, File keyfile);
}