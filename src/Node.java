import java.util.ArrayList;
import java.util.HashMap;

public class Node implements Comparable<Node> {
    Node left;
    Node right;
    Integer freq;
    Character key;

    ArrayList<Boolean> code;

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;

        this.freq = right.freq + left.freq;
    }

    public Node(Character key, Integer freq) {
        this.freq = freq;
        this.key = key;
    }

    @Override
    public int compareTo(Node node){
        return this.freq - node.freq;
    }

    public static void updateCodes(Node root) {
        if(root.code == null) {
            if (root.right != null)
                root.right.code = new ArrayList<>();
            if (root.left != null)
                root.left.code = new ArrayList<>();
        }
        else {
            if (root.right != null)
                root.right.code = new ArrayList<>(root.code);
            if (root.left != null)
                root.left.code = new ArrayList<>(root.code);
        }

        if (root.right != null) {
            root.right.code.add(false);
            updateCodes(root.right);
        }

        if (root.left != null) {
            root.left.code.add(true);
            updateCodes(root.left);
        }
    }

    public static HashMap<Character, ArrayList<Boolean>> getDictionary(Node root) {
        HashMap<Character, ArrayList<Boolean>> result = new HashMap<>();

        if (root.key != null) {
            result.put(root.key, root.code);
            return result;
        }

        result.putAll(getDictionary(root.left));
        result.putAll(getDictionary(root.right));
        return result;
    }
}
