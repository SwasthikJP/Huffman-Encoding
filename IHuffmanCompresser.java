import java.io.IOException;

public interface IHuffmanCompresser {

 

    String encodeFile(String filePath);

    void createHuffmanTree();

    void generateHuffmanCode();

    void calculateCharacterFrequency(String filePath);

}
