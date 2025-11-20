import java.io.*;
import java.util.*;

public class Query {

    private BSTOrderedDictionary bst; // The BST-based ordered dictionary

    // Constructor reads input file and builds the BST
    public Query(String inputFile) {
        bst = new BSTOrderedDictionary(); // Initialize the dictionary

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String keyLine;
            while ((keyLine = br.readLine()) != null) {
                keyLine = keyLine.toLowerCase();  // Convert key to lowercase for uniformity
                String contentLine = br.readLine(); // Read associated content
                if (contentLine == null) break;

                int type = determineType(contentLine); // Determine type based on content
                bst.put(bst.getRoot(), keyLine, contentLine, type); // Insert into BST
            }
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }

    // Determines the type of a multimedia item based on file extension or content
    private int determineType(String content) {
        content = content.toLowerCase();
        if (content.endsWith(".wav") || content.endsWith(".mid")) return 2; // Audio
        if (content.endsWith(".jpg") || content.endsWith(".gif")) return 3; // Image
        if (content.endsWith(".html")) return 4; // HTML
        return 1; // Text by default
    }

    // Public instance method to process user commands
    public String processCommand(String command) {
        StringTokenizer st = new StringTokenizer(command);
        if (!st.hasMoreTokens()) return "Invalid command";

        String cmd = st.nextToken().toLowerCase(); // Command keyword

        try {
            switch (cmd) {
                case "get":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyGet = st.nextToken().toLowerCase();
                    return handleGet(keyGet); // Handle 'get' command

                case "remove":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyRemove = st.nextToken().toLowerCase();
                    try {
                        bst.remove(bst.getRoot(), keyRemove); // Remove entire node
                        return "";
                    } catch (DictionaryException e) {
                        return "No record in the ordered dictionary has key " + keyRemove + ".";
                    }

                case "delete":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyDelete = st.nextToken().toLowerCase();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    int typeDelete = Integer.parseInt(st.nextToken()); // Parse type
                    try {
                        bst.remove(bst.getRoot(), keyDelete, typeDelete); // Remove specific media
                        return "";
                    } catch (DictionaryException e) {
                        return "No record in the ordered dictionary has key " + keyDelete + ".";
                    }

                case "add":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyAdd = st.nextToken().toLowerCase();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String contentAdd = st.nextToken();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    int typeAdd = Integer.parseInt(st.nextToken());
                    bst.put(bst.getRoot(), keyAdd, contentAdd, typeAdd); // Add media to BST
                    return "";

                case "next":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyNext = st.nextToken().toLowerCase();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    int dNext = Integer.parseInt(st.nextToken());
                    return handleNext(keyNext, dNext); // Print key + d successors

                case "prev":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyPrev = st.nextToken().toLowerCase();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    int dPrev = Integer.parseInt(st.nextToken());
                    return handlePrev(keyPrev, dPrev); // Print key + d predecessors

                case "first":
                    Data smallest = bst.smallest(bst.getRoot());
                    if (smallest != null) return smallest.getName(); // Smallest key
                    return "The ordered dictionary is empty.";

                case "last":
                    Data largest = bst.largest(bst.getRoot());
                    if (largest != null) return largest.getName(); // Largest key
                    return "The ordered dictionary is empty.";

                case "size":
                    return "There are " + bst.getNumInternalNodes() + " keys in the ordered dictionary"; // Count of keys

                default:
                    return "Invalid command";
            }
        } catch (NumberFormatException e) {
            return "Invalid command"; // Invalid integer for type/d
        } catch (Exception e) {
            return "Invalid command"; // Catch-all for other exceptions
        }
    }

    // Main method: loops until the user types "end"
    public static void main(String[] args) {
        String result, nextCommand;
        StringReader keyboard = new StringReader(); // Placeholder for user input

        if (args.length != 1) {
            System.out.println("Usage: java Query filename");
            System.exit(0);
        }

        Query myProgram = new Query(args[0]); // Build BST from file

        while (true) {
            nextCommand = keyboard.read("Enter next command: "); // Prompt user
            if (nextCommand.equalsIgnoreCase("end")) break;

            result = myProgram.processCommand(nextCommand); // Execute command
            if (result != null && !result.isEmpty()) {
                System.out.println(result); // Print any output
            }
        }
    }

    // PRIVATE helper to handle 'get' command
    private String handleGet(String key) {
        ArrayList<MultimediaItem> items = bst.get(bst.getRoot(), key);
        if (items == null) {
            // Key not found → display predecessor and successor
            StringBuilder sb = new StringBuilder();
            sb.append("The word ").append(key).append(" is not in the ordered dictionary.");

            Data pred = bst.predecessor(bst.getRoot(), key);
            Data succ = bst.successor(bst.getRoot(), key);

            sb.append("\nPreceding word: ").append(pred != null ? pred.getName() : "");
            sb.append("\nFollowing word: ").append(succ != null ? succ.getName() : "");
            return sb.toString();
        }

        // Key exists → process each multimedia item
        for (MultimediaItem item : items) {
            try {
                switch (item.getType()) {
                    case 1: // Text
                        System.out.println(item.getContent()); 
                        break;
                    case 2: // Audio
                        SoundPlayer sp = new SoundPlayer();
                        sp.play(item.getContent());
                        break;
                    case 3: // Image
                        PictureViewer pv = new PictureViewer();
                        pv.show(item.getContent());
                        break;
                    case 4: // HTML
                        ShowHTML sh = new ShowHTML();
                        sh.show(item.getContent());
                        break;
                }
            } catch (MultimediaException e) {
                System.out.println("Error processing multimedia item: " + e.getMessage());
            }
        }
        return "";
    }

    // PRIVATE helper to handle 'next' command
    // Finds key (or nearest greater) and returns key + d successors
    private String handleNext(String key, int d) {
        List<String> keys = new ArrayList<>();
        ArrayList<MultimediaItem> items = bst.get(bst.getRoot(), key);
        Data current;
        
        if (items != null) {
            keys.add(key); // Key exists → add first
            current = bst.successor(bst.getRoot(), key);
            while (current != null && keys.size() <= d) {
                keys.add(current.getName()); // Add successors
                current = bst.successor(bst.getRoot(), current.getName());
            }
        } else {
            current = findFirstGreaterOrEqual(key); // Key doesn't exist → find first ≥ key
            while (current != null && keys.size() <= d) {
                keys.add(current.getName());
                current = bst.successor(bst.getRoot(), current.getName());
            }
        }
        
        if (keys.isEmpty()) return "There are no keys larger than or equal to " + key;
        return String.join(" ", keys);
    }

    // PRIVATE helper to handle 'prev' command
    // Finds key (or nearest smaller) and returns key + d predecessors in decreasing order
    private String handlePrev(String key, int d) {
        List<String> keys = new ArrayList<>();
        ArrayList<MultimediaItem> items = bst.get(bst.getRoot(), key);
        Data current;
        
        if (items != null) {
            keys.add(key); // Key exists → add first
            current = bst.predecessor(bst.getRoot(), key);
            while (current != null && keys.size() <= d) {
                keys.add(current.getName()); // Add predecessors
                current = bst.predecessor(bst.getRoot(), current.getName());
            }
        } else {
            current = findFirstLessOrEqual(key); // Key doesn't exist → find first ≤ key
            while (current != null && keys.size() <= d) {
                keys.add(current.getName());
                current = bst.predecessor(bst.getRoot(), current.getName());
            }
        }
        
        if (keys.isEmpty()) return "There are no keys smaller than or equal to " + key;
        return String.join(" ", keys);
    }

    // Find first key ≥ given key (used when key not found)
    private Data findFirstGreaterOrEqual(String key) {
        BSTNode curr = bst.getRoot();
        Data result = null;
        while (curr != null && !curr.isLeaf()) {
            int cmp = key.compareTo(curr.getData().getName());
            if (cmp == 0) return curr.getData(); // Exact match
            else if (cmp < 0) {
                result = curr.getData(); // Potential successor
                curr = curr.getLeftChild();
            } else {
                curr = curr.getRightChild();
            }
        }
        return result;
    }

    // Find first key ≤ given key (used when key not found)
    private Data findFirstLessOrEqual(String key) {
        BSTNode curr = bst.getRoot();
        Data result = null;
        while (curr != null && !curr.isLeaf()) {
            int cmp = key.compareTo(curr.getData().getName());
            if (cmp == 0) return curr.getData(); // Exact match
            else if (cmp < 0) {
                curr = curr.getLeftChild();
            } else {
                result = curr.getData(); // Potential predecessor
                curr = curr.getRightChild();
            }
        }
        return result;
    }
}
