package com.capillary.Compression.wordbasedhuffman.compression;

import com.capillary.Compression.huffmanimplementation.compression.IHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import com.capillary.Compression.utils.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.IHeaderInfoReaderWriter;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.StrKeyIntValHashMap;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.StrKeyStrValHashMap;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

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
            return new StrKeyStrValHashMap();
        }
        IHashMap hashMap = new StrKeyStrValHashMap();

        if(rootNode.isLeafNode){
            hashMap.put(rootNode.value,"0");
        }else
            preOrder(rootNode, "",hashMap);
        return hashMap;
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
        IHashMap frequencyMap=new StrKeyIntValHashMap();
        com.capillary.Compression.utils.InputStream inputStream = new com.capillary.Compression.utils.InputStream(fileInputStream);
        String temp="";
        while ((character = inputStream.getByte()) != -1) {
            if ((""+(char)character).matches("^[^a-zA-Z0-9]+$")) {
                if(temp!="") {
                    frequencyMap.put(temp, (int) frequencyMap.get(temp) + 1);
                    temp="";
                }
                frequencyMap.put((char)character+"",(int)frequencyMap.get((char)character+"")+1);

            }else{
                temp=temp+(char)character;
            }
        }
        if(temp!=""){
            frequencyMap.put(temp,(int)frequencyMap.get(temp)+1);
        }
        inputStream.close();
        frequencyMap.put("{^}",1);

        frequencyMap.getSize();
        return frequencyMap;
//        return divideWords(frequencyMap);
    }
}
