package com.enzoconserve.Model;

public class ModelEvent {
    String pId,pTitle,pDesc,pImage,pTime,uid,uEmail,uDp,uName,pLoc,pTime1,pDate,pDist,pPhno;

    public ModelEvent() {
    }

    public ModelEvent(String pId, String pTitle, String pDesc, String pImage, String pTime, String uid, String uEmail, String uDp, String uName, String pLoc, String pTime1, String pDate, String pDist, String pPhno) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pDesc = pDesc;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
        this.pLoc = pLoc;
        this.pTime1 = pTime1;
        this.pDate = pDate;
        this.pDist = pDist;
        this.pPhno = pPhno;

    }
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDesc() {
        return pDesc;
    }

    public void setpDesc(String pDesc) {
        this.pDesc = pDesc;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getpLoc() {
        return pLoc;
    }

    public void setpLoc(String pLoc) {
        this.pLoc = pLoc;
    }

    public String getpTime1() {
        return pTime1;
    }

    public void setpTime1(String pTime1) {
        this.pTime1 = pTime1;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getpDist() {
        return pDist;
    }

    public void setpDist(String pDist) {
        this.pDist = pDist;
    }

    public String getpPhno() {
        return pPhno;
    }

    public void setpPhno(String pPhno) {
        this.pPhno = pPhno;
    }
}
