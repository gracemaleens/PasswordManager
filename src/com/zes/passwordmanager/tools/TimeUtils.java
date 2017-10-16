package com.zes.passwordmanager.tools;

import java.util.Date;

public class TimeUtils {
	public static final String DATEFORMAT_PATTERN = "yyyy-MM-dd EE HH:mm:ss";

	private long mTimeDifference;
	private int mTimeDifferenceOfDays;
	private int mTimeDifferenceOfHours;
	private int mTimeDifferenceOfMinutes;
	private int mTimeDifferenceOfSeconds;

	public static int millisToDays(long millis) {
		return (int) (millis / (1000 * 60 * 60 * 24));
	}

	public static int millisToHors(long millis) {
		return (int) (millis / (1000 * 60 * 60));
	}

	public static int millisToMinutes(long millis) {
		return (int) (millis / (1000 * 60));
	}

	public static int millisToSeconds(long mills) {
		return (int) (mills / 1000);
	}

	public static long daysToMillis(int days) {
		return days * (1000 * 60 * 60 * 24 * 1L);
	}

	public static long hoursToMillis(int hours) {
		return hours * (1000 * 60 * 60 * 1L);
	}

	public static long minutesToMillis(int minutes) {
		return minutes * (1000 * 60 * 1L);
	}

	public static long secondsToMillis(int seconds) {
		return seconds * (1000 * 1L);
	}

	public static long timeDifference(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	public static TimeUtils getInstance() {
		return new TimeUtils();
	}

	public void setTimeDifference(long timeDifference) {
		mTimeDifference = timeDifference;
		mTimeDifferenceOfDays = millisToDays(mTimeDifference);// 相差天
		long daysOfMills = daysToMillis(mTimeDifferenceOfDays);// 相差天转化为毫秒
		mTimeDifferenceOfHours = millisToHors(mTimeDifference - daysOfMills);// 相差时
		long hoursOfMills = hoursToMillis(mTimeDifferenceOfHours);// 相差时转化为毫秒
		mTimeDifferenceOfMinutes = millisToMinutes(mTimeDifference - daysOfMills - hoursOfMills);// 相差分
		long minutesOfMills = minutesToMillis(mTimeDifferenceOfMinutes);// 相差分转化为毫秒
		mTimeDifferenceOfSeconds = millisToSeconds(mTimeDifference - daysOfMills - hoursOfMills - minutesOfMills);// 相差秒
	}

	public int getTimeDifferenceOfDays() {
		return mTimeDifferenceOfDays;
	}

	public int getTimeDifferenceOfHours() {
		return mTimeDifferenceOfHours;
	}

	public int getTimeDifferenceOfMinutes() {
		return mTimeDifferenceOfMinutes;
	}

	public int getTimeDifferenceOfSeconds() {
		return mTimeDifferenceOfSeconds;
	}
	
}
