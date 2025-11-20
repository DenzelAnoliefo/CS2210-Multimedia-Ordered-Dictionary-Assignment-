public class BSTNode {

    private BSTNode parent;    // Reference to parent node
    private BSTNode leftChild; // Reference to left child
    private BSTNode rightChild; // Reference to right child
    private Data data;         // Data stored in this node

    // Default constructor: creates a leaf node with no parent, children, or data
    public BSTNode() {
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.data = null;
    }

    // Parameterized constructor: creates a node with specified parent, children, and data
    public BSTNode(BSTNode newParent, BSTNode newLeftChild, BSTNode newRightChild, Data newData) {
        this.parent = newParent;
        this.leftChild = newLeftChild;
        this.rightChild = newRightChild;
        this.data = newData;
    }

    // Getter for parent
    public BSTNode getParent(){
        return parent;
    }

    // Getter for right child
    public BSTNode getRightChild() {
        return rightChild;
    }

    // Getter for left child
    public BSTNode getLeftChild() {
        return leftChild;
    }

    // Getter for data
    public Data getData() {
        return data;
    } 

    // Setter for parent
    public void setParent(BSTNode newParent) {
        this.parent = newParent;
    }

    // Setter for left child
    public void setLeftChild(BSTNode newLeftChild) {
        this.leftChild = newLeftChild;
    }

    // Setter for right child
    public void setRightChild(BSTNode newRightChild) {
        this.rightChild = newRightChild;
    }

    // Setter for data
    public void setData(Data newData) {
        this.data = newData;
    }

    // Returns true if node is a leaf (i.e., has no children)
    public boolean isLeaf() {
        if (leftChild == null && rightChild == null) {
            return true; // Node is a leaf
        }
        return false; // Node has at least one child
    }
}
