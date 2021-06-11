package com.example.authsignin;

public class dataHolder {
    String name, course, pimage;

    public dataHolder(String name, String course, String pimage) {
        this.name = name;
        this.course = course;
        this.pimage = pimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
