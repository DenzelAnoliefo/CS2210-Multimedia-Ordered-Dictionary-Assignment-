public class MultimediaItem {
    
    String content;
    int type;

    public MultimediaItem(String newContent, int newType) {
        this.content = newContent;
        this.type = newType;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
