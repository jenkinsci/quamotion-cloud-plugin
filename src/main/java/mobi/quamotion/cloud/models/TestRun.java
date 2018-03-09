package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by BartSaintGermain on 2/23/2018.
 */
public class TestRun
{
    //,"scheduleId":null,"cronSchedule":null,"jobs":[]}

    @SerializedName("testRunId")
    public UUID testRunId;

    @SerializedName("deviceGroup")
    public UUID deviceGroupId;

    @SerializedName("operatingSystem")
    public int operatingSystem;

    @SerializedName("appId")
    public String applicationId;

    @SerializedName("appVersion")
    public String applicationVersion;

    @SerializedName("testPackageName")
    public String testPackageName;

    @SerializedName("testPackageVersion")
    public String testPackageVersion;

    @SerializedName("scheduled")
    public String scheduled;

    @SerializedName("commitId")
    private String commitId;

    @SerializedName("branch")
    public String branch;

    @SerializedName("scheduleId")
    public Integer scheduleId;

    @SerializedName("cronSchedule")
    public String cronSchedule;

    @SerializedName("jobs")
    public TestJob[] jobs;

    public UUID getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(UUID testRunId) {
        this.testRunId = testRunId;
    }

    public UUID getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(UUID deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public int getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(int operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getTestPackageName() {
        return testPackageName;
    }

    public void setTestPackageName(String testPackageName) {
        this.testPackageName = testPackageName;
    }

    public String getTestPackageVersion() {
        return testPackageVersion;
    }

    public void setTestPackageVersion(String testPackageVersion) {
        this.testPackageVersion = testPackageVersion;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCronSchedule() {
        return cronSchedule;
    }

    public void setCronSchedule(String cronSchedule) {
        this.cronSchedule = cronSchedule;
    }

    public TestJob[] getJobs() {
        return jobs;
    }

    public void setJobs(TestJob[] jobs) {
        this.jobs = jobs;
    }
}
