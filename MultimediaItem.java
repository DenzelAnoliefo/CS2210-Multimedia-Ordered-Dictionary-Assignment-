public class MultimediaItem {
    
    private String content; // The content of the multimedia item (e.g., text, filename, URL)
    private int type;       // The type of multimedia: 1=text, 2=sound, 3=image, 4=HTML

    // Constructor: initializes a multimedia item with its content and type
    public MultimediaItem(String newContent, int newType) {
        this.content = newContent; // Set the content
        this.type = newType;       // Set the type
    }

    // Returns the content of this multimedia item
    public String getContent() {
        return content;
    }

    // Returns the type of this multimedia item
    public int getType() {
        return type;
    }
}
