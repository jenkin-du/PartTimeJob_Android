package com.android.d.parttimejob.Entry.Communication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 聊天消息
 * Created by Administrator on 2016/8/24.
 */
public class ChatRecord implements Parcelable {

    private String myId;
    private String friendId;
    private String message;

    private String time;


    public ChatRecord() {
    }


    protected ChatRecord(Parcel in) {
        myId = in.readString();
        friendId = in.readString();
        message = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(myId);
        dest.writeString(friendId);
        dest.writeString(message);
        dest.writeString(time);
    }

    public static final Creator<ChatRecord> CREATOR = new Creator<ChatRecord>() {
        @Override
        public ChatRecord createFromParcel(Parcel in) {
            return new ChatRecord(in);
        }

        @Override
        public ChatRecord[] newArray(int size) {
            return new ChatRecord[size];
        }
    };

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }


}
