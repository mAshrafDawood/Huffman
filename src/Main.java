import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                File file = new File(args[1]);
                if (file.exists()) {
                    if (args[0].toLowerCase().contains("c")) { // Compress
                        String input = Files.readString(file.toPath());

                        byte[] compressedTags = compress(input);
                        for (byte tag :
                                compressedTags) {
                            System.out.println("Tag<" + Byte.toUnsignedInt(tag) + ">");
                        }

                        String newPath = file.getPath() + ".lzw";
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
        //TODO
    }

    private static byte[] compress(String input) {
        //TODO
    }
}


