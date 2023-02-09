package com.capillary.Compression;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputStream {

    int buffer;
    int bufferSize;
    BufferedOutputStream fileOutputStream;

    public OutputStream(java.io.OutputStream outputStream) throws IOException{
        fileOutputStream = new BufferedOutputStream(outputStream, 1000000);
        buffer = 0;
        bufferSize = 0;
    }

    public void flushBuffer() throws IOException{

            if (bufferSize == 8) {
                fileOutputStream.write(buffer);
                buffer = 0;
                bufferSize = 0;
            }
        }


    public void writeBit(int bit) throws IOException{
        buffer = (buffer << 1) + bit;
        bufferSize++;
        if (bufferSize == 8) {
            flushBuffer();
        }
    }

    public void writeBits(int bits, int length) throws IOException{
        if(length<0){
            throw new IOException("Negative Length");
        }
        while (length != 0) {
            int bit = bits >> (length - 1);
            length--;
            bits = (bits) & ((int) Math.pow(2, length) - 1);
            writeBit(bit);
        }
    }

    public void writeBits(String bits, int length) throws IOException{
        for (int i = 0; i < length; i++) {
            int bit = (bits.charAt(i)) - '0';
            writeBit(bit);
        }
    }

    public void writeByte(int bits) throws IOException{
            fileOutputStream.write(bits);
    }

    public void closeStream() throws IOException {
        if (bufferSize != 0) {
            writeBits(0, 8 - bufferSize);

        }
            fileOutputStream.close();
    }
}
