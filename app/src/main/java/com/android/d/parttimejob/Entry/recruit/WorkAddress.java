package com.android.d.parttimejob.Entry.recruit;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkAddress implements Parcelable{
	
	private String aId;
	private String province;
	private String city;
	private String district;
	private String detailAddr;

	private String latitude;
	private String longitude;


	protected WorkAddress(Parcel in) {
		aId = in.readString();
		province = in.readString();
		city = in.readString();
		district = in.readString();
		detailAddr = in.readString();
		latitude = in.readString();
		longitude = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(aId);
		dest.writeString(province);
		dest.writeString(city);
		dest.writeString(district);
		dest.writeString(detailAddr);
		dest.writeString(latitude);
		dest.writeString(longitude);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<WorkAddress> CREATOR = new Creator<WorkAddress>() {
		@Override
		public WorkAddress createFromParcel(Parcel in) {
			return new WorkAddress(in);
		}

		@Override
		public WorkAddress[] newArray(int size) {
			return new WorkAddress[size];
		}
	};

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public static Creator<WorkAddress> getCREATOR() {
		return CREATOR;
	}

	@Override
	public String toString() {
		return "WorkAddress{" +
				"aId='" + aId + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", district='" + district + '\'' +
				", detailAddr='" + detailAddr + '\'' +
				", latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				'}';
	}
}
