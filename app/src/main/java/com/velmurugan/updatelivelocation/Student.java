package com.velmurugan.updatelivelocation;

public class Student {
    String sBranch, sYear, sSemister, sSection,sBusNo, sRollno, sName, sMobile, sEmail, sPassword,gender;

    public Student(String sBranch, String sYear, String sSemister, String sSection, String sBusNo,String sRollno, String sName, String sMobile, String sEmail, String sPassword,String gender) {
        this.sBranch = sBranch;
        this.sYear = sYear;
        this.sSemister = sSemister;
        this.sSection = sSection;
        this.sBusNo = sBusNo;
        this.sRollno = sRollno;
        this.sName = sName;
        this.sMobile = sMobile;
        this.sEmail = sEmail;
        this.sPassword = sPassword;
        this.gender = gender;
    }

    public Student() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getsBranch() {
        return sBranch;
    }

    public void setsBranch(String sBranch) {
        this.sBranch = sBranch;
    }

    public String getsYear() {
        return sYear;
    }

    public void setsYear(String sYear) {
        this.sYear = sYear;
    }

    public String getsSemister() {
        return sSemister;
    }

    public void setsSemister(String sSemister) {
        this.sSemister = sSemister;
    }

    public String getsSection() {
        return sSection;
    }

    public void setsSection(String sSection) {
        this.sSection = sSection;
    }

    public String getsBusNo() {return sBusNo;}

    public String getsRollno() {
        return sRollno;
    }

    public void setsRollno(String sRollno) {
        this.sRollno = sRollno;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsMobile() {
        return sMobile;
    }

    public void setsMobile(String sMobile) {
        this.sMobile = sMobile;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }
}
