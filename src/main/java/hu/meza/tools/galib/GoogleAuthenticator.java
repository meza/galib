package hu.meza.tools.galib;

import org.joda.time.DateTimeUtils;

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
	public static final int MAX_VALUE = 0xF;


	public String getCode(String secret) {
		return getCode(secret, getTimeWindow(DateTimeUtils.currentTimeMillis()));
	}

	public String getCode(String secret, long time) {
		byte[] sKey = Base32.decode(secret);

		byte[] data = ByteBuffer.allocate(CAPACITY).putLong(time).array();

		try {
			SecretKeySpec signKey = new SecretKeySpec(sKey, "");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signKey);

			byte[] hash = mac.doFinal(data);
			int offset = hash[hash.length - 1] & MAX_VALUE;

			int truncatedHash = hashToInt(hash, offset) & MAXVALUE;
			int pinValue = truncatedHash % (int) Math.pow(POWER_BASE_FOR_TRUNCATE, TOKEN_LENGTH);

			return padOutput(pinValue);
		} catch (Exception e) {
			throw new CodeGenerationException(e);
		}
	}

	public boolean isValidCode(String secret, String codeToVerify) {
		return isValidCode(secret, codeToVerify, 1);
	}

	public boolean isValidCode(String secret, String codeToVerify, int numberOfTimeWindows) {
		long currentTime = getTimeWindow(System.currentTimeMillis());

		for (int i = numberOfTimeWindows; i > 0; i--) {
			long time = currentTime - i;
			String validCode = getCode(secret, time);
			if (validCode.equals(codeToVerify)) {
				return true;
			}
		}
		return false;
	}

	public String qRBarcodeURL(String user, String host, String secret) {
		String format =
			"https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s" +
			"%%3Fsecret" +
			"%%3D%s";
		return String.format(format, user, host, secret);
	}

	private long getTimeWindow(long baseTime) {
		return (baseTime / MILIS_TO_SECONDS) / TIME_WINDOW;
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


