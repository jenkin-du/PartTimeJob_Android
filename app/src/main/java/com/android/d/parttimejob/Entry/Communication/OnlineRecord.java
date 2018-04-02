package com.android.d.parttimejob.Entry.Communication;

import android.os.Parcel;
import android.os.Parcelable;

public class OnlineRecord implements Parcelable{

	private String id;
	private String ip;

	public OnlineRecord() {
	}

	/**
	 * @param id
	 * @param ip
	 */
	public OnlineRecord(String id, String ip) {
		this.id = id;
		this.ip = ip;
	}

	protected OnlineRecord(Parcel in) {
		id = in.readString();
		ip = in.readString();
	}

	public static final Creator<OnlineRecord> CREATOR = new Creator<OnlineRecord>() {
		@Override
		public OnlineRecord createFromParcel(Parcel in) {
			return new OnlineRecord(in);
		}

		@Override
		public OnlineRecord[] newArray(int size) {
			return new OnlineRecord[size];
		}
	};

	public String getId() {
		return id;
	}
	public String getIp() {
		return ip;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "OnlineRecord [id=" + id + ", ip=" + ip + "]";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(ip);
	}
}
