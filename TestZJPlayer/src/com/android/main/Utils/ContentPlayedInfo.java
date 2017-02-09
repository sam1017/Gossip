package com.android.main.Utils;

/**
*
*@author  Create by liangchangwei   
*@data    2016年10月13日 --- 下午3:17:51
*
*/
import java.io.Serializable;

public class ContentPlayedInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ID = "_id";
	public static final String USERNAME = "username";
	public static final String PICPATH = "pic_path";
	public static final String MP3PATH = "mp3_path";
	public static final String STARTTIME = "start_time";
	public static final String DURATION = "duration";
	
	
	private String username = null;
	private String pic_path = null;
	private String mp3_path = null;
	private long start_time = 0;
	private long duration = 0;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the pic_path
	 */
	public String getPic_path() {
		return pic_path;
	}
	/**
	 * @param pic_path the pic_path to set
	 */
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	/**
	 * @return the mp3_path
	 */
	public String getMp3_path() {
		return mp3_path;
	}
	/**
	 * @param mp3_path the mp3_path to set
	 */
	public void setMp3_path(String mp3_path) {
		this.mp3_path = mp3_path;
	}
	/**
	 * @return the start_time
	 */
	public long getStart_time() {
		return start_time;
	}
	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	
}
