package com.capillary.zipper.wordbasedhuffman.compression;

import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.utils.*;
import com.capillary.zipper.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class WordBasedHuffmanCompresser implements IHuffmanCompresser {

    IHeaderInfoReaderWriter headerInfoReaderWriter;
    ICompressedFileReaderWriter compressedFileReaderWriter;


    public WordBasedHuffmanCompresser(){
       headerInfoReaderWriter=new WordHeaderInfoReaderWriter();
       compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();
    }

    public WordBasedHuffmanCompresser(IHeaderInfoReaderWriter headerInfoReaderWriter,ICompressedFileReaderWriter compressedFileReaderWriter){
        this.headerInfoReaderWriter=headerInfoReaderWriter;
        this.compressedFileReaderWriter=compressedFileReaderWriter;
    }

    @Override
    public IHashMap calculateCharacterFrequency(InputStream fileInputStream) throws IOException {
        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.startTimer();
        int character;
        IHashMap frequencyMap=new HashMapImpl();
        ByteInputStream byteInputStream = new ByteInputStream(fileInputStream);
        String temp="";

        while ((character = byteInputStream.getByte()) != -1) {

            if (!Character.isLetterOrDigit((char)character)) {
                if(temp!="") {
                    frequencyMap.put(temp, (int) frequencyMap.getOrDefault(temp,0) + 1);
                    temp="";
                }
                frequencyMap.put((char)character+"",(int)frequencyMap.getOrDefault((char)character+"",0)+1);

            }else{
                temp=temp+(char)character;
            }
        }
        if(temp!=""){
            frequencyMap.put(temp,(int)frequencyMap.getOrDefault(temp,0)+1);
        }
        byteInputStream.close();
        frequencyMap.put("{^}",1);

      return frequencyMap;
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
            return new HashMapImpl();
        }

        IHashMap hashMap = new HashMapImpl();

        if(rootNode.isLeafNode){
            hashMap.put(rootNode.value,"0");
        }else
            preOrder(rootNode, "",hashMap);
        return hashMap;
    }


    @Override
    public Boolean encodeFile(InputStream fileInputStream, OutputStream fileOutputStream, IHashMap hashMap, Node rootNode) throws IOException {
        ByteInputStream byteInputStream = new ByteInputStream(fileInputStream);
        ByteOutputStream byteOutputStream = new ByteOutputStream(fileOutputStream);


       if(!headerInfoReaderWriter.writeHeaderInfo(rootNode, byteOutputStream)){
            throw new IOException("Invalid huffman tree");
        }
        IZipperStats zipperStats=new FileZipperStats();
        zipperStats.calcHeaderSize(byteOutputStream);

        if(!compressedFileReaderWriter.writeCompressedFile(byteInputStream, byteOutputStream, hashMap)){
            throw new IOException("Invalid Prefix hashMap");
        }

        byteInputStream.close();
        byteOutputStream.closeStream();
        zipperStats.calcCompressedBodySize(byteOutputStream);
        return true;
    }

}
