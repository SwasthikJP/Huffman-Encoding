package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.IHashMap;

import java.io.InputStream;

public class ChunkFileFreqrencyMapCalc implements Runnable{

    IHashMap frequencyMap;
    InputStream inputStream;

    public ChunkFileFreqrencyMapCalc(InputStream inputStream){
        this.inputStream=inputStream;
    }


    @Override
    public void run() {
        try {
            int character;
           frequencyMap = new HashMapImpl();
            ByteInputStream byteInputStream = new ByteInputStream(inputStream);
            String temp = "";

            while ((character = byteInputStream.getByte()) != -1) {

                if (!Character.isLetterOrDigit((char) character)) {
                    if (temp != "") {
                        frequencyMap.put(temp, (int) frequencyMap.getOrDefault(temp, 0) + 1);
                        temp = "";
                    }
                    frequencyMap.put((char) character + "", (int) frequencyMap.getOrDefault((char) character + "", 0) + 1);

                } else {
                    temp = temp + (char) character;
                }
            }
            if (temp != "") {
                frequencyMap.put(temp, (int) frequencyMap.getOrDefault(temp, 0) + 1);
            }
            byteInputStream.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    IHashMap getFrequencyMap(){
        return frequencyMap;
    }

}
