
public interface IHuffmanDecompresser {

    void createHuffmanTree(String compressFilepath);

    String decodeFile(String compressFilepath);

}
