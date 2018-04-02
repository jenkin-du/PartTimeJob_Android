package com.android.d.parttimejob.Entry.Communication;

/**
 * 联系人
 * Created by Administrator on 2016/8/29.
 */
public class Contact {

    private String id;
    private String name;
    private String imageName;
    private boolean isOnline=false;
    private boolean isChatting = false;

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean chatting) {
        isChatting = chatting;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", imageName='" + imageName + '\'' +
                ", name='" + name + '\'' +
                ", isOnline=" + isOnline +
                ", isChatting=" + isChatting +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (o==null){
            return false;
        }else if(!(o instanceof Contact)){
            return false;
        }else if (((Contact) o).getId().equals(id)&&((Contact) o).getName().equals(name)){
            return true;
        }

        return false;
    }
}
