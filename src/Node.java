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

    public static ArrayList<Boolean> convertToBits(Node root) {
        ArrayList<Boolean> result = new ArrayList<>();
        if (root.key != null) {
            result.add(true);

            for (int i = 0; i < 8; i++) {
                result.add((((int) root.key >> i) & 1) == 1);
            }
        }
        else {
            result.add(false);
            result.addAll(convertToBits(root.left));
            result.addAll(convertToBits(root.right));
        }

        return result;
    }

    private static ArrayList<Boolean> buffer = new ArrayList<>();

    public static int getRemainingBufferSize() {
        return buffer.size();
    }

    public static Node convertToNodes(ArrayList<Boolean> bits) {
        return convertToNodes(bits,true);
    }

    private static Node convertToNodes(ArrayList<Boolean> bits, boolean resetBuffer) {
        if (resetBuffer) buffer = bits;

        boolean isLeaf = buffer.get(0);
        buffer.remove(0);

        if(isLeaf) {
            int charCode = 0;
            for (int i = 0; i < 8; i++) {
                charCode |= buffer.get(i) ? 1 << i : 0;
            }

            buffer.subList(0, 8).clear();

            return new Node((char) charCode, -1);
        }
        else {
            return new Node(convertToNodes(bits, false), convertToNodes(bits, false));
        }
    }
}
