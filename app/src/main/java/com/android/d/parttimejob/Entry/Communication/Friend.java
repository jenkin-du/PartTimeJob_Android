package com.android.d.parttimejob.Entry.Communication;

import android.os.Parcel;
import android.os.Parcelable;

/**朋友资料
 * Created by Administrator on 2016/8/30.
 */
public class Friend implements Parcelable{

    private String id;
    private String imageName;
    private String name;
    private String age;
    private String gender;
    private String height;
    private String education;
    private String school;

    private String email;
    private String phone;

    private boolean isOnline;


    public Friend() {
    }


    protected Friend(Parcel in) {
        id = in.readString();
        imageName = in.readString();
        name = in.readString();
        age = in.readString();
        gender = in.readString();
        height = in.readString();
        education = in.readString();
        school = in.readString();
        email = in.readString();
        phone = in.readString();
        isOnline = in.readByte() != 0;
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageName);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(height);
        dest.writeString(education);
        dest.writeString(school);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeByte((byte) (isOnline ? 1 : 0));
    }


    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", imageName='" + imageName + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", height='" + height + '\'' +
                ", education='" + education + '\'' +
                ", school='" + school + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isOnline=" + isOnline +
                '}';
    }
}
