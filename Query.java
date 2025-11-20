import java.io.*;
import java.util.*;

public class Query {

    private BSTOrderedDictionary bst;

    // Constructor reads input file and builds the BST
    public Query(String inputFile) {
        bst = new BSTOrderedDictionary();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String keyLine;
            while ((keyLine = br.readLine()) != null) {
                keyLine = keyLine.toLowerCase();  // convert key to lowercase
                String contentLine = br.readLine();
                if (contentLine == null) break;

                int type = determineType(contentLine);
                bst.put(bst.getRoot(), keyLine, contentLine, type);
            }
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }

    // Determines the type of a multimedia item based on content
    private int determineType(String content) {
        content = content.toLowerCase();
        if (content.endsWith(".wav") || content.endsWith(".mid")) return 2;
        if (content.endsWith(".jpg") || content.endsWith(".gif")) return 3;
        if (content.endsWith(".html")) return 4;
        return 1;
    }

    // Main method: loops until the user types "end"
    public static void main(String[] args) {
        String result, nextCommand;
        StringReader keyboard = new StringReader();

        if (args.length != 1) {
            System.out.println("Usage: java Query filename");
            System.exit(0);
        }

        Query myProgram = new Query(args[0]);

        while (true) {
            nextCommand = keyboard.read("Enter next command: ");
            if (nextCommand.equalsIgnoreCase("end")) break;

            result = processCommand(nextCommand, myProgram.bst);
            System.out.println(result);
        }
    }

    // Process a command string and return the output
    public static String processCommand(String command, BSTOrderedDictionary bst) {
        StringTokenizer st = new StringTokenizer(command);
        if (!st.hasMoreTokens()) return "Invalid command";

        String cmd = st.nextToken().toLowerCase();

        try {
            switch (cmd) {
                case "get":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyGet = st.nextToken().toLowerCase();
                    return handleGet(bst, keyGet);

                case "remove":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyRemove = st.nextToken().toLowerCase();
                    bst.remove(bst.getRoot(), keyRemove);
                    return "";

                case "delete":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyDelete = st.nextToken().toLowerCase();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    int typeDelete = Integer.parseInt(st.nextToken());
                    bst.remove(bst.getRoot(), keyDelete, typeDelete);
                    return "";

                case "add":
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String keyAdd = st.nextToken().toLowerCase();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    String contentAdd = st.nextToken();
                    if (!st.hasMoreTokens()) return "Invalid command";
                    int typeAdd = Integer.parseInt(st.nextToken());
                    bst.put(bst.getRoot(), keyAdd, contentAdd, typeAdd);
                    return "";

                case "next":
                    // Handle next command
                    // ToDo: implement using bst.successor
                    return "TODO: next command";

                case "prev":
                    // Handle prev command
                    // ToDo: implement using bst.predecessor
                    return "TODO: prev command";

                case "first":
                    Data smallest = bst.smallest(bst.getRoot());
                    if (smallest != null) return smallest.getName();
                    return "The ordered dictionary is empty.";

                case "last":
                    Data largest = bst.largest(bst.getRoot());
                    if (largest != null) return largest.getName();
                    return "The ordered dictionary is empty.";

                case "size":
                    return "There are " + bst.getNumInternalNodes() + " keys in the ordered dictionary";

                default:
                    return "Invalid command";
            }
        } catch (DictionaryException e) {
            return e.getMessage();
        }
    }

    // Handles the 'get' command
    private static String handleGet(BSTOrderedDictionary bst, String key) {
        ArrayList<MultimediaItem> items = bst.get(bst.getRoot(), key);
        if (items == null) {
            // key not found → print predecessor and successor
            StringBuilder sb = new StringBuilder();
            sb.append("The word ").append(key).append(" is not in the ordered dictionary.\n");

            Data pred = bst.predecessor(bst.getRoot(), key);
            Data succ = bst.successor(bst.getRoot(), key);

            sb.append("Preceding word: ").append(pred != null ? pred.getName() : "").append("\n");
            sb.append("Following word: ").append(succ != null ? succ.getName() : "");
            return sb.toString();
        }

        for (MultimediaItem item : items) {
            try {
                switch (item.getType()) {
                    case 1: 
                        System.out.println(item.getContent()); 
                        break;
                    case 2:
                        SoundPlayer sp = new SoundPlayer();
                        sp.play(item.getContent());
                        break;

                    case 3:
                        PictureViewer pv = new PictureViewer();
                        pv.show(item.getContent());
                        break;

                    case 4:
                        ShowHTML sh = new ShowHTML();
                        sh.show(item.getContent());
                        break;

                }
            } catch (Exception e) {
                System.out.println("Error processing multimedia item: " + e.getMessage());
            }
        }
        return "";
    }
}

