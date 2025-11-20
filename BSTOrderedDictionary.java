import java.util.ArrayList;

public class BSTOrderedDictionary implements BSTOrderedDictionaryADT {
    
    BSTNode root;               // Root node of the BST
    int numInternalNodes;       // Count of internal nodes (non-leaf nodes)

    public BSTOrderedDictionary() {
        this.numInternalNodes = 0; 
        this.root = new BSTNode(); // Initialize with an empty leaf node
    }

    public BSTNode getRoot() {
        return root; // Getter for root
    }

    public int getNumInternalNodes() {
        return numInternalNodes; // Getter for number of internal nodes
    } 

    // Retrieve list of MultimediaItems associated with a key
    public ArrayList<MultimediaItem> get(BSTNode r, String key) {
        if (r == null || r.isLeaf()) { 
            return null; // Key not found or reached a leaf
        }
        
        String nodeKey = r.getData().getName();
        
        if (nodeKey.equals(key)) {
            return r.getData().getMedia(); // Key found, return media list
        }
        else if (key.compareTo(nodeKey) < 0) {
            return get(r.getLeftChild(), key); // Go left if key is smaller
        }
        else {
            return get(r.getRightChild(), key); // Go right if key is larger
        }
    }

    // Insert a new key-media pair into the BST
    public void put(BSTNode r, String key, String content, int type) {
        if (r.isLeaf()) {
            // Convert leaf to internal node with new data
            r.setData(new Data(key));
            r.getData().add(new MultimediaItem(content, type));

            // Create new leaf children with parent pointers
            BSTNode leftLeaf = new BSTNode();
            leftLeaf.setParent(r);
            r.setLeftChild(leftLeaf);
            
            BSTNode rightLeaf = new BSTNode();
            rightLeaf.setParent(r);
            r.setRightChild(rightLeaf);

            numInternalNodes++; // Increment internal node count
            return;
        }

        // If key exists, just add the media item
        if (r.getData().getName().equals(key)) {
            r.getData().add(new MultimediaItem(content, type));
            return;
        } 

        // Recursively insert based on comparison
        if (key.compareTo(r.getData().getName()) < 0) {
            put(r.getLeftChild(), key, content, type);
        }
        else {
            put(r.getRightChild(), key, content, type);
        }
    }
    
    // Remove an entire node by key
    public void remove(BSTNode r, String key) throws DictionaryException {
        BSTNode target = findNode(r, key);
        if (target == null || target.isLeaf()) {
            throw new DictionaryException("Key not found."); // Node does not exist
        }

        // CASE 1: Node has two leaf children (no real children)
        if (target.getLeftChild().isLeaf() && target.getRightChild().isLeaf()) {
            target.setData(null);       // Remove node data
            target.setLeftChild(null);  // Remove children
            target.setRightChild(null);
            numInternalNodes--;
            return;
        }

        // CASE 2: Node has exactly one child
        if (target.getLeftChild().isLeaf() && !target.getRightChild().isLeaf()) {
            replaceNode(target, target.getRightChild()); // Replace node with its child
            numInternalNodes--;
            return;
        }
        
        if (!target.getLeftChild().isLeaf() && target.getRightChild().isLeaf()) {
            replaceNode(target, target.getLeftChild()); // Replace node with its child
            numInternalNodes--;
            return;
        }

        // CASE 3: Node has two children
        BSTNode succ = smallestNode(target.getRightChild()); // Inorder successor
        target.setData(succ.getData()); // Copy successor's data to target

        // Remove successor (it will have at most one child)
        if (succ.getLeftChild().isLeaf() && succ.getRightChild().isLeaf()) {
            succ.setData(null);
            succ.setLeftChild(null);
            succ.setRightChild(null);
        } else {
            replaceNode(succ, succ.getRightChild());
        }

        numInternalNodes--;
    }
    
    // Remove a specific media type from a node
    public void remove(BSTNode r, String key, int type) throws DictionaryException {
        BSTNode target = findNode(r, key);
        if (target == null || target.isLeaf()) {
            throw new DictionaryException("Key not found."); // Node doesn't exist
        }

        ArrayList<MultimediaItem> list = target.getData().getMedia();
        list.removeIf(item -> item.getType() == type); // Remove media items matching type

        // Delete node entirely if no media remains
        if (list.isEmpty()) {
            remove(r, key);
        }
    }

    // Find the successor of a given key
    public Data successor(BSTNode r, String key) {
        BSTNode target = findNode(r, key);
        
        if (target != null && !target.isLeaf()) {
            if (!target.getRightChild().isLeaf()) {
                return smallest(target.getRightChild()); // Successor in right subtree
            }

            BSTNode parent = target.getParent();
            BSTNode child = target;

            // Go up until we find a left child
            while (parent != null && parent.getRightChild() == child) {
                child = parent;
                parent = parent.getParent();
            }

            return (parent == null) ? null : parent.getData();
        } else {
            return findFirstGreater(r, key); // Key doesn't exist
        }
    }

    // Find the predecessor of a given key
    public Data predecessor(BSTNode r, String key) {
        BSTNode target = findNode(r, key);
        
        if (target != null && !target.isLeaf()) {
            if (!target.getLeftChild().isLeaf()) {
                return largest(target.getLeftChild()); // Predecessor in left subtree
            }

            BSTNode parent = target.getParent();
            BSTNode child = target;

            // Go up until we find a right child
            while (parent != null && parent.getLeftChild() == child) {
                child = parent;
                parent = parent.getParent();
            }

            return (parent == null) ? null : parent.getData();
        } else {
            return findFirstLess(r, key); // Key doesn't exist
        }
    }

    // Get smallest key in subtree
    public Data smallest(BSTNode r) {
        if (r == null || r.isLeaf()) return null;

        BSTNode curr = r;
        while (!curr.getLeftChild().isLeaf()) {
            curr = curr.getLeftChild();
        }
        return curr.getData();
    }

    // Get largest key in subtree
    public Data largest(BSTNode r) {
        if (r == null || r.isLeaf()) return null;

        BSTNode curr = r;
        while (!curr.getRightChild().isLeaf()) {
            curr = curr.getRightChild();
        }
        return curr.getData();
    }

    // Helper to find a node by key
    private BSTNode findNode(BSTNode r, String key) {
        if (r == null || r.isLeaf()) return null;

        int cmp = key.compareTo(r.getData().getName());
        if (cmp == 0) return r;
        if (cmp < 0) return findNode(r.getLeftChild(), key);
        return findNode(r.getRightChild(), key);
    }

    // Replace a node with another node in the tree
    private void replaceNode(BSTNode oldNode, BSTNode newNode) {
        BSTNode parent = oldNode.getParent();

        if (parent == null) {
            root = newNode; // Replacing root
            newNode.setParent(null);
        } else {
            if (parent.getLeftChild() == oldNode) {
                parent.setLeftChild(newNode);
            } else {
                parent.setRightChild(newNode);
            }
            newNode.setParent(parent);
        }
    }

    // Helper to find smallest node in subtree
    private BSTNode smallestNode(BSTNode r) {
        if (r == null || r.isLeaf()) return null;
        
        BSTNode curr = r;
        while (curr.getLeftChild() != null && !curr.getLeftChild().isLeaf()) {
            curr = curr.getLeftChild();
        }
        return curr;
    }

    // Find smallest key strictly greater than the given key
    private Data findFirstGreater(BSTNode r, String key) {
        if (r == null || r.isLeaf()) return null;
        
        Data result = null;
        BSTNode curr = r;
        
        while (curr != null && !curr.isLeaf()) {
            int cmp = key.compareTo(curr.getData().getName());
            if (cmp < 0) {
                result = curr.getData(); // Potential successor
                curr = curr.getLeftChild();
            } else {
                curr = curr.getRightChild();
            }
        }
        
        return result;
    }

    // Find largest key strictly less than the given key
    private Data findFirstLess(BSTNode r, String key) {
        if (r == null || r.isLeaf()) return null;
        
        Data result = null;
        BSTNode curr = r;
        
        while (curr != null && !curr.isLeaf()) {
            int cmp = key.compareTo(curr.getData().getName());
            if (cmp > 0) {
                result = curr.getData(); // Potential predecessor
                curr = curr.getRightChild();
            } else {
                curr = curr.getLeftChild();
            }
        }
        
        return result;
    }
}
