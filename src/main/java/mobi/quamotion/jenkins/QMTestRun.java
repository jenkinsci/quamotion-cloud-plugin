package mobi.quamotion.jenkins;

import hudson.model.AbstractBuild;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResult;
import mobi.quamotion.cloud.QMCloudClient;
import mobi.quamotion.cloud.models.Tenant;
import mobi.quamotion.cloud.models.TestJob;
import mobi.quamotion.cloud.models.TestRun;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jaxen.pantry.Test;
import org.kohsuke.stapler.StaplerProxy;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by BartSaintGermain on 2/28/2018.
 */
public class QMTestRun extends AbstractTestResultAction<QMTestRun> implements StaplerProxy{
    private static final int refreshInterval = 10000;

    private QMTestRunResult testRunResult;


    public QMTestRunResult getTestRunResult() {
        return testRunResult;
    }

    public void setTestRunResult(QMTestRunResult testRunResult) {
        this.testRunResult = testRunResult;
    }

    public QMTestRun( AbstractBuild<?, ?> owner, QMTestRunResult result) {
        super(owner);
        this.setTestRunResult(result);
    }

    public Object getTarget()
    {
        return getResult();
    }

    @Override
    public Object getResult() {
        return getTestRunResult();
    }

    @Override
    public int getSkipCount() {
        return this.getTestRunResult().getSkipCount();
    }

    @Override
    public int getFailCount() {
        return this.getTestRunResult().getFailCount();
    }

    @Override
    public int getTotalCount()
    {
        return this.getTestRunResult().getTestJobResults().size();
    }

    public List<? extends TestResult> getFailedTests() {
        return (List<? extends TestResult>) this.getTestRunResult().getFailedTests();
    }

    private void updateStatus(QMCloudClient cloudClient, Tenant tenant) throws IOException {
        this.getTestRunResult().setTestRun(cloudClient.getTestRun(tenant, testRunResult.getTestRun().getTestRunId().toString()));

        List<TestJob> jobs = cloudClient.getTestJobs(tenant, testRunResult.getTestRun());
        testRunResult.initTestJobResults(jobs);

        for(QMTestJobResult testJobResult : testRunResult.getTestJobResults())
        {
            testJobResult.setConsoleLog(cloudClient.getJobLog(tenant, testJobResult.getTestJob()));
        }
    }

    public boolean isCompleted(QMCloudClient cloudClient, Tenant tenant, PrintStream printStream) throws IOException {
        try
        {
            printStream.print(".");
            this.updateStatus(cloudClient, tenant);
        }
        catch (Exception ex)
        {
            printStream.println(ex.toString());
        }

        return this.getTestRunResult().isCompleted();
    }

    public void waitForCompletion(QMCloudClient cloudClient, Tenant tenant, PrintStream printStream) throws IOException, InterruptedException {
        printStream.print("Performing tests: ");

        while (!isCompleted(cloudClient, tenant, printStream))
        {
            Thread.sleep(refreshInterval);
        }

        printStream.println();
    }
}
