import java.util.ArrayList;

public class Data {
    
    private String name; // The key or identifier for this data node
    private ArrayList<MultimediaItem> media; // List of multimedia items associated with this key

    // Constructor: initializes the data with a name and empty media list
    public Data(String newName) {
        this.name = newName; // Set the key
        this.media = new ArrayList<MultimediaItem>(); // Initialize empty list of media
    }

    // Adds a multimedia item to this data's media list
    public void add(MultimediaItem newItem) {
        media.add(newItem);
    }

    // Returns the name/key of this data node
    public String getName() {
        return name;
    }

    // Returns the list of multimedia items associated with this data
    public ArrayList<MultimediaItem> getMedia() {
        return media;
    }
}
