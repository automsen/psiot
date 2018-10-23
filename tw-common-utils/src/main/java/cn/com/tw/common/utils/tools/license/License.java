package cn.com.tw.common.utils.tools.license;

public class License {
	
	private boolean isLicExpire;
	
	private int expireDay;
	
	public License(){
		
	}
	
	public License(boolean isLicExpire, int expireDay){
		this.isLicExpire = isLicExpire;
		this.expireDay = expireDay;
	}

	public boolean isLicExpire() {
		return isLicExpire;
	}

	public void setLicExpire(boolean isLicExpire) {
		this.isLicExpire = isLicExpire;
	}

	public int getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(int expireDay) {
		this.expireDay = expireDay;
	}

	@Override
	public String toString() {
		return "License [isLicExpire=" + isLicExpire + ", expireDay="
				+ expireDay + "]";
	}
	
	

}
