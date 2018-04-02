package com.android.d.parttimejob.Entry.recruit;

public class Company {
	
	private String cId ;
	private String name;
	private String description;
	private String address;
	private String phone;
	private String legalPerson;
	private String password;
	private float satisfaction;
	
	public String getId() {
		return cId;
	}
	public void setId(String id) {
		this.cId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public float getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(float satisfaction) {
		this.satisfaction = satisfaction;
	}

	@Override
	public String toString() {
		return "Company{" +
				"cId='" + cId + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", legalPerson='" + legalPerson + '\'' +
				", password='" + password + '\'' +
				", satisfaction=" + satisfaction +
				'}';
	}

	public Company(String id, String name, String description, String address,
				   String phone, String leagalPerson, String password,
				   float satisfaction) {
		this.cId = id;
		this.name = name;
		this.description = description;
		this.address = address;
		this.phone = phone;
		this.legalPerson = leagalPerson;
		this.password = password;
		this.satisfaction = satisfaction;
	}
	public Company() {
		
	}
	
	
	
}
