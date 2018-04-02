package com.android.d.parttimejob.Util;

import com.android.d.parttimejob.Entry.Pluralist;

import java.util.ArrayList;

public class ReturnSet {

	private String status;
	private Pluralist pluralist;
	private ArrayList<RecruitStatus> recruitStatus;

	public ArrayList<RecruitStatus> getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(ArrayList<RecruitStatus> recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	public String getStatus() {
		return status;
	}
	public Pluralist getPluralist() {
		return pluralist;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setPluralist(Pluralist pluralist) {
		this.pluralist = pluralist;
	}
	
	
	
	
	
	
	
}
