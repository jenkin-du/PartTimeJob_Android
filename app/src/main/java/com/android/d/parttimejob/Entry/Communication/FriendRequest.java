package com.android.d.parttimejob.Entry.Communication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 好友申请
 * Created by Administrator on 2016/8/30.
 */
public class FriendRequest implements Parcelable {


    private String myId;
    private String friendId;

    private String friendName;
    private String myName;

    private String requestReason;

    private String status;

    private String Time;

    public FriendRequest() {

    }


    protected FriendRequest(Parcel in) {
        myId = in.readString();
        friendId = in.readString();
        friendName = in.readString();
        myName = in.readString();
        requestReason = in.readString();
        status = in.readString();
        Time = in.readString();
    }

    public static final Creator<FriendRequest> CREATOR = new Creator<FriendRequest>() {
        @Override
        public FriendRequest createFromParcel(Parcel in) {
            return new FriendRequest(in);
        }

        @Override
        public FriendRequest[] newArray(int size) {
            return new FriendRequest[size];
        }
    };

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "myId='" + myId + '\'' +
                ", friendId='" + friendId + '\'' +
                ", friendName='" + friendName + '\'' +
                ", myName='" + myName + '\'' +
                ", requestReason='" + requestReason + '\'' +
                ", status='" + status + '\'' +
                ", Time='" + Time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(myId);
        dest.writeString(friendId);
        dest.writeString(friendName);
        dest.writeString(myName);
        dest.writeString(requestReason);
        dest.writeString(status);
        dest.writeString(Time);
    }


}
