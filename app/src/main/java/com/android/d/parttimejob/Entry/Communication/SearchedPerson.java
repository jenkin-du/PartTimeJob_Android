package com.android.d.parttimejob.Entry.Communication;

/**联系人
 * Created by Administrator on 2016/8/29.
 */
public class SearchedPerson {

    private String pId;
    private String imageName;
    private String name;
    private String school;


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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "SearchedPerson{" +
                "pId='" + pId + '\'' +
                ", imageName='" + imageName + '\'' +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        return this.pId.equals(((SearchedPerson)o).pId);
    }
}
