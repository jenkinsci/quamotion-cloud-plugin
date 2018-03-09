package mobi.quamotion.jenkins;

import hudson.tasks.test.TestResult;
import mobi.quamotion.cloud.QMCloudClient;
import mobi.quamotion.cloud.models.Tenant;
import mobi.quamotion.cloud.models.TestJob;
import mobi.quamotion.cloud.models.TestRun;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by BartSaintGermain on 2/28/2018.
 */
public class QMTestJobResult  extends TestResult {

    private TestJob testJob;
    private String consoleLog;
    private QMTestRunResult testRunResult;


    public String getConsoleLog() {
        return consoleLog;
    }

    public void setConsoleLog(String consoleLog) {
        this.consoleLog = consoleLog;
    }

    public TestJob getTestJob() {
        return testJob;
    }

    public void setTestJob(TestJob testJob) {
        this.testJob = testJob;
    }

    public QMTestRunResult getTestRunResult() {
        return testRunResult;
    }

    public void setTestRunResult(QMTestRunResult testRunResult) {
        this.testRunResult = testRunResult;
    }

    public QMTestJobResult(TestJob testJob, QMTestRunResult testRunResult)
    {
        this.setTestJob(testJob);
        this.setTestRunResult(testRunResult);
    }

    public String getTitle() {
        return this.getTestJob().getName();
    }

    public float getDuration() {
        return new Float(this.getTestJob().getDuration());
    }

    @Override
    public String getName() {
        return this.getTestJob().getId().toString();
    }

    @Override
    public TestResult getParent() {
        return getTestRunResult();
    }

    @Override
    public String getDisplayName()
    {
        return getTestJob().getName();
    }

    @Override
    public TestResult findCorrespondingResult(String id) {
        return null;
    }

    public static class RunningTestJobFilter implements TestJobFilter {
        public boolean include(TestJob testJob) {
            return testJob.getStatus().equals("running") || testJob.getStatus().equals("pending");
        }
    }

    public static class CanceledTestJobFilter implements TestJobFilter {
        public boolean include(TestJob testJob) {
            return testJob.getStatus().equals("canceled");
        }
    }

    public static class FailedTestJobFilter implements TestJobFilter {
        public boolean include(TestJob testJob) {
            return testJob.getStatus().equals("failed");
        }
    }

    public static class SkippedTestJobFilter implements TestJobFilter {
        public boolean include(TestJob testJob) {
            return testJob.getStatus().equals("skipped") || testJob.getStatus().equals("canceled");
        }
    }

    public static class PassedTestJobFilter implements TestJobFilter {
        public boolean include(TestJob testJob) {
            return testJob.getStatus().equals("passed");
        }
    }
}
