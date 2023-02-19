package com.capillary.Compression.wordbasedhuffman;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.Compression.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.Compression.zipper.IZipperApp;

public class WordHuffmanZipperApp implements IZipperApp {


    private  IHuffmanCompresser huffmanCompresser;
    private  IHuffmanDecompresser huffmanDecompresser;

    public WordHuffmanZipperApp(){
        huffmanCompresser=new WordBasedHuffmanCompresser();
        huffmanDecompresser=new WordBasedHuffmanDecompresser();
    }

    public WordHuffmanZipperApp(IHuffmanCompresser huffmanCompresser,IHuffmanDecompresser huffmanDecompresser){
        this.huffmanCompresser=huffmanCompresser;
        this.huffmanDecompresser=huffmanDecompresser;
    }






    @Override
    public void compress(IFileHandler fileHandler) {
        try {



            IHashMap frequencyMap=huffmanCompresser.calculateCharacterFrequency(fileHandler.getInputStream());


            Node rootNode=huffmanCompresser.createHuffmanTree(frequencyMap);
//            dfs(rootNode);
            IHashMap hashMap= huffmanCompresser.generatePrefixCode(rootNode);


            huffmanCompresser.encodeFile(fileHandler.getInputStream(),fileHandler.getOutputStream(),hashMap, rootNode);

//            compressionStats.stopTimer();
//            compressionStats.displayCompressionStats(filePath, compressFilePath);
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
