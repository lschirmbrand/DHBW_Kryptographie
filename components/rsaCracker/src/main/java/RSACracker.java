import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

//-------------------
//  Author: 4775194
//-------------------

public class RSACracker {
    private final static RSACracker instance = new RSACracker();
    public final Port port;
    private BigInteger e = BigInteger.ZERO;
    private BigInteger n = BigInteger.ZERO;
    private Date start;

    private RSACracker() {
        port = new Port();
    }

    public static RSACracker getInstance() {
        return instance;
    }

    private String decryptMessage(String encryptedMessage, File publicKeyfile) throws FileNotFoundException, RSACrackingTimeoutException {
        start = new Date();
        readKeyFile(publicKeyfile);

        byte[] bytes = Base64.getDecoder().decode(encryptedMessage);

        try {
            BigInteger plain = execute(new BigInteger(bytes));
            if (plain == null)
                return "";
            byte[] plainBytes = plain.toByteArray();
            return new String(plainBytes);
        } catch (RSACrackingException rsae) {
            System.out.println(rsae.getMessage());
        }
        return null;
    }

    private void readKeyFile(File keyfile) throws FileNotFoundException {
        /*
        Example:
        {
          "n": 7448411,
          "e": 7442947,
          "d": 4465771
        }
         */
        Scanner scanner = new Scanner(keyfile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("\"n\":")) {
                n = getParam(line);
            } else if (line.contains("\"e\":")) {
                e = getParam(line);
            }
        }
    }

    private BigInteger getParam(String input) {
        String[] lineParts = input.split(":");
        String line = lineParts[1];
        line = line.replace(",", "").trim();
        return new BigInteger(line);
    }

    private BigInteger execute(BigInteger cipher) throws RSACrackingException, RSACrackingTimeoutException {
        BigInteger p, q, d;
        List<BigInteger> factorList = factorize(n);

        if (factorList == null)
            return null;

        if (factorList.size() != 2) {
            throw new RSACrackingException("cannot determine factors p and q");
        }

        p = factorList.get(0);
        q = factorList.get(1);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
        return cipher.modPow(d, n);
    }

    public List<BigInteger> factorize(BigInteger n) throws RSACrackingTimeoutException {
        BigInteger two = BigInteger.valueOf(2);
        List<BigInteger> factorList = new LinkedList<>();

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }

        while (n.mod(two).equals(BigInteger.ZERO)) {
            factorList.add(two);
            n = n.divide(two);
            if (Thread.currentThread().isInterrupted())
                return null;
            checkMaxTime();
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger factor = BigInteger.valueOf(3);
            while (factor.multiply(factor).compareTo(n) <= 0) {
                if (n.mod(factor).equals(BigInteger.ZERO)) {
                    factorList.add(factor);
                    n = n.divide(factor);
                } else {
                    factor = factor.add(two);
                }
                if (Thread.currentThread().isInterrupted())
                    return null;
                checkMaxTime();
            }
            factorList.add(n);
        }

        return factorList;
    }

    private void checkMaxTime() throws RSACrackingTimeoutException {
        if ((new Date().getTime() - start.getTime()) / 1000 > 30) {
            throw new RSACrackingTimeoutException();
        }
    }

    public class Port implements IRSACracker {
        public String decrypt(String encryptedMessage, File publicKeyfile) throws FileNotFoundException, RSACrackingTimeoutException {
            return decryptMessage(encryptedMessage, publicKeyfile);
        }
    }
}