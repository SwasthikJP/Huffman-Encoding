package com.capillary.Compression.huffmanimplementation.huffmancompression;
import com.capillary.Compression.huffmanimplementation.ArrayBasedFrequencyMap;
import com.capillary.Compression.utils.IFrequencyMap;
import com.capillary.Compression.utils.InputStream;
import com.capillary.Compression.utils.OutputStream;
import com.capillary.Compression.huffmanimplementation.Node;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class FrequencyBasedHuffmanCompresser implements IHuffmanCompresser {


    @Override
    public IFrequencyMap calculateCharacterFrequency(java.io.InputStream fileInputStream) throws  IOException {
        int character;
        IFrequencyMap frequencyMap=new ArrayBasedFrequencyMap();
        InputStream inputStream = new InputStream(fileInputStream);

        while ((character = inputStream.getByte()) != -1) {
            frequencyMap.put(character,frequencyMap.get(character)+1);
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
    public Node createHuffmanTree(IFrequencyMap frequencyMap) {

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
            if (frequencyMap.get(i) != 0) {
                subTrees.add(new Node(i, frequencyMap.get(i)));
            }
        }

        return combineSubTrees(subTrees);
    }

    private void preOrder(Node node, String code,String[] hashCode) {
        if (node == null) {
            return;
        }

        if (node.isLeafNode) {
            hashCode[node.value] = code;
        } else {
            preOrder(node.left, code + "0",hashCode);
            preOrder(node.right, code + "1",hashCode);
        }
    }

    @Override
    public String[] generatePrefixCode(Node rootNode) {
        if(rootNode==null){
            return new String[257];
        }
        String[] hashCode = new String[257];

        if(rootNode.isLeafNode){
            hashCode[rootNode.value]="0";
        }else
          preOrder(rootNode, "",hashCode);
        return hashCode;
    }

    private void writeCompressedCharacters(InputStream inputStream, OutputStream outputStream, String[] hashCode)throws IOException {

        int character;
        while ((character = inputStream.getByte()) != -1) {
            outputStream.writeBits(hashCode[character], hashCode[character].length());
        }

        outputStream.writeBits(hashCode[256], hashCode[256].length());
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
    public Boolean encodeFile(java.io.InputStream fileInputStream, java.io.OutputStream fileOutputStream, String[] hashCode,Node rootNode) throws  IOException {
        InputStream inputStream = new InputStream(fileInputStream);

        if(rootNode==null){
            throw  new NullPointerException("Root node is null");
        }
        OutputStream outputStream = new OutputStream(fileOutputStream);

        writeHeaderInfo(rootNode, outputStream);

        writeCompressedCharacters(inputStream, outputStream, hashCode);
        inputStream.close();
        outputStream.closeStream();
        return true;
    }

}
