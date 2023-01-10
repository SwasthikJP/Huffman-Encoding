import java.io.IOException;

public interface IHuffmanCompresser {

    String encodeFile(String filePath);

    void createHuffmanTree();

    void generatePrefixCode();

    void calculateCharacterFrequency(String filePath);

}
