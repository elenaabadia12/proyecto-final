package proyecto;
import com.google.gson.annotations.SerializedName;

public class Author {
    @SerializedName("_id")
    private String id;
    private String name;
    private String nationality;

    public Author(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getNationality() { return nationality; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setNationality(String nationality) { this.nationality = nationality; }
}