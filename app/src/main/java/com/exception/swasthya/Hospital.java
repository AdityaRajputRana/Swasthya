package com.exception.swasthya;

public class Hospital {

    private String mHospitalName;
    private float mHospitalLongitude;
    private float mHospitalLatitude;
    private String mGeoHash;
    private int mTotalNoOfBeds;
    private int mNumberOfBedsVacant;
    private String mEmailId;
    private String mRegistrarName;

    public Hospital(String mHospitalName, float mHospitalLatitude, float mHospitalLongitude, String mGeoHash, int mTotalNoOfBeds, int mNumberOfBedsVacant, String mEmailId,String mRegistrarName){
        this.mHospitalName = mHospitalName;
        this.mHospitalLatitude = mHospitalLatitude;
        this.mHospitalLongitude = mHospitalLongitude;
        this.mGeoHash = mGeoHash;
        this.mTotalNoOfBeds = mTotalNoOfBeds;
        this.mNumberOfBedsVacant = mNumberOfBedsVacant;
        this.mEmailId = mEmailId;
        this.mRegistrarName = mRegistrarName;
    }

    public String getmHospitalName() {
        return mHospitalName;
    }

    public void setmHospitalName(String mHospitalName) {
        this.mHospitalName = mHospitalName;
    }

    public float getmHospitalLongitude() {
        return mHospitalLongitude;
    }

    public void setmHospitalLongitude(float mHospitalLongitude) {
        this.mHospitalLongitude = mHospitalLongitude;
    }

    public float getmHospitalLatitude() {
        return mHospitalLatitude;
    }

    public void setmHospitalLatitude(float mHospitalLatitude) {
        this.mHospitalLatitude = mHospitalLatitude;
    }

    public String getmGeoHash() {
        return mGeoHash;
    }

    public void setmGeoHash(String mGeoHash) {
        this.mGeoHash = mGeoHash;
    }

    public int getmTotalNoOfBeds() {
        return mTotalNoOfBeds;
    }

    public void setmTotalNoOfBeds(int mTotalNoOfBeds) {
        this.mTotalNoOfBeds = mTotalNoOfBeds;
    }

    public int getmNumberOfBedsVacant() {
        return mNumberOfBedsVacant;
    }

    public void setmNumberOfBedsVacant(int mNumberOfBedsVacant) {
        this.mNumberOfBedsVacant = mNumberOfBedsVacant;
    }

    public String getmEmailId() {
        return mEmailId;
    }

    public void setmEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getmRegistrarName() {
        return mRegistrarName;
    }

    public void setmRegistrarName(String mRegistrarName) {
        this.mRegistrarName = mRegistrarName;
    }
}
