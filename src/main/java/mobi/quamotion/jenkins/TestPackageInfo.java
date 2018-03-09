package mobi.quamotion.jenkins;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by BartSaintGermain on 2/27/2018.
 */
public class TestPackageInfo
{
    public String testPackageName;
    public String testPackageVersion;

    public String getTestPackageName()
    {
        return this.testPackageName;
    }

    public String getTestPackageVersion()
    {
        return this.testPackageVersion;
    }

    @DataBoundConstructor
    public TestPackageInfo(String testPackageName, String testPackageVersion)
    {
        this.testPackageName = testPackageName;
        this.testPackageVersion = testPackageVersion;
    }
}
