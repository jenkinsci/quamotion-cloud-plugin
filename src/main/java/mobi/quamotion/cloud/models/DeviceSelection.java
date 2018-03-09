package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BartSaintGermain on 2/23/2018.
 */
public class DeviceSelection
{
    @SerializedName("deviceSelectionId")
    public String DeviceSelectionId;

    @SerializedName("displayName")
    public String DisplayName;

    @SerializedName("tags")
    public List<String> Tags;

    @SerializedName("variables")
    public HashMap<String, String> Variables;
}
