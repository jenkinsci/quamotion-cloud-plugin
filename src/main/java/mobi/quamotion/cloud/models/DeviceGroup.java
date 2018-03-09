package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by BartSaintGermain on 2/23/2018.
 */
public class DeviceGroup
{
    @SerializedName("deviceGroupId")
    public UUID deviceGroupId;

    @SerializedName("name")
    public String Name;

    @SerializedName("displayName")
    public String DisplayName;

    @SerializedName("devices")
    public List<DeviceSelection> Devices;

    @SerializedName("variables")
    public HashMap<String, String> Variables;
}
