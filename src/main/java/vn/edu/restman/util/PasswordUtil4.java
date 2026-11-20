package vn.edu.restman.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

public final class PasswordUtil4 {
    private static final String PREFIX = "pbkdf2_sha256";
    private PasswordUtil4() { }

    public static boolean matches(String plainPassword, String encodedHash) {
        if (plainPassword == null || encodedHash == null) return false;
        String[] parts = encodedHash.split("\\$");
        if (parts.length != 4 || !PREFIX.equals(parts[0])) return false;
        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            PBEKeySpec specification = new PBEKeySpec(plainPassword.toCharArray(), salt, iterations, expected.length * 8);
            byte[] actual = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(specification).getEncoded();
            specification.clearPassword();
            return MessageDigest.isEqual(expected, actual);
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            return false;
        }
    }
}
