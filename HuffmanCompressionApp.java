public class HuffmanCompressionApp implements ICompressionApp{

    @Override
    public String compress(String filePath) {
        IHuffmanCompresser huffmanCompresser=new FrequencyBasedHuffmanCompresser(); 
        huffmanCompresser.calculateCharacterFrequency(filePath);
        huffmanCompresser.createHuffmanTree();
        huffmanCompresser.generatePrefixCode();
        String compressFile=huffmanCompresser.encodeFile(filePath);

        return compressFile;
    }

    @Override
    public String decompress(String compressFilepath) {
        IHuffmanDecompresser huffmanDecompresser=new FrequencyBasedHuffmanDecompresser(compressFilepath);
        huffmanDecompresser.createHuffmanTree();
        String decompressFile=huffmanDecompresser.decodeFile(compressFilepath);
        return decompressFile;
        // return null;
    }
    
}
