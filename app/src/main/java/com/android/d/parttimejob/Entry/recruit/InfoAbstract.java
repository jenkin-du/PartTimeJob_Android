package com.android.d.parttimejob.Entry.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoAbstract implements Parcelable {

    private String iId;

    private String description;
    private String salary;
    private WorkAddress address;
    private String startWorkTime;
    private String updateTime;

    protected InfoAbstract(Parcel in) {
        iId = in.readString();
        description = in.readString();
        salary = in.readString();
        address = in.readParcelable(WorkAddress.class.getClassLoader());
        startWorkTime = in.readString();
        updateTime = in.readString();
        workDays = in.readInt();
        category = in.readString();
        distance = in.readInt();
    }

    public static final Creator<InfoAbstract> CREATOR = new Creator<InfoAbstract>() {
        @Override
        public InfoAbstract createFromParcel(Parcel in) {
            return new InfoAbstract(in);
        }

        @Override
        public InfoAbstract[] newArray(int size) {
            return new InfoAbstract[size];
        }
    };

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    private int workDays;
    private String category;
    private int distance;






    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
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

    public WorkAddress getAddress() {
        return address;
    }

    public void setAddress(WorkAddress address) {
        this.address = address;
    }

    public String getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(String startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public String toString() {
        return "InfoAbstract{" +
                "iId='" + iId + '\'' +
                ", description='" + description + '\'' +
                ", salary='" + salary + '\'' +
                ", address=" + address +
                ", startWorkTime='" + startWorkTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", workDays=" + workDays +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iId);
        dest.writeString(description);
        dest.writeString(salary);
        dest.writeParcelable(address, flags);
        dest.writeString(startWorkTime);
        dest.writeString(updateTime);
        dest.writeInt(workDays);
        dest.writeString(category);
        dest.writeInt(distance);
    }
}
