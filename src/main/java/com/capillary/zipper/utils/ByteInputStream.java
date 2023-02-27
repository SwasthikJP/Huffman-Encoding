package com.capillary.zipper.utils;
import java.io.*;

public class ByteInputStream {

     int buffer;
     int bufferSize;
    BufferedInputStream fileInputStream;

    public ByteInputStream(java.io.InputStream inputStream) throws  IOException {
             if(inputStream==null){
                 throw  new NullPointerException("inputStream is null");
             }

             if(inputStream.available()==0){
                 throw new IOException("File is empty");
             }

            fileInputStream = new BufferedInputStream(inputStream, 1000000);


            bufferSize = 0;
            buffer = -1;

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
        if (bufferSize == 0) {
            loadBuffer();
        }
        if (buffer == -1) {
            return -1;
        }
        int bit = buffer >> 7;
        bufferSize--;
        buffer = (buffer << 1) & 255;


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

    public Object getObject()throws Exception{
        ObjectInputStream inputStream=new ObjectInputStream(fileInputStream);
       Object object= inputStream.readObject();
       return object;
    }

    public void close() throws IOException{
            fileInputStream.close();
    }

}
