package hu.meza.tools.galib;

import hu.meza.tools.Base32;
import hu.meza.tools.CodeGenerationException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GoogleAuthenticator {


	public static final long TIME_WINDOW = 30L;
	public static final long MILIS_TO_SECONDS = 1000L;
	public static final int CAPACITY = 8;
	public static final int TOKEN_LENGTH = 6;
	public static final int POWER_BASE_FOR_TRUNCATE = 10;
	public static final int MAXVALUE = 0x7FFFFFFF;
	private String secret;

	public GoogleAuthenticator(String secret) {
		this.secret = secret;
	}

	public String getCode() {
		byte[] sKey = Base32.decode(secret);
		long time = (System.currentTimeMillis() / MILIS_TO_SECONDS) / TIME_WINDOW;

		byte[] data = ByteBuffer.allocate(CAPACITY).putLong(time).array();

		try {
			SecretKeySpec signKey = new SecretKeySpec(sKey, "");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signKey);

			byte[] hash = mac.doFinal(data);
			int offset = hash[hash.length - 1] & 0xF;

			int truncatedHash = hashToInt(hash, offset) & MAXVALUE;
			int pinValue = truncatedHash % (int) Math.pow(POWER_BASE_FOR_TRUNCATE, TOKEN_LENGTH);

			return padOutput(pinValue);
		} catch (Exception e) {
			throw new CodeGenerationException(e);
		}
	}

	public static String qRBarcodeURL(String user, String host, String secret) {
		String format =
			"https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s" +
			"%%3Fsecret" +
			"%%3D%s";
		return String.format(format, user, host, secret);
	}


	private String padOutput(int value) {
		String result = Integer.toString(value);
		for (int i = result.length(); i < TOKEN_LENGTH; i++) {
			result = "0" + result;
		}
		return result;
	}

	private int hashToInt(byte[] bytes, int start) {
		DataInput input = new DataInputStream(new ByteArrayInputStream(bytes, start, bytes.length - start));
		int val;
		try {
			val = input.readInt();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return val;
	}
}


