package com.capillary.zipper.characterbasedhuffman.compression;
import com.capillary.zipper.characterbasedhuffman.huffmanutils.*;
import com.capillary.zipper.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.zipper.utils.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class CharacterBasedHuffmanCompresser implements IHuffmanCompresser {

    IHeaderInfoReaderWriter headerInfoReaderWriter;
    ICompressedFileReaderWriter compressedFileReaderWriter;


    public CharacterBasedHuffmanCompresser(){
        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();
    }

    public CharacterBasedHuffmanCompresser(IHeaderInfoReaderWriter headerInfoReaderWriter,ICompressedFileReaderWriter compressedFileReaderWriter){
        this.headerInfoReaderWriter=headerInfoReaderWriter;
        this.compressedFileReaderWriter=compressedFileReaderWriter;
    }


    @Override
    public IHashMap calculateCharacterFrequency(java.io.InputStream fileInputStream) throws  IOException {
        int character;
        IHashMap frequencyMap=new ArrayBasedHashMap();
        ByteInputStream byteInputStream = new ByteInputStream(fileInputStream);

        while ((character = byteInputStream.getByte()) != -1) {
            frequencyMap.put(character,((int)frequencyMap.getOrDefault(character,0))+1);
        }
        byteInputStream.close();
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

        for (int i = 0; i < frequencyMap.getSize(); i++) {
            if ((int)frequencyMap.getOrDefault(i,0) != 0) {
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
            return new ArrayBasedHashMap();
        }
        IHashMap hashMap = new ArrayBasedHashMap();

        if(rootNode.isLeafNode){
            hashMap.put(rootNode.value,"0");
        }else
          preOrder(rootNode, "",hashMap);
        return hashMap;
    }


    @Override
    public Boolean encodeFile(java.io.InputStream fileInputStream, java.io.OutputStream fileOutputStream, IHashMap hashMap,Node rootNode) throws  IOException {

        ByteInputStream byteInputStream = new ByteInputStream(fileInputStream);
        ByteOutputStream byteOutputStream = new ByteOutputStream(fileOutputStream);

        if(!headerInfoReaderWriter.writeHeaderInfo(rootNode, byteOutputStream)){
            throw new IOException("Invalid huffman tree");
        }

        if(!compressedFileReaderWriter.writeCompressedFile(byteInputStream, byteOutputStream, hashMap)){
            throw new IOException("Invalid Prefix hashMap");
        }

        byteInputStream.close();
        byteOutputStream.closeStream();
        return true;
    }

}
