import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path for compression");
        String filePath = scanner.next();
        ICompressionApp iApp = new HuffmanCompressionApp();
        String compressFilepath = iApp.compress(filePath);
        System.out.println("Compressed file path is " + compressFilepath);
        String decompressFilepath = iApp.decompress(compressFilepath);
        System.out.println("Decompressed file path is " + decompressFilepath);

    }
}
