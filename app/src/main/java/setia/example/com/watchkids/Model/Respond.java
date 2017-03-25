package setia.example.com.watchkids.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by My Computer on 3/25/2017.
 */

public class Respond {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("isExist")
    @Expose
    private Boolean isExist;
    @SerializedName("profile")
    @Expose
    private List<Profile> profile = null;
    @SerializedName("data_kids")
    @Expose
    private List<Kids> dataKids = null;
    @SerializedName("data_limit")
    @Expose
    private List<DataLimit> dataLimit = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getIsExist() {
        return isExist;
    }

    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }

    public List<Profile> getProfile() {
        return profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public List<Kids> getDataKids() {
        return dataKids;
    }

    public void setDataKids(List<Kids> dataKids) {
        this.dataKids = dataKids;
    }

    public List<DataLimit> getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(List<DataLimit> dataLimit) {
        this.dataLimit = dataLimit;
    }
}