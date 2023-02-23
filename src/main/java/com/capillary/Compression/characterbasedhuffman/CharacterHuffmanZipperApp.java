package com.capillary.Compression.characterbasedhuffman;

import com.capillary.Compression.utils.*;
import com.capillary.Compression.zipper.IZipperApp;
import com.capillary.Compression.characterbasedhuffman.compression.CharacterBasedHuffmanCompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.characterbasedhuffman.decompression.CharacterBasedHuffmanDecompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;

public class CharacterHuffmanZipperApp implements IZipperApp {


    private IHuffmanCompresser huffmanCompresser;
    private IHuffmanDecompresser huffmanDecompresser;

    public CharacterHuffmanZipperApp(){
        huffmanCompresser=new CharacterBasedHuffmanCompresser();
        huffmanDecompresser=new CharacterBasedHuffmanDecompresser();
    }

    public CharacterHuffmanZipperApp(IHuffmanCompresser huffmanCompresser, IHuffmanDecompresser huffmanDecompresser){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
    }



    @Override
    public void compress(IFileHandler fileHandler) {


        try {


            IHashMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());
            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
            IHashMap hashMap= huffmanCompresser.generatePrefixCode(rootNode);

//            System.out.println("average code length is "+calculateAverageCode(frequencyMap,hashCode));


            huffmanCompresser.encodeFile(fileHandler.getInputStream(),fileHandler.getOutputStream(),hashMap, rootNode);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void decompress(IFileHandler fileHandler) {


        try {



           Node rootNode=(Node)huffmanDecompresser.createHuffmanTree(fileHandler.getInputStream());

            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
