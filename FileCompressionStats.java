import java.io.File;
import java.time.Duration;

public class FileCompressionStats implements ICompressionStats {

    long startTime;
    long elapsedTime;

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        elapsedTime = System.nanoTime() - startTime;
    }

    @Override
    public void displayCompressionStats(String inputFilePath, String compressedFilePath) {

        Duration duration = Duration.ofNanos(elapsedTime);
        System.out.println("\r \nTime taken for compression: " + duration.toHoursPart() + " : "
                + duration.toMinutesPart() + " : " + duration.toSecondsPart() + " : " + duration.toMillisPart());

        File originalfile = new File(inputFilePath);
        File compressedFile = new File(compressedFilePath);

        if (originalfile.exists() && compressedFile.exists()) {
            System.out.println("Original File Size: " + originalfile.length() + " bytes");
            System.out.println("Compressed File Size: " + compressedFile.length() + " bytes\n");
        }
    }

    @Override
    public void displayDecompressionStats(String compressedFilePath, String decompressedFilePath) {

        Duration duration = Duration.ofNanos(elapsedTime);
        System.out.println("\r \n Time taken for decompression: " + duration.toHoursPart() + " : "
                + duration.toMinutesPart() + " : " + duration.toSecondsPart() + " : " + duration.toMillisPart());

        File compressedFile = new File(compressedFilePath);
        File decompressedFile = new File(decompressedFilePath);
        
        if (compressedFile.exists() && decompressedFile.exists()) {
            System.out.println("Compressed File Size: " + compressedFile.length() + " bytes");
            System.out.println("Decompressed File Size: " + decompressedFile.length() + " bytes\n ");
        }
    }

}
