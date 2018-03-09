//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package mobi.quamotion.jenkins;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import mobi.quamotion.cloud.QMCloudClient;
import mobi.quamotion.cloud.models.*;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class QuamotionCloudTest extends Recorder {
    public FileInfo publishNewTestPackage;
    public TestPackageInfo useTestPackage;
    public FileInfo publishNewApp;
    public ApplicationInfo useApp;
    public String deviceGroupId;

    @DataBoundConstructor
    public QuamotionCloudTest(ApplicationInfo useApp, FileInfo publishNewApp, TestPackageInfo useTestPackage, FileInfo publishNewTestPackage, String deviceGroupId) {
        this.useApp = useApp;
        this.useTestPackage = useTestPackage;
        this.publishNewTestPackage = publishNewTestPackage;
        this.publishNewApp = publishNewApp;
        this.deviceGroupId = deviceGroupId;
    }

    public ApplicationInfo getUseApp()
    {
        return this.useApp;
    }

    public FileInfo getPublishNewTestPackage()
    {
        return this.publishNewTestPackage;
    }

    public TestPackageInfo getUseTestPackage()
    {
        return this.useTestPackage;
    }

    public FileInfo getPublishNewApp()
    {
        return this.publishNewApp;
    }

    public String getDeviceGroupId()
    {
        return this.deviceGroupId;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        try {
            listener.getLogger().println("Starting test run:");

            listener.getLogger().println("    device group id:" + this.deviceGroupId);
            listener.getLogger().println();
            QuamotionCloudTest.QMCloudDescriptor exc = (QuamotionCloudTest.QMCloudDescriptor)this.getDescriptor();
            listener.getLogger().println("Log in to Quamotion Cloud... ");
            QMCloudClient cloudClient = new QMCloudClient();
            cloudClient.login(exc.apiToken);
            listener.getLogger().println("success.");
            listener.getLogger().println();
            listener.getLogger().println("Fetch tenant... ");
            List tenants = cloudClient.getProjects();
            if(tenants != null && !tenants.isEmpty()) {
                Tenant tenant = (Tenant)tenants.get(0);
                listener.getLogger().println("Using tenant: " + tenant.name);
                listener.getLogger().println("success.");
                listener.getLogger().println();
                Application application = null;
                if(this.publishNewApp != null) {
                    EnvVars env = build.getEnvironment(listener);
                    String expandedPath = env.expand(publishNewApp.getPath());

                    listener.getLogger().println("Publish application from " + expandedPath + "... ");
                    application = cloudClient.publishApplication(tenant, expandedPath);
                    listener.getLogger().println("Application published:");
                    listener.getLogger().println("    appId = " + application.appId);
                    listener.getLogger().println("    version = " + application.version);
                    listener.getLogger().println("    versionDisplayName = " + application.versionDisplayName);
                    listener.getLogger().println("    operatingSystem = " + application.operatingSystem);
                    listener.getLogger().println("    operatingSystemVersion = " + application.operatingSystemVersion);
                    listener.getLogger().println("    cpuType = " + application.cpuType);
                    listener.getLogger().println("    displayName = " + application.displayName);
                    listener.getLogger().println("success.");
                }

                listener.getLogger().println();
                TestPackage testPackage = null;
                if(this.publishNewTestPackage != null) {
                    EnvVars env = build.getEnvironment(listener);
                    String expandedPath = env.expand(publishNewApp.getPath());
                    listener.getLogger().println("Publish test package from " + expandedPath + "... ");
                    testPackage = cloudClient.publishTestPackage(tenant, expandedPath);
                    listener.getLogger().println("Test package published:");
                    listener.getLogger().println("    name = " + testPackage.name);
                    listener.getLogger().println("    version = " + testPackage.version);
                    listener.getLogger().println("    type = " + testPackage.testPackageType);
                    listener.getLogger().println("success.");
                }

                listener.getLogger().println();
                if(this.useApp != null) {
                    application = cloudClient.getApplication(tenant, this.useApp.appId, this.useApp.appVersion, this.useApp.appOperatingSystem);
                }

                listener.getLogger().println("Using application:");
                listener.getLogger().println("    appId = " + application.appId);
                listener.getLogger().println("    version = " + application.version);
                listener.getLogger().println("    versionDisplayName = " + application.versionDisplayName);
                listener.getLogger().println("    operatingSystem = " + application.operatingSystem);
                listener.getLogger().println("    operatingSystemVersion = " + application.operatingSystemVersion);
                listener.getLogger().println("    cpuType = " + application.cpuType);
                listener.getLogger().println("    displayName = " + application.displayName);
                listener.getLogger().println();
                if(this.useTestPackage != null) {
                    testPackage = cloudClient.getTestPackage(tenant, this.useTestPackage.testPackageName, this.useTestPackage.testPackageVersion);
                }

                listener.getLogger().println("Using test package:");
                listener.getLogger().println("    name = " + testPackage.name);
                listener.getLogger().println("    version = " + testPackage.version);
                listener.getLogger().println("    type = " + testPackage.testPackageType);
                listener.getLogger().println();
                listener.getLogger().println("Schedule test run... ");
                TestRun testRun = cloudClient.scheduleTestRun(tenant, testPackage, application, this.deviceGroupId);
                listener.getLogger().println("Test run details:");
                listener.getLogger().println("    testRunId = " + testRun.getTestRunId());
                listener.getLogger().println("    commitId = " + testRun.getCommitId());
                listener.getLogger().println("    scheduleId = " + testRun.getScheduleId());
                listener.getLogger().println("    applicationId = " + testRun.getApplicationId());
                listener.getLogger().println("    applicationVersion = " + testRun.getApplicationVersion());
                listener.getLogger().println("    testPackageName = " + testRun.getTestPackageName());
                listener.getLogger().println("    testPackageVersion = " + testRun.getTestPackageVersion());
                listener.getLogger().println("    deviceGroupId = " + testRun.getDeviceGroupId());
                listener.getLogger().println("    cronSchedule = " + testRun.getCronSchedule());
                listener.getLogger().println("success.");
                listener.getLogger().println();
                listener.getLogger().println("Wait until jobs are initialized... ");

                ArrayList<TestJob> testJobs = cloudClient.getTestJobs(tenant, testRun);
                while(testJobs.isEmpty())
                {
                    testJobs = cloudClient.getTestJobs(tenant, testRun);
                }

                QMTestRun action = new QMTestRun(build, new QMTestRunResult(testRun));
                build.addAction(action);
                action.waitForCompletion(cloudClient, tenant, listener.getLogger());

                listener.getLogger().println();
                return action.getTestRunResult().getFailedTests().size() == 0;
            } else {
                listener.getLogger().println("No tenants are registered to this api key");
                return false;
            }
        } catch (Exception var12) {
            listener.getLogger().println(var12.toString());
            return false;
        }
    }

    @Extension
    public static final class QMCloudDescriptor extends BuildStepDescriptor<Publisher> {
        public String apiToken;

        public QMCloudDescriptor() {
            super(QuamotionCloudTest.class);
            this.load();
        }

        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindParameters(this);
            this.apiToken = formData.getString("apiToken");
            this.save();
            return super.configure(req, formData);
        }

        public ListBoxModel doFillAppIdItems(@QueryParameter String appOperatingSystem) throws IOException {

            ListBoxModel items = new ListBoxModel();
            items.add("");
            QMCloudClient cloudClient = new QMCloudClient();
            cloudClient.login(this.apiToken);
            List tenants = cloudClient.getProjects();
            Tenant tenant = (Tenant)tenants.get(0);
            Iterator applicationIterator = cloudClient.getApplications(tenant).iterator();

            while(applicationIterator.hasNext()) {
                Application application = (Application)applicationIterator.next();
                if(application.operatingSystem.equals(appOperatingSystem)) {
                    items.add(application.appId);
                }
            }

            return items;
        }

        public ListBoxModel doFillAppVersionItems(@QueryParameter String appOperatingSystem, @QueryParameter String appId) throws IOException {
            ListBoxModel items = new ListBoxModel();
            items.add("");
            QMCloudClient cloudClient = new QMCloudClient();
            cloudClient.login(this.apiToken);
            List tenants = cloudClient.getProjects();
            Tenant tenant = (Tenant)tenants.get(0);

            Iterator applicationIterator = cloudClient.getApplications(tenant).iterator();
            while(applicationIterator.hasNext()) {
                Application application = (Application)applicationIterator.next();
                if(application.appId.equals(appId) && application.operatingSystem.equals(appOperatingSystem)) {
                    items.add(application.version);
                }
            }

            return items;
        }

        public ListBoxModel doFillAppOperatingSystemItems() throws IOException {
            List<ListBoxModel.Option> entries = new ArrayList<ListBoxModel.Option>();
            entries.add(new ListBoxModel.Option("iOS"));
            entries.add(new ListBoxModel.Option("Android"));
            return new ListBoxModel(entries);
        }

        public ListBoxModel doFillTestPackageNameItems() throws IOException {
            ListBoxModel items = new ListBoxModel();
            items.add("");
            QMCloudClient cloudClient = new QMCloudClient();
            cloudClient.login(this.apiToken);
            List tenants = cloudClient.getProjects();
            Tenant tenant = (Tenant)tenants.get(0);
            Iterator testPackageIterator = cloudClient.getTestPackages(tenant).iterator();

            while(testPackageIterator.hasNext()) {
                TestPackage testPackage = (TestPackage)testPackageIterator.next();
                items.add(testPackage.name, testPackage.name);
            }

            return items;
        }

        public ListBoxModel doFillTestPackageVersionItems(@QueryParameter String testPackageName) throws IOException {
            ListBoxModel items = new ListBoxModel();
            items.add("");
            QMCloudClient cloudClient = new QMCloudClient();
            cloudClient.login(this.apiToken);
            List tenants = cloudClient.getProjects();
            Tenant tenant = (Tenant)tenants.get(0);
            Iterator testPackageIterator = cloudClient.getTestPackages(tenant).iterator();

            while(testPackageIterator.hasNext()) {
                TestPackage testPackage = (TestPackage)testPackageIterator.next();
                if(testPackage.name.equals(testPackageName)) {
                    items.add(testPackage.version, testPackage.version);
                }
            }

            return items;
        }

        public ListBoxModel doFillDeviceGroupIdItems() throws IOException {
            ListBoxModel items = new ListBoxModel();
            items.add("");
            QMCloudClient cloudClient = new QMCloudClient();
            cloudClient.login(this.apiToken);
            List tenants = cloudClient.getProjects();
            Tenant tenant = (Tenant)tenants.get(0);
            Iterator var5 = cloudClient.getDeviceGroups(tenant).iterator();

            while(var5.hasNext()) {
                DeviceGroup deviceGroup = (DeviceGroup)var5.next();
                items.add(deviceGroup.DisplayName, deviceGroup.deviceGroupId.toString());
            }

            return items;
        }

        public FormValidation doValidateToken(@QueryParameter("apiToken") String token) throws IOException, ServletException {
            QMCloudClient cloudClient = new QMCloudClient();

            try {
                cloudClient.login(this.apiToken);
            } catch (Exception var5) {
                return FormValidation.error(var5, "Failed to login with token " + this.apiToken);
            }

            if(cloudClient.isConnected()) {
                List tenants = cloudClient.getProjects();
                if(tenants != null && !tenants.isEmpty()) {
                    if(tenants.size() > 1) {
                        return FormValidation.error("More than one tenant is registered to this api key");
                    } else {
                        Tenant tenant = (Tenant)tenants.get(0);
                        return FormValidation.ok("Connection ok, using tenant " + tenant.name);
                    }
                } else {
                    return FormValidation.error("No tenants are registered to this api key");
                }
            } else {
                return FormValidation.error("Failed to login with token " + this.apiToken);
            }
        }

        public boolean isApplicable(Class<? extends AbstractProject> clazz) {
            return true;
        }

        public String getDisplayName() {
            return "Quamotion Cloud Test";
        }
    }
}