package mobi.quamotion.cloud;

import com.google.gson.Gson;
import mobi.quamotion.cloud.models.*;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the
 */
public class QMCloudClient
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType APPLICATION = MediaType.parse("application/octet-stream");


    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    private String host = "https://cloud.quamotion.mobi";
    private String accessToken = null;


    public QMCloudClient(String host)
    {
        this.host = host;
    }

    public QMCloudClient()
    {
    }

    public String getHost()
    {
        return this.host;
    }

    public String getAccessToken() { return this.accessToken; };

    public void login(String apiKey) throws IOException {
        HashMap<String, String> loginData = new HashMap<String, String>();
        loginData.put("ApiKey", apiKey);
        String response = this.postFormRequest("/api/login", loginData);
        LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
        this.accessToken = loginResponse.accessToken;
    }

    public boolean isConnected()
    {
        return this.accessToken != null;
    }

    public List<Tenant> getProjects() throws IOException {
        String response = this.getRequest("/api/project");
        return this.gson.fromJson(response, ProjectResponse.class);
    }

    public TestPackage publishTestPackage(Tenant tenant, String filePath) throws IOException {
        String response = this.sendFile("/project/"+ tenant.name + "/api/testPackage", filePath);
        TestPackageResponse testPackages = this.gson.fromJson(response, TestPackageResponse.class);
        if(testPackages.size() == 0)
        {
            throw new IllegalStateException("No test package was added for: " + filePath);
        }

        if(testPackages.size() > 1)
        {
            throw new IllegalStateException("More than one test package were added for: " + filePath);
        }

        return testPackages.get(0);
    }

    public Application publishApplication(Tenant tenant, String filePath) throws IOException {
        String response = this.sendFile("/project/"+ tenant.name + "/api/app", filePath);

        List<Application> applications = this.gson.fromJson(response, ApplicationResponse.class);

        if(applications.size() == 0)
        {
            throw new IllegalStateException("No application was added for: " + filePath);
        }

        if(applications.size() > 1)
        {
            throw new IllegalStateException("More than one application were added for: " + filePath);
        }

        return applications.get(0);
    }

    public List<Application> getApplications(Tenant tenant) throws IOException {
        String response = this.getRequest("/project/"+ tenant.name +"/api/app");
        return this.gson.fromJson(response, ApplicationResponse.class);
    }

    public List<TestPackage> getTestPackages(Tenant tenant) throws IOException {
        String response = this.getRequest("/project/"+ tenant.name +"/api/testPackage");
        return this.gson.fromJson(response, TestPackageResponse.class);
    }

    public List<DeviceGroup> getDeviceGroups(Tenant tenant) throws IOException {
        String response = this.getRequest("/project/"+ tenant.name +"/api/deviceGroup");
        return this.gson.fromJson(response, DeviceGroupResponse.class);
    }

    public DeviceGroup getDeviceGroup(Tenant tenant, String deviceGroupId) throws IOException {
        List<DeviceGroup> deviceGroups = this.getDeviceGroups(tenant);
        DeviceGroup result = null;
        for (DeviceGroup deviceGroup : deviceGroups)
        {
            if(deviceGroup.deviceGroupId.toString().equals(deviceGroupId))
            {
                result = deviceGroup;
            }
        }

        return result;
    }

    public Application getApplication(Tenant tenant, String applicationId, String applicationVersion, String operatingSystem) throws IOException {
        List<Application> applications = this.getApplications(tenant);
        Application result = null;
        for (Application application : applications)
        {
            if(application.appId.equals(applicationId) && application.version.equals(applicationVersion) && application.operatingSystem.equals(operatingSystem))
            {
                result = application;
            }
        }

        return result;
    }

    public TestPackage getTestPackage(Tenant tenant, String name, String version) throws IOException {
        List<TestPackage> testPackages = this.getTestPackages(tenant);
        TestPackage result = null;
        for (TestPackage testPackage : testPackages)
        {
            if(testPackage.name.equals(name) && testPackage.version.equals(version))
            {
                result = testPackage;
            }
        }

        return result;
    }

    public TestRun scheduleTestRun(Tenant tenant, TestPackage testPackage, Application application, String deviceGroupId) throws IOException {
        CreateTestRunRequest createTestRunRequest = new CreateTestRunRequest();
        createTestRunRequest.app = application;
        createTestRunRequest.deviceGroupId = deviceGroupId;
        createTestRunRequest.schedule ="";
        createTestRunRequest.testPackage = testPackage;
        createTestRunRequest.testScriptEnvironmentVariables = new HashMap<String, String>();
        createTestRunRequest.testScriptParameters = "";

        String response = this.postJsonRequest("/project/"+ tenant.name +"/api/testRun", createTestRunRequest);

        return this.gson.fromJson(response, TestRun.class);
    }

    public TestRun scheduleTestRun(Tenant tenant, TestPackage testPackage, Application application, DeviceGroup deviceGroup) throws IOException {
        return this.scheduleTestRun(tenant, testPackage, application, deviceGroup.deviceGroupId.toString());
    }

    public TestRun getTestRun(Tenant tenant, String testRunId) throws IOException {
        String response = this.getRequest("/project/"+ tenant.name +"/api/testRun");
        ArrayList<TestRun> testRuns = this.gson.fromJson(response, TestRunResponse.class);

        TestRun testRun = null;
        for(TestRun tr : testRuns)
        {
            if(tr.getTestRunId().toString().equals(testRunId))
            {
                testRun = tr;
            }
        }

        return testRun;
    }

    public ArrayList<TestJob> getTestJobs(Tenant tenant, TestRun testRun) throws IOException {
        String response = this.getRequest("/project/"+ tenant.name +"/api/testRun/"+ testRun.getTestRunId() +"/jobs");
        return this.gson.fromJson(response, TestJobResponse.class);
    }

    public TestJob getTestJob(Tenant tenant, int jobId)throws IOException {
        String response = this.getRequest("/project/"+ tenant.name +"/api/job/"+ jobId);
        return this.gson.fromJson(response, TestJob.class);
    }

    public String getJobLog(Tenant tenant, TestJob testJob)throws IOException {
        return this.getRequest("/project/"+ tenant.name +"/api/job/"+ testJob.getId() +"/log");
    }

    private void ensureLogin()
    {
        if(this.accessToken == null)
        {
            throw new IllegalStateException("You are not logged in. Please log in and try again");
        }
    }

    private void ensureSuccess(Response response) throws IOException {
        if (!response.isSuccessful()) throw new IOException(response.body().string());
    }

    private  String sendFile(String relativeUrl, String filePath) throws IOException {
        this.ensureLogin();

        String url = this.host+relativeUrl;
        File file = new File(filePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", file.getName(),
                        RequestBody.create(APPLICATION, file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer " + this.accessToken)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        this.ensureSuccess(response);

        return response.body().string();
    }

    private String getRequest(String relativeUrl) throws IOException {
        this.ensureLogin();

        String url = this.host+relativeUrl;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer " + this.accessToken)
                .build();

        Response response = client.newCall(request).execute();
        this.ensureSuccess(response);

        return response.body().string();
    }

    private String postFormRequest(String relativeUrl, HashMap<String,String> form) throws IOException {
        String url = this.host+relativeUrl;

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for(Map.Entry<String, String> entry: form.entrySet())
        {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }

        FormBody formBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        this.ensureSuccess(response);

        return response.body().string();
    }

    private String postJsonRequest(String relativeUrl, Object object) throws IOException {
        this.ensureLogin();

        String url = this.host+relativeUrl;
        String json = this.gson.toJson(object);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization","Bearer " + this.accessToken)
                .build();
        Response response = client.newCall(request).execute();
        this.ensureSuccess(response);

        return response.body().string();
    }
}
