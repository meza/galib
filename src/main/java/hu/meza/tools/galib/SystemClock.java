package hu.meza.tools.galib;

import org.joda.time.DateTimeUtils;

public class SystemClock implements Clock {

	private static final long MILLIS_TO_SECONDS = 1000L;

	@Override
	public long getEpochTime() {
		return DateTimeUtils.currentTimeMillis() / MILLIS_TO_SECONDS;
	}
}
