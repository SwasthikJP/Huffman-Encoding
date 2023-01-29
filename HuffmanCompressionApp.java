public class HuffmanCompressionApp implements ICompressionApp {

    @Override
    public String compress(String filePath) {

        ICompressionStats compressionStats=new FileCompressionStats();
        compressionStats.startTimer();
        IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();
        huffmanCompresser.calculateCharacterFrequency(filePath);
        huffmanCompresser.createHuffmanTree();
        huffmanCompresser.generatePrefixCode();
        String compressFilePath = huffmanCompresser.encodeFile(filePath);
        compressionStats.stopTimer();
        compressionStats.displayCompressionStats(filePath,compressFilePath);
        

        return compressFilePath;
    }

    @Override
    public String decompress(String compressFilepath) {

        ICompressionStats compressionStats=new FileCompressionStats();
        compressionStats.startTimer();
        
        IHuffmanDecompresser huffmanDecompresser = new FrequencyBasedHuffmanDecompresser(compressFilepath);
        huffmanDecompresser.createHuffmanTree();
        String decompressFilepath = huffmanDecompresser.decodeFile(compressFilepath);

        compressionStats.stopTimer();
        compressionStats.displayDecompressionStats(compressFilepath,decompressFilepath);

        return decompressFilepath;
       
    }

}
