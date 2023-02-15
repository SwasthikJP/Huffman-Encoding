package com.capillary.Compression.wordbasedhuffman.compression;

import com.capillary.Compression.huffmanimplementation.compression.IHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import com.capillary.Compression.utils.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.IHeaderInfoReaderWriter;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.StringHashMap;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.StringKeyValueHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.PriorityQueue;

public class WordBasedHuffmanCompresser implements IHuffmanCompresser {
    @Override
    public Boolean encodeFile(InputStream fileInputStream, OutputStream fileOutputStream, IHashMap hashMap, Node rootNode) throws IOException {
        com.capillary.Compression.utils.InputStream inputStream = new com.capillary.Compression.utils.InputStream(fileInputStream);

        if(rootNode==null){
            throw  new NullPointerException("Root node is null");
        }
        com.capillary.Compression.utils.OutputStream outputStream = new com.capillary.Compression.utils.OutputStream(fileOutputStream);

        IHeaderInfoReaderWriter headerInfoReaderWriter=new WordHeaderInfoReaderWriter();
        headerInfoReaderWriter.writeHeaderInfo(rootNode,outputStream);


        ICompressedFileReaderWriter compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();
        compressedFileReaderWriter.writeCompressedFile(inputStream, outputStream, hashMap);

        inputStream.close();
        outputStream.closeStream();
        return true;
    }

    private Node combineSubTrees(PriorityQueue<Node> pq) {
        if(pq.isEmpty()){
            return null;
        }
        while (pq.size() != 1) {
            Node a = pq.poll();
            Node b = pq.poll();
            pq.add(new Node(a, b));
        }
        return  pq.poll();
    }

    @Override
    public Node createHuffmanTree(IHashMap frequencyMap) {

        PriorityQueue<Node> subTrees = new PriorityQueue<>(1, new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                return Integer.compare(a.frequency, b.frequency);
            }
        });

        for(Object key:frequencyMap.keySet()){
            subTrees.add(new Node(key,(int)frequencyMap.get(key)));
        }
        return combineSubTrees(subTrees);
    }
    private void preOrder(Node node, String code,IHashMap hashMap) {
        if (node == null) {
            return;
        }

        if (node.isLeafNode) {
            hashMap.put(node.value, code);
        } else {
            preOrder(node.left, code + "0",hashMap);
            preOrder(node.right, code + "1",hashMap);
        }

    }

    @Override
    public IHashMap generatePrefixCode(Node rootNode) {
        if(rootNode==null){
            return new StringKeyValueHashMap();
        }
        IHashMap hashMap = new StringKeyValueHashMap();

        if(rootNode.isLeafNode){
            hashMap.put(rootNode.value,"0");
        }else
            preOrder(rootNode, "",hashMap);
        return hashMap;
    }



    @Override
    public IHashMap calculateCharacterFrequency(InputStream fileInputStream) throws IOException {
        int character;
        IHashMap frequencyMap=new StringHashMap();
        com.capillary.Compression.utils.InputStream inputStream = new com.capillary.Compression.utils.InputStream(fileInputStream);
        String temp="";
        while ((character = inputStream.getByte()) != -1) {
            if(character==32 || character==10 || character==13){
                if(temp!="") {
                    frequencyMap.put(temp, (int) frequencyMap.get(character) + 1);
                    temp="";
                }
                frequencyMap.put((char)character+"",(int)frequencyMap.get((char)character+"")+1);

            }else{
                temp=temp+(char)character;
            }
        }
        if(temp!=""){
            frequencyMap.put(temp,(int)frequencyMap.get(character)+1);
        }
        inputStream.close();
        frequencyMap.put("{^}",1);
        return frequencyMap;
    }
}
