public class HuffmanCompressionApp implements ICompressionApp{

    @Override
    public String compress(String filePath) {
        IHuffmanCompresser huffmanCompresser=new FrequencyBasedHuffmanCompresser(); 
        huffmanCompresser.calculateCharacterFrequency(filePath);
        huffmanCompresser.createHuffmanTree();
        huffmanCompresser.generateHuffmanCode();
        String compressFile=huffmanCompresser.encodeFile(filePath);

        return compressFile;
    }

    @Override
    public String decompress(String compressFilepath) {
        IHuffmanDecompresser huffmanDecompresser=null;
        huffmanDecompresser.createHuffmanTree(compressFilepath);
        String decompressFile=huffmanDecompresser.decodeFile(compressFilepath);
        return decompressFile;
    }
    
}
