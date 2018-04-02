package com.android.d.parttimejob.Entry.recruit;

public class Recruit {

	private String infoId;
	private String PluralistId;
	private String applyReason;
	private String applyStatus;
	
	public String getInfoId() {
		return infoId;
	}
	public String getPluralistId() {
		return PluralistId;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public void setPluralistId(String pluralistId) {
		PluralistId = pluralistId;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	
	@Override
	public String toString() {
		return "Recruit [infoId=" + infoId + ", PluralistId=" + PluralistId + ", applyReason=" + applyReason
				+ ", applyStatus=" + applyStatus + "]";
	}
	
	
}
