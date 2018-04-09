
package hello;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import groovy.lang.GroovyShell;

public class AesUtil {

	private static final String PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
	private static final String AES = "AES";
	private static final String UTF_8 = "UTF-8";
	static final int ITERATION_COUNT = 100;
	static final int KEY_SIZE = 128;
	private final Cipher cipher;

	public AesUtil() {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw fail(e);
		}
	}

	public String encrypt(final String salt, final String iv, final String passphrase, final String plaintext) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes(UTF_8));
			return base64(encrypted);
		} catch (UnsupportedEncodingException e) {
			throw fail(e);
		}
	}

	public String decrypt(final String salt, final String iv, final String passphrase, final String ciphertext) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
			return new String(decrypted, UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw fail(e);
		}
	}

	private byte[] doFinal(final int encryptMode, final SecretKey key, final String iv, final byte[] bytes) {
		try {
			cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
			return cipher.doFinal(bytes);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw fail(e);
		}
	}

	private SecretKey generateKey(final String salt, final String passphrase) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);
			KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), ITERATION_COUNT, KEY_SIZE);
			SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);
			return key;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw fail(e);
		}
	}

	public static String random(final int length) {
		byte[] salt = new byte[length];
		new SecureRandom().nextBytes(salt);
		return hex(salt);
	}

	public static String base64(final byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base64(final String str) {
		return Base64.decodeBase64(str);
	}

	public static String hex(final byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	public static byte[] hex(final String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException(e);
		}
	}

	private IllegalStateException fail(final Exception e) {
		return new IllegalStateException(e);
	}

	public static void main(final String[] args) {
		GroovyShell groovyShell = new GroovyShell();
		// Demo evaluate = (Demo) groovyShell.evaluate("[a:{->return 4;},b:{->return 4;}]as hello.Demo");
		Demo evaluate = (Demo) groovyShell.evaluate("new hello.Demo(){def int a(){return 2;};def int b(){return 2;}};");
		// Object demoImlp = new DemoImlp();
		// Demo evaluate = (Demo) demoImlp;
		System.out.println(evaluate.a());
		System.out.println(evaluate.b());
	}

	// public static void main(final String[] args) {
	// AesUtil aesUtil = new AesUtil();
	// String salt = "2aef08914a6be27daee7933134961c58";
	// String four = "62a773f639a7bec5c3d9b5bb9d0b0222";
	// String plaintext = aesUtil.decrypt(salt, four, "haslo", "Mv6t5rgrGTC+oMLpOWAOEGwV2FZaz6X6nkEvBOQ0s3Y=");
	// System.out.println(plaintext);
	// }
}
