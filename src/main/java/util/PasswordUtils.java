package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    // Hash a password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password.");
            e.printStackTrace();
            return null;
        }
    }

    // Convert byte array to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Verify if the provided password matches the hashed password
    public static boolean verifyPassword(String providedPassword, String storedHash) {
        String hashedPassword = hashPassword(providedPassword);
        return hashedPassword != null && hashedPassword.equals(storedHash);
    }
}
