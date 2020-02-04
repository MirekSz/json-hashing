import java.util.Arrays;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;

public class ByteEncryptorDemo {

	public static void main(final String[] args) {
		String text = "The quick brown fox jumps over the lazy dog";
		System.out.println("Text      = " + Arrays.toString(text.getBytes()));

		StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor();
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor.setPassword("HelloWorld");

		byte[] encrypted = encryptor.encrypt(text.getBytes());
		System.out.println("Encrypted = " + Arrays.toString(encrypted));

		byte[] original = encryptor.decrypt(encrypted);
		System.out.println("Original  = " + Arrays.toString(original) + new String(original));
	}
}
