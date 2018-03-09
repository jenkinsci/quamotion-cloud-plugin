package mobi.quamotion.jenkins;

import mobi.quamotion.cloud.models.TestJob;

/**
 * Created by BartSaintGermain on 3/1/2018.
 */
public interface TestJobFilter
{
    boolean include(TestJob testJob);
}
