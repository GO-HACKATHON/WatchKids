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

}