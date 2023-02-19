package com.capillary.Compression.wordbasedhuffman.compression;

import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.HashMapImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WordBasedHuffmanCompresserTest {


    IHuffmanCompresser wordBasedHuffmanCompresser;
    @Rule
    public ExpectedException expectedException=ExpectedException.none();


    @Test
    public void calculateCharacterFrequency_WhenNormalAscii_ThenMatchfreqMap() throws IOException {

        wordBasedHuffmanCompresser=new WordBasedHuffmanCompresser();
        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        IHashMap hashMap=new HashMapImpl();
        hashMap.put(" ",1);
        hashMap.put("aB",1);
        hashMap.put("1",1);
        hashMap.put("a",1);
        hashMap.put("{^}",1);
        hashMap.put("/",1);

        IHashMap frequencyMap=wordBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);

        for(Object key:hashMap.keySet()){
            assertEquals(hashMap.get(key),frequencyMap.get(key));
        }

    }


    @Test
    public void calculateCharacterFrequency_WhenNormalUnicode_ThenMatchfreqMap() throws IOException {

        wordBasedHuffmanCompresser=new WordBasedHuffmanCompresser();
        String fileInput="\uD83D\uDE00 \uD83D\uDE00\uD83E\uDD79"; //😀 😀🥹
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        IHashMap hashMap=new HashMapImpl();
        hashMap.put(" ",1);
        hashMap.put("ð",3);
        hashMap.put("\u0080",2);
        hashMap.put("¥",1);
        hashMap.put("\u0098",2);
        hashMap.put("¹",1);
        hashMap.put("\u009F",3);
        hashMap.put("{^}",1);

        IHashMap frequencyMap=wordBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);

        for(Object key:frequencyMap.keySet()){
            assertEquals(hashMap.get(key),frequencyMap.get(key));
        }

    }


    Boolean dfs(Node root1, Node root2){
        if(root1==null && root2==null){
            return true;
        }
        if((root1.value!=null && root2.value!=null) && (!root1.value.equals(root2.value) || root1.frequency!=root2.frequency)){
            return false;
        }
        Boolean result=true;
        if(root1.left!=null){
            if(root2.left==null){
                result=result&&false;
            }else{
                result=result&&dfs(root1.left,root2.left);
            }
        }
        if(root1.right!=null){
            if(root2.right==null){
                result=result&&false;
            }else{
                result=result&&dfs(root1.right,root2.right);
            }
        }
        return  result;
    }

    public  Boolean dfs(Node node){
        if(node==null){
            return true;
        }
        System.out.println((String) node.value);
        if(node.left!=null){
            dfs(node.left);
        }
        if(node.right!=null){
            dfs(node.right);
        }
        return true;
    }

    @Test
    public void createHuffmanTree_WhenNormalWords_ThenMatchHuffmanTree() throws IOException{
        wordBasedHuffmanCompresser=new WordBasedHuffmanCompresser();
        IHashMap frequencyMap=new HashMapImpl();
        frequencyMap.put(" ",1);
        frequencyMap.put("aB",1);
        frequencyMap.put("1",1);
        frequencyMap.put("a",1);
        frequencyMap.put("{^}",1);
        frequencyMap.put("/",1);

        Node w1=new Node("a",1);
        Node w2=new Node("{^}",1);
        Node w3=new Node("aB",1);
        Node w4=new Node(" ",1);
        Node w5=new Node("/",1);
        Node w6=new Node("1",1);

        Node p1=new Node(w4,w5);
        Node p3=new Node(w2,w3);
        Node p4=new Node(w1,w6);
        Node p2=new Node(p3,p4);
        Node expectedRoot=new Node(p1,p2);
        dfs(expectedRoot);
        assertEquals(true, dfs(expectedRoot, wordBasedHuffmanCompresser.createHuffmanTree(frequencyMap)));
    }

    @Test
    public void createHuffmanTree_WhenFrequencyMapIsEmpty_ThenRootNodeIsNull() throws IOException{
        wordBasedHuffmanCompresser =new WordBasedHuffmanCompresser();

        IHashMap frequencyMap=new HashMapImpl();

        assertEquals(null, wordBasedHuffmanCompresser.createHuffmanTree(frequencyMap));

    }

    @Test
    public void generatePrefixCode_WhenNormalCharacter_ThenMatchPrefixCode() throws IOException{
        wordBasedHuffmanCompresser =new WordBasedHuffmanCompresser();
        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        Node w1=new Node("a",1);
        Node w2=new Node("{^}",1);
        Node w3=new Node("aB",1);
        Node w4=new Node(" ",1);
        Node w5=new Node("/",1);
        Node w6=new Node("1",1);

        Node p1=new Node(w4,w5);
        Node p3=new Node(w2,w3);
        Node p4=new Node(w1,w6);
        Node p2=new Node(p3,p4);
        Node rootNode=new Node(p1,p2);


        IHashMap expectedPrefixCode=new HashMapImpl();

        expectedPrefixCode.put(" ","00");
        expectedPrefixCode.put("/","01");
        expectedPrefixCode.put("{^}","100");
        expectedPrefixCode.put("aB","101");
        expectedPrefixCode.put("a","110");
        expectedPrefixCode.put("1","111");

        IHashMap hashMap= wordBasedHuffmanCompresser.generatePrefixCode(rootNode);
        for(Object key:expectedPrefixCode.keySet()){
                assertEquals(expectedPrefixCode.get(key),hashMap.get(key));
        }
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsNull_ThenHashMapEmpty() throws IOException {
        wordBasedHuffmanCompresser =new WordBasedHuffmanCompresser();

        IHashMap hashMap= wordBasedHuffmanCompresser.generatePrefixCode(null);

            assertEquals(0,hashMap.getSize());
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleNonLeafNode_ThenHashMapEmpty() throws IOException {
        wordBasedHuffmanCompresser =new WordBasedHuffmanCompresser();
        Node rootNode=new Node(null,null);

        IHashMap hashMap= wordBasedHuffmanCompresser.generatePrefixCode(rootNode);
            assertEquals(0,hashMap.getSize());
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleLeafNode_ThenPrefixCodeMatch() throws IOException {
        wordBasedHuffmanCompresser =new WordBasedHuffmanCompresser();
        Node rootNode=new Node("a",1);

      IHashMap expectedHashMap=new HashMapImpl();
      expectedHashMap.put("a","0");

        IHashMap hashMap= wordBasedHuffmanCompresser.generatePrefixCode(rootNode);
        for(Object key:hashMap.keySet()){
                assertEquals(expectedHashMap.get(key),hashMap.get(key));

        }
    }


    @Test
    public void encodeFile_WhenNormalCharacters_ThenCompressedFilesMatch() throws Exception {


//        IHuffmanCompresser huffmanCompresser = new WordBasedHuffmanCompresser();
//
//        String fileInput="aB 1/a";
//        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
//                (Charset.forName("UTF-8")));
//        Node w1=new Node("a",1);
//        Node w2=new Node("{^}",1);
//        Node w3=new Node("aB",1);
//        Node w4=new Node(" ",1);
//        Node w5=new Node("/",1);
//        Node w6=new Node("1",1);
//
//        Node p1=new Node(w4,w5);
//        Node p3=new Node(w2,w3);
//        Node p4=new Node(w1,w6);
//        Node p2=new Node(p3,p4);
//        Node rootNode=new Node(p1,p2);
//
//
//        IHashMap hashMap=new StrKeyStrValHashMap();
//
//        hashMap.put(" ","00");
//        hashMap.put("/","01");
//        hashMap.put("{^}","100");
//        hashMap.put("aB","101");
//        hashMap.put("a","110");
//        hashMap.put("1","111");



//        byte[] compressFileArray=byteArrayOutputStream.toByteArray();
//        byte[] expectedFileArray={32,36,16,18,-14,6,-10,-68,-5,2,97,66,64,88,96,38,52,-18,-128};
//
//        assertEquals(compressFileArray.length,expectedFileArray.length);
//        assertTrue(Arrays.equals(compressFileArray,expectedFileArray));



        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        Node rootNode=new Node("",0);

        IHashMap hashMap=new HashMapImpl();


        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(1);

        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
        doReturn(true).when(headerInfoReaderWriter).writeHeaderInfo(any(Node.class),any(ByteOutputStream.class));


        ICompressedFileReaderWriter compressedFileReaderWriter = spy(new CompressedWordFileReaderWriterImpl());
        doReturn(true).when(compressedFileReaderWriter).writeCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(IHashMap.class));

        IHuffmanCompresser huffmanCompresser = new WordBasedHuffmanCompresser(headerInfoReaderWriter,compressedFileReaderWriter);

        assertTrue(huffmanCompresser.encodeFile(inputStream,outputStream,hashMap,rootNode));





    }



@Test
    public void encodeFile_WhenIHeaderReaderWriterReturnsFalse_ThenThrowException() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Invalid huffman tree");


        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
//        Node w1=new Node("a",1);
//        Node w2=new Node("{^}",1);
//        Node w3=new Node("aB",1);
//        Node w4=new Node(" ",1);
//        Node w5=new Node("/",1);
//        Node w6=new Node("1",1);
//
//        Node p1=new Node(w4,w5);
//        Node p3=new Node(w2,w3);
//        Node p4=new Node(w1,w6);
//        Node p2=new Node(p3,p4);
//        Node rootNode=new Node(p1,p2);

    Node rootNode=new Node("",0);

        IHashMap hashMap=new HashMapImpl();

//        hashMap.put(" ","00");
//        hashMap.put("/","01");
//        hashMap.put("aB","101");
//        hashMap.put("a","110");
//        hashMap.put("1","111");
//        hashMap.put("{^}","100");


    ByteArrayOutputStream outputStream=new ByteArrayOutputStream(1);

        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
       doReturn(false).when(headerInfoReaderWriter).writeHeaderInfo(any(Node.class),any(ByteOutputStream.class));


            ICompressedFileReaderWriter compressedFileReaderWriter = spy(ICompressedFileReaderWriter.class);
         doReturn(true).when(compressedFileReaderWriter).writeCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(IHashMap.class));

    IHuffmanCompresser huffmanCompresser = new WordBasedHuffmanCompresser(headerInfoReaderWriter,compressedFileReaderWriter);

       huffmanCompresser.encodeFile(inputStream,outputStream,hashMap,rootNode);

    }

@PrepareForTest(WordBasedHuffmanCompresser.class)
    @Test
    public void encodeFile_WhenICompressedFileReaderWriterReturnsFalse_ThenThrowException() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Invalid Prefix hashMap");

        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        Node rootNode=new Node("",0);

        IHashMap hashMap=new HashMapImpl();


        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(1);

        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
        doReturn(true).when(headerInfoReaderWriter).writeHeaderInfo(any(Node.class),any(ByteOutputStream.class));


        ICompressedFileReaderWriter compressedFileReaderWriter = spy(new CompressedWordFileReaderWriterImpl());
        doReturn(false).when(compressedFileReaderWriter).writeCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(IHashMap.class));

        IHuffmanCompresser huffmanCompresser = new WordBasedHuffmanCompresser(headerInfoReaderWriter,compressedFileReaderWriter);

        huffmanCompresser.encodeFile(inputStream,outputStream,hashMap,rootNode);

    }



    }