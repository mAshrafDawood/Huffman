public class Node implements Comparable<Node> {
    Node left;
    Node right;
    Integer freq;
    String key;

    @Override
    public int compareTo(Node node){
        return this.freq - node.freq;
    }
}
