import java.util.ArrayList;

public class Data {
    
    public String name;
    public ArrayList<MultimediaItem> media;


    public Data(String newName) {
        this.name = newName;
        this.media = new ArrayList<MultimediaItem>();
    }

    public void add(MultimediaItem newItem) {
        media.add(newItem);
    }

    public String getName() {
        return name;
    }

    public ArrayList<MultimediaItem> getMedia() {
        return media;
    }
}
