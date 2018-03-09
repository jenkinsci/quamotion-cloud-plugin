package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BartSaintGermain on 2/22/2018.
 */
public class LoginResponse
{
    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expires_in")
    public int expiresIn;

    @SerializedName("ext_expires_in")
    public int extExpiresIn;

    @SerializedName("expires_on")
    public int expiresOn;

    @SerializedName("not_before")
    public int notBefore;

    @SerializedName("resource")
    public String resource;

    @SerializedName("access_token")
    public String accessToken;
}
