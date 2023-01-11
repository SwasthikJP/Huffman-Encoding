
public interface ICompressionStats {

    void startTimer();

    void stopTimer();

    void displayDecompressionStats(String compressFilepath, String decompressFilepath);

    void displayCompressionStats(String filePath, String compressFile);


}
