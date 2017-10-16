package com.zes.passwordmanager.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.UUID;

import com.zes.passwordmanager.tools.ITools;
import com.zes.passwordmanager.tools.TimeUtils;

import android.text.TextUtils;

public class Password {
	private static final String TAG = Password.class.getName();
	
	private UUID mId;
	private String mTitle;
	private String mPassword;
	private String mNotes;
	private Date mStartTime;
	private Timelock mTimelock;
	
	public enum Timelock {
		NOT_LOCK,
		ONE_HOUR,
		ONE_DAY,
		ONE_WEEK,
		ONE_MONTH,
		THREE_MONTHS,
		SIX_MONTHS,
		NINE_MONTHS,
		TWELVE_MONTHS;
		
		public static Timelock getTimeLockByOrdinal(int ordinal) {
			return ITools.getEnumByOrdinal(Timelock.class, ordinal);
		}
	}
	
	private static EnumMap<Timelock, String> sTimelockMap = new EnumMap<Password.Timelock, String>(Timelock.class);
	
	static {
		sTimelockMap.put(Timelock.NOT_LOCK, "无");
		sTimelockMap.put(Timelock.ONE_HOUR, "一小时");
		sTimelockMap.put(Timelock.ONE_DAY, "一天");
		sTimelockMap.put(Timelock.ONE_WEEK, "一周");
		sTimelockMap.put(Timelock.ONE_MONTH, "一个月");
		sTimelockMap.put(Timelock.THREE_MONTHS, "三个月");
		sTimelockMap.put(Timelock.SIX_MONTHS, "六个月");
		sTimelockMap.put(Timelock.NINE_MONTHS, "九个月");
		sTimelockMap.put(Timelock.TWELVE_MONTHS, "十二个月");
	}
	
	public static String getTimelocksValue(int index) {
		return sTimelockMap.get(Timelock.getTimeLockByOrdinal(index));
	}
	
	public static int getTimelocksSize() {
		return sTimelockMap.size();
	}
	
	public Password() {
		mId = UUID.randomUUID();
		mTimelock = Timelock.NOT_LOCK;
	}
	
	public Password(String password) {
		this();
		mPassword = password;
	}
	
	@SuppressWarnings("incomplete-switch")
	public long getUnlockRemainingTime() {
		if(mTimelock.ordinal() > 0 && mStartTime != null) {
			Calendar unlockTime = Calendar.getInstance();
			unlockTime.setTime(mStartTime);
			switch(mTimelock) {
			case ONE_HOUR:
				unlockTime.add(Calendar.HOUR_OF_DAY, 1);
				break;
			case ONE_DAY:
				unlockTime.add(Calendar.DAY_OF_MONTH, 1);
				break;
			case ONE_WEEK:
				unlockTime.add(Calendar.WEEK_OF_MONTH, 1);
				break;
			case ONE_MONTH:
				unlockTime.add(Calendar.MONTH, 1);
				break;
			case THREE_MONTHS:
				unlockTime.add(Calendar.MONTH, 3);
				break;
			case SIX_MONTHS:
				unlockTime.add(Calendar.MONTH, 6);
				break;
			case NINE_MONTHS:
				unlockTime.add(Calendar.MONTH, 9);
				break;
			case TWELVE_MONTHS:
				unlockTime.add(Calendar.YEAR, 1);
				break;
			}
			return TimeUtils.timeDifference(unlockTime.getTime(), new Date());
		}
		return 0;
	}
	
	public boolean isTimeLock() {
		if(getTimeLock().ordinal() > 0) {
			if(getUnlockRemainingTime() > 0) {
				return true;
			}else {
				mTimelock = Timelock.NOT_LOCK;
				mStartTime = null;
			}
		}
		return false;
	}

	public UUID getId() {
		return mId;
	}

	public void setId(UUID id) {
		mId = id;
	}
	
	public void setId(String id) {
		mId = UUID.fromString(id);
	}

	public String getPassword() {
		if(!isTimeLock()) {
			return mPassword;
		}
		return mPassword.replaceAll(".", "*");
	}
	
	public String getPasswordForSave() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public Timelock getTimeLock() {
		return mTimelock;
	}
	
	public int getTimeLockOfOrdinal() {
		return getTimeLock().ordinal();
	}

	public void setTimeLock(Timelock timeLock) {
		mTimelock = timeLock;
		if(mStartTime == null || getUnlockRemainingTime() <= 0) {
			setStartTime(new Date()); // 不是真实时间，只是为了防止mTimelock变量重置
		}
	}
	
	public void setTimeLock(int ordinal) {
		setTimeLock(Timelock.getTimeLockByOrdinal(ordinal));
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getNotes() {
		return mNotes;
	}

	public void setNotes(String notes) {
		mNotes = notes;
	}

	public Date getStartTime() {
		return mStartTime;
	}
	
	public String getStartTimeFormat() {
		if(mStartTime != null) {
			SimpleDateFormat format = new SimpleDateFormat(TimeUtils.DATEFORMAT_PATTERN);
			return format.format(mStartTime);
		}
		return "";
	}

	public void setStartTime(Date startTime) {
		if(mTimelock != null && mTimelock != Timelock.NOT_LOCK && startTime != null) {
			mStartTime = startTime;
		}
	}
	
	public void setStartTime(String startTimeFormat) {
		if(!TextUtils.isEmpty(startTimeFormat)) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(TimeUtils.DATEFORMAT_PATTERN);
				setStartTime(format.parse(startTimeFormat));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
