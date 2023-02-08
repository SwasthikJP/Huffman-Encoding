package com.capillary.Compression;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class FrequencyBasedHuffmanCompresser implements IHuffmanCompresser {

    int[] characterFrequency;
    String[] huffmanCode;
    Node rootNode;

    public FrequencyBasedHuffmanCompresser() {
        characterFrequency = new int[257];
        huffmanCode = new String[257];
        rootNode = null;
    }

    @Override
    public void calculateCharacterFrequency(java.io.InputStream fileInputStream) throws  IOException {
        int character;
        InputStream inputStream = new InputStream(fileInputStream);

        while ((character = inputStream.getByte()) != -1) {
            characterFrequency[character]++;
        }
        inputStream.close();
        characterFrequency[256] = 1;
    }

    public void combineSubTrees(PriorityQueue<Node> pq) {
        if(pq.size()==0){
            return;
        }
        while (pq.size() != 1) {
            Node a = pq.poll();
            Node b = pq.poll();
            pq.add(new Node(a, b));
        }
        rootNode = pq.poll();
    }

    @Override
    public void createHuffmanTree() {

        PriorityQueue<Node> subTrees = new PriorityQueue<>(1, new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                return Integer.compare(a.frequency, b.frequency);
            }
        });

        for (int i = 0; i < characterFrequency.length; i++) {
            if (characterFrequency[i] != 0) {
                subTrees.add(new Node(i, characterFrequency[i]));
            }
        }
        combineSubTrees(subTrees);

    }

    public void preOrder(Node node, String code) {
        if (node == null) {
            return;
        }

        if (node.isLeafNode) {
            huffmanCode[node.value] = code;
        } else {
            preOrder(node.left, code + "0");
            preOrder(node.right, code + "1");
        }
    }

    @Override
    public void generatePrefixCode() {
        if(rootNode==null){
            return;
        }
        if(rootNode.isLeafNode){
            huffmanCode[rootNode.value]="0";
        }else
          preOrder(rootNode, "");
    }

    public void writeEncodedCharacters(InputStream inputStream, OutputStream outputStream) {

        int character;
        while ((character = inputStream.getByte()) != -1) {
            outputStream.writeBits(huffmanCode[character], huffmanCode[character].length());
        }

        outputStream.writeBits(huffmanCode[256], huffmanCode[256].length());
    }

    public void writeHeaderInfo(Node node, OutputStream outputStream) {
//        if (node == null) {
//            return;
//        }
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
    public String encodeFile(java.io.InputStream fileInputStream,String filePath) throws  IOException {
        InputStream inputStream = new InputStream(fileInputStream);
        // if(inputStream.)
        String[] filePathSplit=filePath.split("\\.(?=[^\\.]+$)");
        String compressFilePath=filePathSplit[0];
        if(rootNode==null){
            NullPointerException e=new NullPointerException("Root node is null");
            throw e;
        }
        FileOutputStream fileOutputStream=new FileOutputStream(compressFilePath + ".huf"+".txt");
        OutputStream outputStream = new OutputStream(fileOutputStream);

        writeHeaderInfo(rootNode, outputStream);

        writeEncodedCharacters(inputStream, outputStream);
        inputStream.close();
        outputStream.closeStream();
        return compressFilePath + ".huf"+".txt";
    }

}
