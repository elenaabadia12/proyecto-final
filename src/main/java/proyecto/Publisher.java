package proyecto;
import com.google.gson.annotations.SerializedName;

public class Publisher {
    @SerializedName("_id")
    private String id;
    private String name;
    private String website;

    public Publisher(String name, String website) {
        this.name = name;
        this.website = website;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getWebsite() { return website; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setWebsite(String website) { this.website = website; }
}