package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by BartSaintGermain on 2/23/2018.
 */
public class CreateTestRunRequest
{
    @SerializedName("app")
    public Application app;

    @SerializedName("testPackage")
    public TestPackage testPackage;

    @SerializedName("deviceGroupId")
    public String deviceGroupId;

    @SerializedName("schedule")
    public String schedule;

    @SerializedName("testScriptParameters")
    public String testScriptParameters;

    @SerializedName("testScriptEnvironmentVariables")
    public HashMap<String, String> testScriptEnvironmentVariables;
}
