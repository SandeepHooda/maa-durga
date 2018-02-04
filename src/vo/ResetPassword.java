package vo;

public class ResetPassword {
	private String uuid;
	private String email;
	private long generateTime;
	private int validTillMinutes = 10;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}
	public int getValidTillMinutes() {
		return validTillMinutes;
	}
	public void setValidTillMinutes(int validTillMinutes) {
		this.validTillMinutes = validTillMinutes;
	}

}
