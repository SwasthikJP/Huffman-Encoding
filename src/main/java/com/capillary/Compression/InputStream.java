package com.capillary.Compression;
import java.io.*;

public class InputStream {

    int buffer;
    int bufferSize;
    BufferedInputStream fileInputStream;

    public InputStream(java.io.InputStream inputStream) throws  IOException {

             if(inputStream.available()==0){
                 IOException e=new IOException("File is empty");
                    throw e;
             }

            fileInputStream = new BufferedInputStream(inputStream, 1000000);

            bufferSize = 0;
            buffer = 0;

        // loadBuffer();
    }

    public void loadBuffer() throws  IOException{
        try {
            if (bufferSize == 0) {
                buffer = fileInputStream.read();
                bufferSize = 8;
            }
        } catch (IOException e) {
            IOException exception = new IOException ("InputStream is closed");
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }

    public int getBit() throws IOException{
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

    public int getBits(int length) throws IOException{
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

    public int getByte() throws IOException{
        try {
            return fileInputStream.read();
        } catch (Exception e) {
            IOException exception = new IOException ("InputStream is closed");
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }

    public void close() throws IOException{
            fileInputStream.close();
    }

}
