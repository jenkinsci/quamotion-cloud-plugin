package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BartSaintGermain on 2/23/2018.
 */
public class TestJob
{
    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("status")
    public String status;

    @SerializedName("duration")
    public double duration;

    @SerializedName("started")
    public String started;

    @SerializedName("commitId")
    public String commitId;

    @SerializedName("hasArtifacts")
    public Boolean hasArtifacts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Boolean hasArtifacts() {
        return hasArtifacts;
    }

    public void setHasArtifacts(Boolean hasArtifacts) {
        this.hasArtifacts = hasArtifacts;
    }
}
