package com.capillary.Compression.characterbasedhuffman.compression;

import com.capillary.Compression.characterbasedhuffman.huffmanutils.ArrayBasedHashMap;
import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanCompresser;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.wordbasedhuffman.compression.WordBasedHuffmanCompresser;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.HashMapImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class CharacterBasedHuffmanCompresserTest {

    CharacterBasedHuffmanCompresser characterBasedHuffmanCompresser;

    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Test
    public void calculateCharacterFrequency_WhenCharactersASCII_ThenCountShouldMatch() throws IOException {
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        int[] result=new int[257];
        result[97]=2;
        result[66]=1;
        result[32]=1;
        result[49]=1;
        result[47]=1;
        result[256]=1;

        IHashMap frequencyMap= characterBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);

        for(int i=0;i<result.length;i++){
            if(result[i]!=0)
              assertEquals(result[i],frequencyMap.get(i) );
        }

    }

    @Test
    public void calculateCharacterFrequency_WhenCharactersUNICODE_ThenCountShouldMatch() throws IOException {
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        String fileInput="✅♨\uD83D\uDE80";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        int[] result=new int[257];
        result[128]=1;
        result[133]=1;
        result[153]=1;
        result[154]=1;
        result[156]=1;
        result[159]=1;
        result[168]=1;
        result[226]=2;
        result[240]=1;
        result[256]=1;
       IHashMap frequencyMap= characterBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);
        for(int i=0;i< result.length;i++){
            if(result[i]!=0){
                assertEquals(result[i],frequencyMap.get(i));
            }
        }


    }



    @Test
    public  void calculateCharacterFrequency_WhenFileIsEmpty_ThenThrowFileIsEmptyExceptionMessage() throws IOException{
        expectedException.expect(IOException.class);
        expectedException.expectMessage("File is empty");
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        String fileInput="";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        characterBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);
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


    @Test
    public void createHuffmanTree_WhenNormalCharacters_ThenMatchHuffmanTree() throws IOException{
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        characterBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);

        IHashMap frequencyMap=new ArrayBasedHashMap();
        frequencyMap.put(97,1);
        frequencyMap.put(66,1);
        frequencyMap.put(256,1);

        Node l1=new Node(66,1);
        Node l2=new Node(97,1);
        Node l3=new Node(256,1);
        Node p1=new Node(l1,l3);
        Node expectedRoot=new Node(l2,p1);
      assertEquals(true, dfs(expectedRoot, characterBasedHuffmanCompresser.createHuffmanTree(frequencyMap)));
    }

    @Test
    public void createHuffmanTree_WhenFrequencyMapIsEmpty_ThenRootNodeIsNull() throws IOException{
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();

        IHashMap frequencyMap=new ArrayBasedHashMap();

        assertEquals(null, characterBasedHuffmanCompresser.createHuffmanTree(frequencyMap));

    }



    @Test
    public void generatePrefixCode_WhenNormalCharacter_ThenMatchPrefixCode() throws IOException{
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        Node l1=new Node(66,1);
        Node l2=new Node(97,1);
        Node l3=new Node(256,1);
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(l2,p1);

        String[] expectedPrefixCode=new String[257];
        expectedPrefixCode[66]="10";
        expectedPrefixCode[97]="0";
        expectedPrefixCode[256]="11";

         IHashMap hashMap= characterBasedHuffmanCompresser.generatePrefixCode(rootNode);
         for(int i=0;i<expectedPrefixCode.length;i++){
             if(expectedPrefixCode[i]!=null){
                 assertEquals(expectedPrefixCode[i],hashMap.get(i));
             }
         }
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsNull_ThenReturnEmptyArray() throws IOException {
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();

        IHashMap hashMap= characterBasedHuffmanCompresser.generatePrefixCode(null);
        for(int i=0;i<257;i++){
                assertEquals(null,hashMap.get(i));
        }
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleNonLeafNode_ThenPrefixCodeEmpty() throws IOException {
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        Node rootNode=new Node(null,null);


        IHashMap hashMap= characterBasedHuffmanCompresser.generatePrefixCode(rootNode);
        for(int i=0;i<257;i++){
                assertEquals(null,hashMap.get(i));
        }
    }


    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleLeafNode_ThenPrefixCodeMatch() throws IOException {
        characterBasedHuffmanCompresser =new CharacterBasedHuffmanCompresser();
        Node rootNode=new Node(97,1);

        String[] expectedHashCode=new String[257];
        expectedHashCode[97]="0";

        IHashMap hashMap= characterBasedHuffmanCompresser.generatePrefixCode(rootNode);
        for(int i=0;i<expectedHashCode.length;i++){
            if(expectedHashCode[i]!=null){
                assertEquals(expectedHashCode[i],hashMap.get(i));
            }
        }
    }

    @Test
    public void encodeFile_WhenNormalCharacters_ThenCompressedFilesMatch() throws Exception {

        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        Node rootNode=new Node("",0);

        IHashMap hashMap=new HashMapImpl();


        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(1);

        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
        doReturn(true).when(headerInfoReaderWriter).writeHeaderInfo(any(Node.class),any(ByteOutputStream.class));


        ICompressedFileReaderWriter compressedFileReaderWriter = spy(new CompressedWordFileReaderWriterImpl());
        doReturn(true).when(compressedFileReaderWriter).writeCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(IHashMap.class));

        IHuffmanCompresser huffmanCompresser = new CharacterBasedHuffmanCompresser(headerInfoReaderWriter,compressedFileReaderWriter);

        assertTrue(huffmanCompresser.encodeFile(inputStream,outputStream,hashMap,rootNode));

    }



    @Test
    public void encodeFile_WhenIHeaderReaderWriterReturnsFalse_ThenThrowException() throws Exception {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Invalid huffman tree");


        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        Node rootNode=new Node("",0);

        IHashMap hashMap=new HashMapImpl();


        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(1);

        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
        doReturn(false).when(headerInfoReaderWriter).writeHeaderInfo(any(Node.class),any(ByteOutputStream.class));


        ICompressedFileReaderWriter compressedFileReaderWriter = spy(ICompressedFileReaderWriter.class);
        doReturn(true).when(compressedFileReaderWriter).writeCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(IHashMap.class));

        IHuffmanCompresser huffmanCompresser = new CharacterBasedHuffmanCompresser(headerInfoReaderWriter,compressedFileReaderWriter);

        huffmanCompresser.encodeFile(inputStream,outputStream,hashMap,rootNode);

    }

    @PrepareForTest(CharacterBasedHuffmanCompresser.class)
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

        IHuffmanCompresser huffmanCompresser = new CharacterBasedHuffmanCompresser(headerInfoReaderWriter,compressedFileReaderWriter);

        huffmanCompresser.encodeFile(inputStream,outputStream,hashMap,rootNode);

    }


    }