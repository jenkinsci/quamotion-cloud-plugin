package mobi.quamotion.jenkins;

import hudson.tasks.test.TestResult;
import mobi.quamotion.cloud.QMCloudClient;
import mobi.quamotion.cloud.models.Tenant;
import mobi.quamotion.cloud.models.TestJob;
import mobi.quamotion.cloud.models.TestRun;
import org.jaxen.pantry.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by BartSaintGermain on 2/28/2018.
 */
public class QMTestRunResult extends TestResult{

    private TestRun testRun;

    private List<QMTestJobResult> testJobResults = new ArrayList<QMTestJobResult>();

    public QMTestRunResult(TestRun testRun)
    {
        this.setTestRun(testRun);
    }

    protected void initTestJobResults(List<TestJob> testJobs)
    {
        for(TestJob testJob : testJobs)
        {
            QMTestJobResult testJobResult = this.getTestJobResult(testJob.getId());
            if(testJobResult != null)
            {
                testJobResult.setTestJob(testJob);
            }
            else
            {
                this.getTestJobResults().add(new QMTestJobResult(testJob,this));
            }
        }
    }

    private QMTestJobResult getTestJobResult(int testJobId)
    {
        QMTestJobResult result = null;
        for (QMTestJobResult testJobResult : this.getTestJobResults())
        {
            if(testJobResult.getTestJob().getId() == testJobId)
            {
                result = testJobResult;
            }
        }

        return result;
    }

    public List<QMTestJobResult> getTestJobResults() {
        return testJobResults;
    }

    public void setTestJobResults(List<QMTestJobResult> testJobResults) {
        this.testJobResults = testJobResults;
    }

    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    public float getDuration() {
        float duration = 0;
        for (QMTestJobResult testJob : this.getTestJobResults())
        {
            duration += testJob.getDuration();
        }
        return duration;
    }

    public int getPassCount() {
        return  this.getPassedTests().size();
    }

    public int getFailCount() {
        return this.getFailedTests().size();
    }

    public int getSkipCount() {
        return this.getSkippedTests().size();
    }

    @Override
    public int getTotalCount() {
        return this.getTestJobResults().size();
    }

    public Collection<? extends TestResult> getFailedTests() {
        return this.filterJobs(new QMTestJobResult.FailedTestJobFilter());
    }

    public Collection<? extends TestResult> getPassedTests() {
        return this.filterJobs(new QMTestJobResult.PassedTestJobFilter());
    }

    public Collection<? extends TestResult> getSkippedTests() {
        return this.filterJobs(new QMTestJobResult.SkippedTestJobFilter());
    }

    public Collection<? extends TestResult> getRunningTests() {
        return this.filterJobs(new QMTestJobResult.RunningTestJobFilter());
    }

    public Collection<? extends TestResult> getCanceledTests() {
        return this.filterJobs(new QMTestJobResult.CanceledTestJobFilter());
    }

    @Override
    public String getName() {
        return this.getTestRun().getTestRunId().toString();
    }

    @Override
    public TestResult getParent() {
        return null;
    }

    @Override
    public String getDisplayName()
    {
        return this.getTestRun().getTestRunId().toString();
    }

    public boolean isCompleted() {
        return this.getRunningTests().size() == 0;
    }

    public TestResult findCorrespondingResult(String id) {
        return null;
    }

    public List<QMTestJobResult> filterJobs(TestJobFilter includeTestJob)
    {
        ArrayList<QMTestJobResult> filteredJobs = new ArrayList<QMTestJobResult>();
        for(QMTestJobResult testJobResult : this.getTestJobResults())
        {
            if(includeTestJob.include(testJobResult.getTestJob()))
            {
                filteredJobs.add(testJobResult);
            }
        }

        return filteredJobs;
    }

    public Collection<? extends TestResult>  toTestResults(List<TestJob> testJobs){
        ArrayList<QMTestJobResult> failedTests = new ArrayList<QMTestJobResult>();

        for(TestJob testJob : testJobs)
        {
            failedTests.add(new QMTestJobResult(testJob, this));
        }

        return failedTests;
    }
}
