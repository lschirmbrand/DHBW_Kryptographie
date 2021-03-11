import java.io.File;

public interface IShiftCracker {
    String version();

    String decrypt(String encryptedMessage);
}