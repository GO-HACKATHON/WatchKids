package setia.example.com.watchkids.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by My Computer on 3/26/2017.
 */

public class DataLimit {

    @SerializedName("id_limit")
    @Expose
    private String idLimit;
    @SerializedName("kids_id")
    @Expose
    private String kidsId;
    @SerializedName("kids_firstname")
    @Expose
    private String kidsFirstname;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("profile_firstname")
    @Expose
    private String profileFirstname;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("radius")
    @Expose
    private String radius;
    @SerializedName("status")
    @Expose
    private String status;

    public String getIdLimit() {
        return idLimit;
    }

    public void setIdLimit(String idLimit) {
        this.idLimit = idLimit;
    }

    public String getKidsId() {
        return kidsId;
    }

    public void setKidsId(String kidsId) {
        this.kidsId = kidsId;
    }

    public String getKidsFirstname() {
        return kidsFirstname;
    }

    public void setKidsFirstname(String kidsFirstname) {
        this.kidsFirstname = kidsFirstname;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getProfileFirstname() {
        return profileFirstname;
    }

    public void setProfileFirstname(String profileFirstname) {
        this.profileFirstname = profileFirstname;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
