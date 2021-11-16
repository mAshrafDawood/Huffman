import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;

public class Main {

    public static void returnErrorMessage(String message){
        JFrame FileDoesNotExist = new JFrame("Debug");
        FileDoesNotExist.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FileDoesNotExist.setLayout(new GridLayout(2, 1));
        JLabel errorMessage = new JLabel("Faulty Path entered");
        FileDoesNotExist.add(errorMessage);
        JButton confirm = new JButton("Confirm");
        FileDoesNotExist.add(confirm);
        FileDoesNotExist.setSize(400, 150);
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileDoesNotExist.dispose();
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        FileDoesNotExist.setVisible(true);
    }

    public static void main(String[] args) {
        //Setting up main frame
        JFrame mainWindow = new JFrame();
        mainWindow.setTitle("Standard Huffman Assignment");
        mainWindow.setSize(800, 600);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new GridLayout(2, 1));


        //Setting up Font
        Font f = new Font("serif", Font.PLAIN, 35);

        //Setting up upper panel
        JPanel upperPanal = new JPanel(new GridLayout(2, 1));
        JLabel message = new JLabel("Enter full path to file");
        message.setHorizontalAlignment(JLabel.CENTER);
        message.setFont(f);
        JTextField input = new JTextField();
        input.setFont(f);
        upperPanal.add(message);
        upperPanal.add(input);
        mainWindow.add(upperPanal);

        //Setting up lower panel
        JPanel lowerPanel = new JPanel(new GridLayout(1, 2));
        JButton compressButton = new JButton("Compress");
        JButton decompressButton = new JButton("Decompress");
        compressButton.setFont(f);
        decompressButton.setFont(f);
        lowerPanel.add(compressButton);
        lowerPanel.add(decompressButton);
        mainWindow.add(lowerPanel);

        //Setting up event listeners
        compressButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String path = input.getText();
                try {
                    byte[] inputBytes = Files.readAllBytes(new File(path).toPath());
                    StringBuilder inputString = new StringBuilder();
                    for (byte b : inputBytes) inputString.append((char) b);
                    byte[] outputBytes = compress(inputString.toString());
                    int index = path.length();
                    for (int i = 0; i < path.length(); i++){
                        if (path.charAt(i) == '.') index = i;
                    }
                    path = path.substring(0, index) + ".huffman";
                    Files.write(new File(path).toPath(), outputBytes);
                } catch (IOException ignored) {
                    returnErrorMessage("Unable to compress due to invalid path");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        decompressButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String path = input.getText();
                try {
                    byte[] inputBytes = Files.readAllBytes(new File(path).toPath());
                    String outputString = decompress(inputBytes);
                    File output = new File(path);
                    int index = path.length();
                    for (int i = 0; i < path.length(); i++){
                        if (path.charAt(i) == '.') index = i;
                    }
                    path = path.substring(0, index) + ".txt";
                    Files.write(new File(path).toPath(), outputString.getBytes());
                } catch (IOException ignored) {
                    returnErrorMessage("Unable to decompress due to invalid path");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        mainWindow.setVisible(true);
    }

    private static String decompress(byte[] input) {
        BitSet bitSet = BitSet.valueOf(input);
        int bitsToThrow = 0;
        for (int i = 0; i < 3; i++) {
            bitsToThrow |= bitSet.get(i) ? 1 << i : 0;
        }

        ArrayList<Boolean> bits = new ArrayList<>((input.length * 8) - 3 - bitsToThrow);
        for (int i = 3; i <= input.length * 8 - bitsToThrow; i++) {
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

        System.out.println("||===================================||");
        System.out.println("||             DICTIONARY            ||");
        System.out.println("||===================================||");
        for (Character c :
                dictionary.keySet()) {
            System.out.print(c + " -> ");
            for (Boolean b :
                    dictionary.get(c)) {
                System.out.print(b ? 1 : 0);
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
        int bitsToThrow = 8 - (bits.size() % 8);
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


