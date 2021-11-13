public class Node implements Comparable<Node> {
    Node left;
    Node right;
    Integer freq;
    Character key;

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
}
