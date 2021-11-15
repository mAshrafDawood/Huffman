import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                File file = new File(args[1]);
                if (file.exists()) {
                    if (args[0].toLowerCase().contains("c")) { // Compress
                        String input = Files.readString(file.toPath());

                        byte[] compressedTags = compress(input);

                        String newPath = file.getPath() + ".huffman";
                        File compressedFile = new File(newPath);
                        compressedFile.createNewFile();

                        Files.write(compressedFile.toPath(), compressedTags);


                    } else if (args[0].toLowerCase().contains("d")) { // Decompress
                        byte[] input = Files.readAllBytes(file.toPath());

                        String decompressedTxt = decompress(input);
                        System.out.println(decompressedTxt);

                        String newPath = file.getPath() + ".txt";

                        File decompressedFile = new File(newPath);
                        decompressedFile.createNewFile();

                        List<String> lines = new ArrayList<>();
                        lines.add(decompressedTxt);
                        Files.write(decompressedFile.toPath(), lines);
                    } else {
                        System.out.println(args[0] + " is invalid argument");
                    }
                } else {
                    System.out.println(args[1] + " is not an existing file");
                }
            } else {
                System.out.println("No arguments were supplied");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String decompress(byte[] input) {
        BitSet bitSet = BitSet.valueOf(input);
        int bitsToThrow = 0;
        for (int i = 0; i < 3; i++) {
            bitsToThrow |= bitSet.get(i) ? 1 << i : 0;
        }

        ArrayList<Boolean> bits = new ArrayList<>((input.length * 8) - 3 - bitsToThrow);
        for (int i = 3; i < input.length * 8 - bitsToThrow; i++) {
            bits.add(bitSet.get(i));
        }

        Node root = Node.convertToNodes(bits);
        Node.updateCodes(root);

        StringBuilder builder = new StringBuilder();
        Node currentNode = root;
        for (Boolean b :
                bits) {
            if (b) currentNode = currentNode.left;
            else currentNode = currentNode.right;

            if (currentNode.key != null) {
                builder.append(currentNode.key);
                currentNode = root;
            }
        }

        return builder.toString();
    }

    private static byte[] compress(String input) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (int i = 0; i < input.length(); i++){
            Character c = input.charAt(i);
            if (frequency.containsKey(c))
                frequency.replace(c, frequency.get(c) + 1);
            else
                frequency.put(c, 1);
        }

        System.out.println("||===================================||");
        System.out.println("||          FREQUENCY TABLE          ||");
        System.out.println("||===================================||");

        for (Character c : frequency.keySet()){
            System.out.println(c + " -> " + frequency.get(c));
        }

        System.out.println("||===================================||");
        System.out.println();

        ArrayList<Node> nodes = new ArrayList<>();
        for (Character c :
                frequency.keySet()) {
            nodes.add(new Node(c, frequency.get(c)));
        }

        while (nodes.size() > 2){
            Collections.sort(nodes);

            Node l = nodes.get(0);
            Node r = nodes.get(1);

            nodes.remove(l);
            nodes.remove(r);
            nodes.add(new Node(l, r));
        }

        Node root = new Node(nodes.get(0), nodes.get(1));
        Node.updateCodes(root);
        HashMap<Character, ArrayList<Boolean>> dictionary = Node.getDictionary(root);

        for (Character c :
                dictionary.keySet()) {
            System.out.print(c + " -> ");
            for (Boolean b :
                    dictionary.get(c)) {
                System.out.print(b ? "1" : "0");
            }
            System.out.println();
        }
        System.out.println("||===================================||");
        System.out.println();

        ArrayList<Boolean> compressedBits = new ArrayList<>();
        for (int i = 0; i < input.length(); i++){
            Character c = input.charAt(i);
            compressedBits.addAll(dictionary.get(c));
        }

        System.out.println("Data:");
        System.out.println("Note that the first 3 bits doesn't belong to the data, rather they indicate how many bits "
                         + "should be thrown away at the end");
        for (Boolean b :
                compressedBits) {
            System.out.print(b ? "1" : "0");
        }
        System.out.println();

        ArrayList<Boolean> dictionaryBits = Node.convertToBits(root);

        dictionaryBits.addAll(compressedBits);
        return bitListToBytes(dictionaryBits);
    }

    private static byte[] bitListToBytes(ArrayList<Boolean> bits) {
        // 3 bits to indicate how many bits should be thrown away
        bits.add(0, false);
        bits.add(0, false);
        bits.add(0, false);
        int bitsToThrow = bits.size() % 8;
        for (int i = 0; i < 3; i++) {
            bits.set(i, ((bitsToThrow >> i) & 1) == 1);
        }

        BitSet bitSet = new BitSet(bits.size());
        for (int i = 0; i < bits.size(); i++) {
            bitSet.set(i, bits.get(i));
        }

        byte[] bytes = bitSet.toByteArray();
        if(bytes.length * 8 >= bits.size()) {
            return bytes;
        }
        else {
            return Arrays.copyOf(bytes, bits.size() / 8 + (bits.size() % 8 == 0 ? 0 : 1));
        }
    }
}


