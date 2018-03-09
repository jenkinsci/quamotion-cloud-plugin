package mobi.quamotion.jenkins;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by BartSaintGermain on 2/27/2018.
 */
public class ApplicationInfo {
    public String appId;
    public String appVersion;
    public String appOperatingSystem;

    public String getAppId()
    {
        return this.appId;
    }

    public String getAppVersion()
    {
        return this.appVersion;
    }

    public String getAppOperatingSystem()
    {
        return this.appOperatingSystem;
    }

    @DataBoundConstructor
    public ApplicationInfo(String appId, String appVersion, String appOperatingSystem)
    {
        this.appId = appId;
        this.appVersion = appVersion;
        this.appOperatingSystem = appOperatingSystem;
    }
}
