package com.android.d.parttimejob.Entry.Communication;

/**
 * 联系人
 * Created by Administrator on 2016/8/29.
 */
public class Message {

    private String id;
    private String name;
    private String lastMessage;//最后的聊天记录
    private String time;//最后的聊天时间



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
