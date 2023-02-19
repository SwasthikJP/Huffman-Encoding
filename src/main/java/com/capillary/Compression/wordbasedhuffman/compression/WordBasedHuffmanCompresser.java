package com.capillary.Compression.wordbasedhuffman.compression;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.HashMapImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;

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



    private IHashMap divideWords(IHashMap hashMap){

        int ignorePerc=100;

        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(((Map)hashMap.getMap()).entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> w1,
                               Map.Entry<String, Integer> w2)
            {
                return (w2.getValue()).compareTo(w1.getValue());
            }
        });




        for (int i=ignorePerc* list.size()/100;i<list.size();i++) {
            Map.Entry<String, Integer> aa=list.get(i);
            String word= aa.getKey();
            if(word=="{^}"){
                continue;
            }
            int wordCount= aa.getValue();
            hashMap.remove(word);
            for(int j=0;j<word.length();j++){
                hashMap.put(word.charAt(j)+"",(int)hashMap.get(word.charAt(j)+"")+wordCount);
            }
        }

        System.out.println(hashMap.getSize());
        return hashMap;
    }

    @Override
    public IHashMap calculateCharacterFrequency(InputStream fileInputStream) throws IOException {
        int character;
        IHashMap frequencyMap=new HashMapImpl();
        ByteInputStream byteInputStream = new ByteInputStream(fileInputStream);
        String temp="";
        while ((character = byteInputStream.getByte()) != -1) {
            if ((""+(char)character).matches("^[^a-zA-Z0-9]+$")) {
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

        frequencyMap.getSize();
        return frequencyMap;
//        return divideWords(frequencyMap);
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

        if(!compressedFileReaderWriter.writeCompressedFile(byteInputStream, byteOutputStream, hashMap)){
            throw new IOException("Invalid Prefix hashMap");
        }

        byteInputStream.close();
        byteOutputStream.closeStream();
        return true;
    }

}
