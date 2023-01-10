import java.io.FileInputStream;
import java.io.IOException;

public class InputStream {

    int buffer;
    int bufferSize;
    FileInputStream fileInputStream;

    public InputStream(String fileName) {
        try {
            fileInputStream = new FileInputStream(fileName);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        bufferSize = 0;
        buffer = 0;
        loadBuffer();
    }

    public void loadBuffer() {
        try {
            if (bufferSize == 0) {
                buffer = fileInputStream.read();
                bufferSize = 8;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int getBit() {
        if (buffer == -1) {
            return -1;
        }
        int bit = buffer >> 7;
        bufferSize--;
        buffer = (buffer << 1) & 255;

        if (bufferSize == 0) {
            loadBuffer();
        }
        return bit;
    }

    public int getBits(int length) {
        int tempByte = 0;
        while (length != 0) {
            int bit = getBit();
            if (bit == -1) {
                return -1;
            }
            tempByte = (tempByte << 1) + bit;
            length--;
        }

        return tempByte;
    }
}
