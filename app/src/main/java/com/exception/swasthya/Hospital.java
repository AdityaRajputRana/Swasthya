package com.exception.swasthya;

public class Hospital {

    private String mHospitalName;
    private String mHospitalLongitude;
    private String mHospitalLatitude;
    private String mGeoHash;

    private int mTotalNoOfCovidBeds;
    private int mVacantCovidBeds;
    private int mTotalICUBeds;
    private int mVacantICUBeds;

    private int mTotalNormalBeds;
    private int mVacantNormalBeds;

    private String mEmailId;
    private String UId;

    public Hospital(String mHospitalName, String mHospitalLongitude, String mHospitalLatitude, String mGeoHash, int mTotalNoOfCovidBeds, int mVacantCovidBeds, int mTotalICUBeds, int mVacantICUBeds, int mTotalNormalBeds, int mVacantNormalBeds, String mEmailId, String UId) {
        this.mHospitalName = mHospitalName;
        this.mHospitalLongitude = mHospitalLongitude;
        this.mHospitalLatitude = mHospitalLatitude;
        this.mGeoHash = mGeoHash;
        this.mTotalNoOfCovidBeds = mTotalNoOfCovidBeds;
        this.mVacantCovidBeds = mVacantCovidBeds;
        this.mTotalICUBeds = mTotalICUBeds;
        this.mVacantICUBeds = mVacantICUBeds;
        this.mTotalNormalBeds = mTotalNormalBeds;
        this.mVacantNormalBeds = mVacantNormalBeds;
        this.mEmailId = mEmailId;
        this.UId = UId;
    }

    public String getmHospitalName() {
        return mHospitalName;
    }

    public void setmHospitalName(String mHospitalName) {
        this.mHospitalName = mHospitalName;
    }

    public String getmHospitalLongitude() {
        return mHospitalLongitude;
    }

    public void setmHospitalLongitude(String mHospitalLongitude) {
        this.mHospitalLongitude = mHospitalLongitude;
    }

    public String getmHospitalLatitude() {
        return mHospitalLatitude;
    }

    public void setmHospitalLatitude(String mHospitalLatitude) {
        this.mHospitalLatitude = mHospitalLatitude;
    }

    public String getmGeoHash() {
        return mGeoHash;
    }

    public void setmGeoHash(String mGeoHash) {
        this.mGeoHash = mGeoHash;
    }

    public int getmTotalNoOfCovidBeds() {
        return mTotalNoOfCovidBeds;
    }

    public void setmTotalNoOfCovidBeds(int mTotalNoOfCovidBeds) {
        this.mTotalNoOfCovidBeds = mTotalNoOfCovidBeds;
    }

    public int getmVacantCovidBeds() {
        return mVacantCovidBeds;
    }

    public void setmVacantCovidBeds(int mVacantCovidBeds) {
        this.mVacantCovidBeds = mVacantCovidBeds;
    }

    public int getmTotalICUBeds() {
        return mTotalICUBeds;
    }

    public void setmTotalICUBeds(int mTotalICUBeds) {
        this.mTotalICUBeds = mTotalICUBeds;
    }

    public int getmVacantICUBeds() {
        return mVacantICUBeds;
    }

    public void setmVacantICUBeds(int mVacantICUBeds) {
        this.mVacantICUBeds = mVacantICUBeds;
    }

    public int getmTotalNormalBeds() {
        return mTotalNormalBeds;
    }

    public void setmTotalNormalBeds(int mTotalNormalBeds) {
        this.mTotalNormalBeds = mTotalNormalBeds;
    }

    public int getmVacantNormalBeds() {
        return mVacantNormalBeds;
    }

    public void setmVacantNormalBeds(int mVacantNormalBeds) {
        this.mVacantNormalBeds = mVacantNormalBeds;
    }

    public String getmEmailId() {
        return mEmailId;
    }

    public void setmEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }
}
