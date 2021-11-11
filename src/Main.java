import java.io.File;
import java.io.IOException;
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

                        BitSet compressedTags = compress(input);

                        String newPath = file.getPath() + ".haffman";
                        File compressedFile = new File(newPath);
                        compressedFile.createNewFile();

                        Files.write(compressedFile.toPath(), compressedTags.toByteArray());


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
        //TODO
        return "";
    }

    private static BitSet compress(String input) {
        Set <Character> unique = new HashSet();
        for (int i = 0; i < input.length(); i++){
            unique.add(input.charAt(i));
        }
        Map<Character, Integer> frequency = new HashMap<>();
        for (Character c : unique){
            frequency.put(c, 0);
        }
        for (int i = 0; i < input.length(); i++){
            Character c = input.charAt(i);
            frequency.replace(c, frequency.get(c) + 1);
        }
        System.out.println("||===================================||");
        System.out.println("||          FREQUENCY TABLE          ||");
        System.out.println("||===================================||");
        for (Character c : unique){
            System.out.println(c + " -> " + frequency.get(c));
        }



        return new BitSet();
    }
}


