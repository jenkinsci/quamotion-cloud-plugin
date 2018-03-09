package mobi.quamotion.cloud.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BartSaintGermain on 2/22/2018.
 */
public class Tenant
{
    /// <summary>
    /// Gets or sets the name of the tenant.
    /// </summary>
    @SerializedName("name")
    public String name;

    /// <summary>
    /// Gets or sets the unique ID of this tenant.
    /// </summary>
    @SerializedName("tenantId")
    public Integer tenantId;

    /// <summary>
    /// Gets or sets the ID of the GitLab project that backs this tenant.
    /// </summary>
    @SerializedName("gitLabId")
    public Integer gitLabId;

    /// <summary>
    /// Gets or sets flags for this tenant.
    /// </summary>
    @SerializedName("flags")
    public int flags;

    /// <summary>
    /// Gets the URL of the tenant.
    /// </summary>
    @SerializedName("relativeUrl")
    public String relativeUrl;
}
