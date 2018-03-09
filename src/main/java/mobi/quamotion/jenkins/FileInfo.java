package mobi.quamotion.jenkins;

import hudson.FilePath;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by BartSaintGermain on 2/27/2018.
 */
public class FileInfo {
    public String path;

    public String getPath()
    {
        return this.path;
    }

    @DataBoundConstructor
    public FileInfo(String path)
    {
        this.path = path;
    }
}
