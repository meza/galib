package hu.meza.tools.galib;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class GoogleAuthenticatorTest {

	private static final long DEFAULT_TIME = 1392643545L;
	private static final String SECRET = Base32.encode("I want to play a game!".getBytes());
	private static final String VALID_CODE_FOR_DEFAULT_TIME = "047594";
	private GoogleAuthenticator ga;

	@Before
	public void setUp() {
		Clock clock = Mockito.mock(Clock.class);
		Mockito.when(clock.getEpochTime()).thenReturn(DEFAULT_TIME);
		ga = new GoogleAuthenticator(clock);
	}

	@Test
	public void testGetCode() {
		String expected = VALID_CODE_FOR_DEFAULT_TIME;
		String actual = ga.getCode(SECRET);
		Assert.assertEquals("Failed to generate the correct passcode", expected, actual);
	}

	@Test
	public void testGetCodeForSpecificTime() {
		final long specificTime = 1361512434L;
		String expected = "865344";
		String actual = ga.getCode(SECRET, specificTime);
		Assert.assertEquals("Failed to generate the correct passcode for specified time", expected, actual);
	}

	@Test
	public void testIsValidCode() {

		Assert.assertTrue("Could not validate correct code",
			ga.isValidCode(SECRET, VALID_CODE_FOR_DEFAULT_TIME));

		Assert.assertFalse("Did not recognize invalidity of code", ga.isValidCode(SECRET, "000000"));

	}

	@Test
	public void testIsValidCodeForDifferentTimeWindows() {
		final int numberOfPreviousCodesToAccept = 1;
		final String[] previousCodes = new String[]{"078483", "709656", "808718"};

		for (int i = 0; i < previousCodes.length; i++) {
			if (i < numberOfPreviousCodesToAccept) {
				Assert.assertTrue("Could not validate previous time window's code",
					ga.isValidCode(SECRET, previousCodes[i], numberOfPreviousCodesToAccept));
			} else {
				Assert.assertFalse("Passcode of a distant time window was considered valid",
					ga.isValidCode(SECRET, previousCodes[i], numberOfPreviousCodesToAccept));
			}
		}

	}

	@Test
	public void testQRBarcodeURL() {
		final String user = UUID.randomUUID().toString();
		final String host = UUID.randomUUID().toString();
		final String secret = UUID.randomUUID().toString();

		String expected = String
			.format("https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s" +
					"%%3Fsecret" +
					"%%3D%s", user, host, secret);

		String actual = ga.qRBarcodeURL(user, host, secret);

		Assert.assertEquals("did not generate the correct barcode url", expected, actual);
	}
}
