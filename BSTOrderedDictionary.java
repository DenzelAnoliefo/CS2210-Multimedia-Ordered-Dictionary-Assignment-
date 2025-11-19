import java.util.ArrayList;

public class BSTOrderedDictionary implements BSTOrderedDictionaryADT {
    
    BSTNode root;
    int numInternalNodes;

    public BSTOrderedDictionary() {
        this.numInternalNodes = 0;
        this.root = new BSTNode();
    }

    public BSTNode getRoot() {
        return root;
    }

    public int getNumInternalNodes() {
        return numInternalNodes;
    } 

    public ArrayList<MultimediaItem> get(BSTNode r, String k) {
        String nodeKey = r.getData().getName();

        if (r.isLeaf() == true) { 
            return null; 
        }
        else { 
            if (nodeKey.compareTo(k) == 0) {
                return r.getData().getMedia();
            }
            else if (nodeKey.compareTo(k) > 0) {
                return get(r.leftChild, k);
            }
            else {
                return get(r.rightChild, k);
            }
        }
    }

	public void put(BSTNode r, String name, String content, int type) {
        if (r.isLeaf()) {
            
            r.setData(new Data(name));
            r.getData().add(new MultimediaItem(content, type));

            r.leftChild = new BSTNode();
            r.rightChild = new BSTNode();

            numInternalNodes += 1;
            return;
        }

        if (r.getData().getName().equals(name)) {
            r.getData().getMedia().add(new MultimediaItem(content, type));
            return;
        } 

        if (name.compareTo(r.getData().getName()) < 0) {
            put(r.leftChild, name, content, type);
        }
        else {
            put(r.rightChild, name, content, type);
        }
    }
	
	public void remove(BSTNode r, String k) throws DictionaryException {
        
    }
	
	public void remove(BSTNode r, String k, int type) throws DictionaryException {

    }
	
	public Data successor(BSTNode r, String k) {

    }
	
	public Data predecessor(BSTNode r, String k) {

    }
	
	public Data smallest(BSTNode r) {

    }
	
	public Data largest(BSTNode r) {

    }
}
