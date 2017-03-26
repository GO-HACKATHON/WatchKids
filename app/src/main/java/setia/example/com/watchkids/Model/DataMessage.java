package setia.example.com.watchkids.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by My Computer on 3/26/2017.
 */

public class DataMessage {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_kids")
    @Expose
    private String idKids;
    @SerializedName("kids_firstname")
    @Expose
    private String kidsFirstname;
    @SerializedName("id_profile")
    @Expose
    private String idProfile;
    @SerializedName("profile_firstname")
    @Expose
    private String profileFirstname;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKids() {
        return idKids;
    }

    public void setIdKids(String idKids) {
        this.idKids = idKids;
    }

    public String getKidsFirstname() {
        return kidsFirstname;
    }

    public void setKidsFirstname(String kidsFirstname) {
        this.kidsFirstname = kidsFirstname;
    }

    public String getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(String idProfile) {
        this.idProfile = idProfile;
    }

    public String getProfileFirstname() {
        return profileFirstname;
    }

    public void setProfileFirstname(String profileFirstname) {
        this.profileFirstname = profileFirstname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

}




