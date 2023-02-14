package com.capillary.Compression.huffmanimplementation.huffmancompression;
import com.capillary.Compression.huffmanimplementation.IntegerArrayHashMap;
import com.capillary.Compression.huffmanimplementation.StringArrayHashMap;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.InputStream;
import com.capillary.Compression.utils.OutputStream;
import com.capillary.Compression.huffmanimplementation.Node;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class FrequencyBasedHuffmanCompresser implements IHuffmanCompresser {


    @Override
    public IHashMap calculateCharacterFrequency(java.io.InputStream fileInputStream) throws  IOException {
        int character;
        IHashMap frequencyMap=new IntegerArrayHashMap();
        InputStream inputStream = new InputStream(fileInputStream);

        while ((character = inputStream.getByte()) != -1) {
            frequencyMap.put(character,(int)frequencyMap.get(character)+1);
        }
        inputStream.close();
        frequencyMap.put(256,1);
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

//        for (int i = 0; i < characterFrequency.length; i++) {
//            if (characterFrequency[i] != 0) {
//                subTrees.add(new Node(i, characterFrequency[i]));
//            }
//        }
        for (int i = 0; i < frequencyMap.getSize(); i++) {
            if ((int)frequencyMap.get(i) != 0) {
                subTrees.add(new Node(i,(int) frequencyMap.get(i)));
            }
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
            return new StringArrayHashMap();
        }
        IHashMap hashMap = new StringArrayHashMap();

        if(rootNode.isLeafNode){
            hashMap.put(rootNode.value,"0");
        }else
          preOrder(rootNode, "",hashMap);
        return hashMap;
    }

    private void writeCompressedCharacters(InputStream inputStream, OutputStream outputStream, IHashMap hashMap)throws IOException {

        int character;
        String hashCode="";
        while ((character = inputStream.getByte()) != -1) {
            hashCode=(String) hashMap.get(character);
            outputStream.writeBits(hashCode, hashCode.length());
        }
        hashCode=(String)hashMap.get(256);
        outputStream.writeBits(hashCode,hashCode.length());
    }

    private void writeHeaderInfo(Node node, OutputStream outputStream) throws IOException{

        if (node.isLeafNode) {
            outputStream.writeBit(1);
            outputStream.writeBits(node.value, 9);
        } else {
            outputStream.writeBit(0);
            writeHeaderInfo(node.left, outputStream);
            writeHeaderInfo(node.right, outputStream);
        }

    }



    @Override
    public Boolean encodeFile(java.io.InputStream fileInputStream, java.io.OutputStream fileOutputStream, IHashMap hashMap,Node rootNode) throws  IOException {
        InputStream inputStream = new InputStream(fileInputStream);

        if(rootNode==null){
            throw  new NullPointerException("Root node is null");
        }
        OutputStream outputStream = new OutputStream(fileOutputStream);

        writeHeaderInfo(rootNode, outputStream);

        writeCompressedCharacters(inputStream, outputStream, hashMap);
        inputStream.close();
        outputStream.closeStream();
        return true;
    }

}
