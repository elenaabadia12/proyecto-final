package proyecto;
import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("_id")
    private String id;
    private String title;
    private String theme; // Temática
    private String authorId; // Relación con Author
    private String publisherId; // Relación con Publisher

    public Book(String title, String theme, String authorId, String publisherId) {
        this.title = title;
        this.theme = theme;
        this.authorId = authorId;
        this.publisherId = publisherId;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getTheme() { return theme; }
    public String getAuthorId() { return authorId; }
    public String getPublisherId() { return publisherId; }

    // Setters 
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setTheme(String theme) { this.theme = theme; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public void setPublisherId(String publisherId) { this.publisherId = publisherId; }
}