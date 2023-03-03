package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.ByteInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Checksum {



    public <T> List<T> calcCheckSum(ByteInputStream inputStream)throws Exception {
        if(inputStream==null){
            return new ArrayList<>();
        }
        MessageDigest digest = MessageDigest.getInstance("MD5");

        int byteBuffer=0;

        //Read file data and update in message digest
        while ((byteBuffer = inputStream.getByte()) != -1) {
            digest.update((byte) byteBuffer);
        }

        inputStream.close();

        List<Byte> checkSumBytes=new ArrayList<>();


        //Get the hash's bytes
        byte[] bytes = digest.digest();
        for(byte b:bytes){
            checkSumBytes.add(b);
        }
        return (List<T>) checkSumBytes;
    }

    public <T> void writeCheckSum(List<T> checkSum, OutputStream outputStream)throws IOException{
       if(outputStream==null){
           return;
       }
        for(T b:checkSum){
            outputStream.write(Byte.toUnsignedInt((Byte) b));
        }
    }

    public <T> List<T> readCheckSum(InputStream inputStream)throws IOException{

        if(inputStream==null){
            return new ArrayList<>();
        }

      List<Byte> checkSum=new ArrayList<>();
        for(int i=0;i<16;i++){
            checkSum.add((byte) inputStream.read());
        }
        return (List<T>) checkSum;
    }

    public <T> Boolean validateCheckSum(List<T> checkSum1,List<T> checkSum2){
             if(checkSum1.equals(checkSum2)){
                Logger.getAnonymousLogger().info("Lossless Compression Achieved!");
                return true;
            }else{
                Logger.getAnonymousLogger().info("Lossy Compression!");
                return false;
            }
    }
}