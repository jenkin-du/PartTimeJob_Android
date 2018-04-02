package com.android.d.parttimejob.Entry.recruit;

import java.sql.Date;

public class Information {

	private String iId;
	private String description;
	private String salary;
	private String workAddress;
	private Date startWorkTime;
	private String workTime;
	private String workDays ;
	private String category;
	private int recruitNumber;
	private String contactName;
	private String contactPhone;
	private String workDetail;
	private String heightRequest;
	private String genderRequest;
	private String experienceRequest;

	public String getId() {
		return iId;
	}
	public void setId(String id) {
		this.iId = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public Date getStartWorkTime() {
		return startWorkTime;
	}
	public void setStartWorkTime(Date startWorkTime) {
		this.startWorkTime = startWorkTime;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getWorkDays() {
		return workDays;
	}
	public void setWorkDays(String workDays) {
		this.workDays = workDays;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getRecruitNumber() {
		return recruitNumber;
	}
	public void setRecruitNumber(int recruitNumber) {
		this.recruitNumber = recruitNumber;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getWorkDetail() {
		return workDetail;
	}
	public void setWorkDetail(String workDetail) {
		this.workDetail = workDetail;
	}
	public String getHeightRequest() {
		return heightRequest;
	}
	public void setHeightRequest(String heightRequest) {
		this.heightRequest = heightRequest;
	}
	public String getGenderRequest() {
		return genderRequest;
	}
	public void setGenderRequest(String genderRequest) {
		this.genderRequest = genderRequest;
	}
	public String getExperienceRequest() {
		return experienceRequest;
	}
	public void setExperienceRequest(String experienceRequest) {
		this.experienceRequest = experienceRequest;
	}
	@Override
	public String toString() {
		return "Information [id=" + iId + ", description=" + description
				+ ", salary=" + salary + ", WorkAddress=" + workAddress
				+ ", startWorkTime=" + startWorkTime + ", workTime=" + workTime
				+ ", workDays=" + workDays + ", category=" + category
				+ ", recruitNumber=" + recruitNumber + ", contactName="
				+ contactName + ", contactPhone=" + contactPhone
				+ ", workDetail=" + workDetail + ", heightRequest="
				+ heightRequest + ", genderRequest=" + genderRequest
				+ ", experienceRequest=" + experienceRequest + ", getId()="
				+ getId() + ", getDescription()=" + getDescription()
				+ ", getSalary()=" + getSalary() + ", getPlace()="
				+ getWorkAddress() + ", getStartWorkTime()="
				+ getStartWorkTime() + ", getWorkTime()=" + getWorkTime()
				+ ", getWorkDays()=" + getWorkDays() + ", getCategory()="
				+ getCategory() + ", getRecruitNumber()=" + getRecruitNumber()
				+ ", getContactName()=" + getContactName()
				+ ", getContactPhone()=" + getContactPhone()
				+ ", getWorkDetail()=" + getWorkDetail()
				+ ", getHeightRequest()=" + getHeightRequest()
				+ ", getGenderRequest()=" + getGenderRequest()
				+ ", getExperienceRequest()=" + getExperienceRequest()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public Information(String id, String description, String salary,
			String workAddress, Date startWorkTime, String workTime,
			String workDays, String category, int recruitNumber,
			String contactName, String contactPhone, String workDetail,
			String heightRequest, String genderRequest, String experienecRequest) {
		
		this.iId = id;
		this.description = description;
		this.salary = salary;
		this.workAddress = workAddress;
		this.startWorkTime = startWorkTime;
		this.workTime = workTime;
		this.workDays = workDays;
		this.category = category;
		this.recruitNumber = recruitNumber;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.workDetail = workDetail;
		this.heightRequest = heightRequest;
		this.genderRequest = genderRequest;
		this.experienceRequest = experienecRequest;
	}
	public Information() {
	}
	
	
	
}
