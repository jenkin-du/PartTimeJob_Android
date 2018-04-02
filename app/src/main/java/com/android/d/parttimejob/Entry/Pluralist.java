package com.android.d.parttimejob.Entry;

public class Pluralist {

	private String id;
	private String name;
	private String phone;
	private String password;
	private String gender;
	private float salary;
	private String imageName;//头像的名字
	private String email;
	private String feature;
	private String school;
	private String experience;

	private int age;
	private int height;
	private String educationBackground;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getEducationBackground() {
		return educationBackground;
	}

	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}

	@Override
	public String toString() {
		return "Pluralist{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", phone='" + phone + '\'' +
				", password='" + password + '\'' +
				", gender='" + gender + '\'' +
				", salary=" + salary +
				", imageName='" + imageName + '\'' +
				", email='" + email + '\'' +
				", feature='" + feature + '\'' +
				", school='" + school + '\'' +
				", experience='" + experience + '\'' +
				", age=" + age +
				", height=" + height +
				", educationBackground='" + educationBackground + '\'' +
				'}';
	}
}
