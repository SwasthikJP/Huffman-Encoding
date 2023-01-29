import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputStream {

    int buffer;
    int bufferSize;
    BufferedOutputStream fileOutputStream;

    public OutputStream(String fileName) {
        try {
            fileOutputStream = new BufferedOutputStream(new FileOutputStream(fileName), 100000);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        buffer = 0;
        bufferSize = 0;
    }

    public void flushBuffer() {
        try {
            if (bufferSize == 8) {
                fileOutputStream.write(buffer);
                buffer = 0;
                bufferSize = 0;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void writeBit(int bit) {
        buffer = (buffer << 1) + bit;
        bufferSize++;
        if (bufferSize == 8) {
            flushBuffer();
        }
    }

    public void writeBits(int bits, int length) {
        while (length != 0) {
            int bit = bits >> (length - 1);
            length--;
            bits = (bits) & ((int) Math.pow(2, length) - 1);
            writeBit(bit);
        }
    }

    public void writeBits(String bits, int length) {
        for (int i = 0; i < length; i++) {
            int bit = (bits.charAt(i)) - '0';
            writeBit(bit);
        }
    }

    public void writeByte(int bits){
        try {
                fileOutputStream.write(bits);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void closeStream() {
        if (bufferSize != 0) {
            writeBits(0, 8 - bufferSize);

        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
