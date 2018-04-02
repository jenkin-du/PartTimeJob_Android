package com.android.d.parttimejob.Entry.recruit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单个招聘信息
 * Created by D on 2016/7/13.
 */
public class InfoDetail implements Parcelable{

    private String description;       //招聘简介
    private String workTime;          //工作时间段（每天）
    private String startWorkTime;     //开始工作时间
    private String workDays;          //工作天数
    private String workDetail;        //工作详情

    private String company;           //发布公司
    private String companyId;         //公司ID

    private String distance;          //距离
    private WorkAddress address;         //工作地点（区/县）

    private String salary;            //薪资
    private String category;             //工作类别
    private String updateTime;        //更新时间

    private int recruitNumber;        //招聘数量
    private int recruitedNumber;      //已招聘数量

    private String genderRequest;    //性别要求

    private String contactName;      //联系人姓名
    private String contactPhone;      //联系人电话

       protected InfoDetail(Parcel in) {
        description = in.readString();
        workTime = in.readString();
        startWorkTime = in.readString();
        workDays = in.readString();
        workDetail = in.readString();
        company = in.readString();
        companyId = in.readString();
        distance = in.readString();
        address = in.readParcelable(WorkAddress.class.getClassLoader());
        salary = in.readString();
        category = in.readString();
        updateTime = in.readString();
        recruitNumber = in.readInt();
        recruitedNumber = in.readInt();
        genderRequest = in.readString();
        contactName = in.readString();
        contactPhone = in.readString();
    }

    public static final Creator<InfoDetail> CREATOR = new Creator<InfoDetail>() {
        @Override
        public InfoDetail createFromParcel(Parcel in) {
            return new InfoDetail(in);
        }

        @Override
        public InfoDetail[] newArray(int size) {
            return new InfoDetail[size];
        }
    };

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }



    public InfoDetail() {

    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDescription() {
        return description;
    }

    public String getWorkTime() {
        return workTime;
    }

    public String getDistance() {
        return distance;
    }

    public WorkAddress getAddress() {
        return address;
    }

    public String getSalary() {
        return salary;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setAddress(WorkAddress address) {
        this.address = address;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(String startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }

    public String getWorkDetail() {
        return workDetail;
    }

    public void setWorkDetail(String workDetail) {
        this.workDetail = workDetail;
    }

    public int getRecruitNumber() {
        return recruitNumber;
    }

    public void setRecruitNumber(int recruitNumber) {
        this.recruitNumber = recruitNumber;
    }

    public int getRecruitedNumber() {
        return recruitedNumber;
    }

    public void setRecruitedNumber(int recruitedNumber) {
        this.recruitedNumber = recruitedNumber;
    }

    public String getGenderRequest() {
        return genderRequest;
    }

    public void setGenderRequest(String genderRequest) {
        this.genderRequest = genderRequest;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(workTime);
        dest.writeString(startWorkTime);
        dest.writeString(workDays);
        dest.writeString(workDetail);
        dest.writeString(company);
        dest.writeString(companyId);
        dest.writeString(distance);
        dest.writeParcelable(address, flags);
        dest.writeString(salary);
        dest.writeString(category);
        dest.writeString(updateTime);
        dest.writeInt(recruitNumber);
        dest.writeInt(recruitedNumber);
        dest.writeString(genderRequest);
        dest.writeString(contactName);
        dest.writeString(contactPhone);
    }

    @Override
    public String toString() {
        return "InfoDetail{" +
                "description='" + description + '\'' +
                ", workTime='" + workTime + '\'' +
                ", startWorkTime='" + startWorkTime + '\'' +
                ", workDays='" + workDays + '\'' +
                ", workDetail='" + workDetail + '\'' +
                ", company='" + company + '\'' +
                ", companyId='" + companyId + '\'' +
                ", distance='" + distance + '\'' +
                ", address=" + address +
                ", salary='" + salary + '\'' +
                ", category='" + category + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", recruitNumber=" + recruitNumber +
                ", recruitedNumber=" + recruitedNumber +
                ", genderRequest='" + genderRequest + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                '}';
    }
}
