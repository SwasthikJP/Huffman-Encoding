package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Checksum {



    public byte[] calcCheckSum(ByteInputStream inputStream)throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");

        int byteBuffer=0;

        //Read file data and update in message digest
        while ((byteBuffer = inputStream.getByte()) != -1) {
            digest.update((byte) byteBuffer);
        }

        inputStream.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();
        return bytes;
    }

    public void writeFileCheckSum(byte[] checkSum,OutputStream outputStream)throws IOException{
        for(byte b:checkSum){
            outputStream.write(Byte.toUnsignedInt(b));
        }
    }

    public byte[] readFileCheckSum(InputStream inputStream)throws IOException{
      byte[] checkSum=new byte[16];
        for(int i=0;i<16;i++){
            checkSum[i]=(byte) inputStream.read();
        }
        return checkSum;
    }

    public Boolean validateCheckSum(byte[] checkSum1,byte[] checkSum2){

            if(Arrays.equals(checkSum1,checkSum2)){
                Logger.getAnonymousLogger().info("Lossless Compression Achieved!");
                return true;
            }else{
                Logger.getAnonymousLogger().info("Lossy Compression!");
                return false;
            }
    }
}