package lab;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        String s = "呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃呃";
        byte[]bytes = s.getBytes("UTF-8");
        messageDigest.update(bytes);
        byte[]result = messageDigest.digest();
        System.out.println(result.length);
        for(byte b : result){
            System.out.printf("%02x",b);
        }
    }
}
