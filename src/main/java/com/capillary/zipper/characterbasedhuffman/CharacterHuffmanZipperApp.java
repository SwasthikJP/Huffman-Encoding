package com.capillary.zipper.characterbasedhuffman;

import com.capillary.zipper.utils.*;
import com.capillary.zipper.zipper.IZipperApp;
import com.capillary.zipper.characterbasedhuffman.compression.CharacterBasedHuffmanCompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.characterbasedhuffman.decompression.CharacterBasedHuffmanDecompresser;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;

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
