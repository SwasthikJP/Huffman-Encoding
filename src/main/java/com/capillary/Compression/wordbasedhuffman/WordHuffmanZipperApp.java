package com.capillary.Compression.wordbasedhuffman;

import com.capillary.Compression.huffmanimplementation.compression.IHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.decompression.IHuffmanDecompresser;
import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import com.capillary.Compression.utils.IFileHandler;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.Compression.wordbasedhuffman.decompression.WordBasedHuffmanDecompresser;
import com.capillary.Compression.zipper.IZipperApp;

public class WordHuffmanZipperApp implements IZipperApp {


    public void dfs(Node node){
        if(node==null){
            return;
        }
        if(node.isLeafNode){
            System.out.println(node.value+"  ---  "+node.frequency);
        }else{
            if(node.left!=null){
                dfs(node.left);
            }
            if(node.right!=null){
                dfs(node.right);
            }
        }
    }

    @Override
    public void compress(IFileHandler fileHandler) {
        try {

            IHuffmanCompresser huffmanCompresser = new WordBasedHuffmanCompresser();

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

            IHuffmanDecompresser huffmanDecompresser = new WordBasedHuffmanDecompresser();

            Node rootNode=(Node)huffmanDecompresser.createHuffmanTree(fileHandler.getInputStream());
//            System.out.println("hehejjjj "+((IHashMap)hashMap).getSize());

//            dfs(rootNode);

            huffmanDecompresser.decodeFile(fileHandler.getOutputStream(),rootNode);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
