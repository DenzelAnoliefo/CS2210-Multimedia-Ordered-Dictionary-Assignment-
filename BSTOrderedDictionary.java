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

    public ArrayList<MultimediaItem> get(BSTNode r, String key) {
        String nodeKey = r.getData().getName();

        if (r.isLeaf() == true) { 
            return null; 
        }
        else { 
            if (nodeKey.compareTo(key) == 0) {
                return r.getData().getMedia();
            }
            else if (nodeKey.compareTo(key) > 0) {
                return get(r.leftChild, key);
            }
            else {
                return get(r.rightChild, key);
            }
        }
    }

	public void put(BSTNode r, String key, String content, int type) {
        if (r.isLeaf()) {
            
            r.setData(new Data(key));
            r.getData().add(new MultimediaItem(content, type));

            r.leftChild = new BSTNode();
            r.rightChild = new BSTNode();

            numInternalNodes += 1;
            return;
        }

        if (r.getData().getName().equals(key)) {
            r.getData().getMedia().add(new MultimediaItem(content, type));
            return;
        } 

        if (key.compareTo(r.getData().getName()) < 0) {
            put(r.leftChild, key, content, type);
        }
        else {
            put(r.rightChild, key, content, type);
        }
    }
	
	public void remove(BSTNode r, String key) throws DictionaryException {
        BSTNode target = findNode(r, key);
        if (target == null || target.isLeaf())
            throw new DictionaryException("Key not found.");

        // CASE 1: target is a leaf ⇒ cannot happen (internal nodes store keys)
        // CASE 2: target has one child
        if (target.getLeftChild().isLeaf() && !target.getRightChild().isLeaf()) {
            replaceNode(target, target.getRightChild());
            numInternalNodes--;
            return;
        }
        if (!target.getLeftChild().isLeaf() && target.getRightChild().isLeaf()) {
            replaceNode(target, target.getLeftChild());
            numInternalNodes--;
            return;
        }

        // CASE 3: target has TWO children
        // Replace with successor
        BSTNode succ = smallestNode(target.getRightChild());
        target.setData(succ.getData());

        // Remove successor node
        if (succ.getRightChild().isLeaf()) {
            replaceNode(succ, succ.getRightChild());
        } else {
            replaceNode(succ, succ.getRightChild());
        }

        numInternalNodes--;
    }
	
	public void remove(BSTNode r, String key, int type) throws DictionaryException {
        BSTNode target = findNode(r, key);
        if (target == null || target.isLeaf())
            throw new DictionaryException("Key not found.");

        ArrayList<MultimediaItem> list = target.getData().getMedia();
        list.removeIf(item -> item.getType() == type);

        // If empty after deletion → remove whole node
        if (list.isEmpty()) {
            remove(r, key);
        }
    }

	
	public Data successor(BSTNode r, String key) {
        BSTNode target = findNode(r, key);
        if (target == null || target.isLeaf()) return null;

        // CASE 1: right subtree exists
        if (!target.getRightChild().isLeaf()) {
            return smallest(target.getRightChild());
        }

        // CASE 2: climb up to find first left turn
        BSTNode parent = target.getParent();
        BSTNode child = target;

        while (parent != null && parent.getRightChild() == child) {
            child = parent;
            parent = parent.getParent();
        }

        return (parent == null) ? null : parent.getData();
    }

	
	public Data predecessor(BSTNode r, String key) {
        BSTNode target = findNode(r, key);
        if (target == null || target.isLeaf()) return null;

        // CASE 1: left subtree exists
        if (!target.getLeftChild().isLeaf()) {
            return largest(target.getLeftChild());
        }

        // CASE 2: climb up to find first right turn
        BSTNode parent = target.getParent();
        BSTNode child = target;

        while (parent != null && parent.getLeftChild() == child) {
            child = parent;
            parent = parent.getParent();
        }

        return (parent == null) ? null : parent.getData();
    }

	
	public Data smallest(BSTNode r) {
        if (r == null || r.isLeaf()) return null;

        BSTNode curr = r;
        while (!curr.getLeftChild().isLeaf()) {
            curr = curr.getLeftChild();
        }
        return curr.getData();
    }

	
	public Data largest(BSTNode r) {
        if (r == null || r.isLeaf()) return null;

        BSTNode curr = r;
        while (!curr.getRightChild().isLeaf()) {
            curr = curr.getRightChild();
        }
        return curr.getData();
    }

    private BSTNode findNode(BSTNode r, String key) {
        if (r == null || r.isLeaf()) return null;

        int cmp = key.compareTo(r.getData().getName());

        if (cmp == 0) return r;
        if (cmp < 0) return findNode(r.getLeftChild(), key);
        return findNode(r.getRightChild(), key);
    }

    private void replaceNode(BSTNode oldNode, BSTNode newNode) {
        BSTNode parent = oldNode.getParent();

        if (parent != null) {
            if (parent.getLeftChild() == oldNode)
                parent.setLeftChild(newNode);
            else
                parent.setRightChild(newNode);
        }

        newNode.setParent(parent);
    }

    private BSTNode smallestNode(BSTNode r) {
        BSTNode curr = r;
        while (!curr.getLeftChild().isLeaf()) {
            curr = curr.getLeftChild();
        }
        return curr;
    }

}
