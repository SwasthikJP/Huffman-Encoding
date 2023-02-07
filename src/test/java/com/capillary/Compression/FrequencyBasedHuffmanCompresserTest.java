package com.capillary.Compression;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FrequencyBasedHuffmanCompresserTest {

    FrequencyBasedHuffmanCompresser frequencyBasedHuffmanCompresser;


    @Test
    public void calculateCharacterFrequency_WhenCharactersASCII_ThenCountShouldMatch() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);
        int[] result=new int[257];
        result[97]=2;
        result[66]=1;
        result[32]=1;
        result[49]=1;
        result[47]=1;
        result[256]=1;

        assertArrayEquals(result, frequencyBasedHuffmanCompresser.characterFrequency);

    }

    @Test
    public void calculateCharacterFrequency_WhenCharactersUNICODE_ThenCountShouldMatch() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        String fileInput="✅♨\uD83D\uDE80";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
//        frequencyBasedHuffmanCompresser.calculateCharacterFrequency("FrequencyTestUnicode.txt");
        frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);

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
        assertArrayEquals(result, frequencyBasedHuffmanCompresser.characterFrequency);

    }


    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    @Test
    public  void calculateCharacterFrequency_WhenFileIsEmpty_ThenThrowFileIsEmptyExceptionMessage() throws IOException{
        expectedException.expect(IOException.class);
        expectedException.expectMessage("File is empty");
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        String fileInput="";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);
    }


    Boolean dfs(Node root1, Node root2){
        if(root1==null && root2==null){
            return true;
        }
        if(root1.value!=root2.value || root1.frequency!=root2.frequency){
            return false;
        }
        Boolean result=true;
//        System.out.println(root1.value);
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
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);
        frequencyBasedHuffmanCompresser.createHuffmanTree();
        Node testRoot=frequencyBasedHuffmanCompresser.rootNode;
        Node l1=new Node(66,1);
        Node l2=new Node(97,1);
        Node l3=new Node(256,1);
        Node p1=new Node(l1,l3);
        Node expectedRoot=new Node(l2,p1);
        assertEquals(true, dfs(expectedRoot, testRoot));
    }

    @Test
    public void createHuffmanTree_WhenFrequencyMapIsEmpty_ThenRootNodeIsNull() throws IOException{
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        frequencyBasedHuffmanCompresser.createHuffmanTree();
        assertEquals(null,frequencyBasedHuffmanCompresser.rootNode);

    }



    @Test
    public void generatePrefixCode_WhenNormalCharacter_ThenMatchPrefixCode() throws IOException{
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream);
        frequencyBasedHuffmanCompresser.createHuffmanTree();
        frequencyBasedHuffmanCompresser.generatePrefixCode();
        String[] expectedPrefixCode=new String[257];
        expectedPrefixCode[66]="10";
        expectedPrefixCode[97]="0";
        expectedPrefixCode[256]="11";
        assertArrayEquals(expectedPrefixCode, frequencyBasedHuffmanCompresser.huffmanCode);
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsNull_ThenPrefixCodeEmpty() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        frequencyBasedHuffmanCompresser.generatePrefixCode();
        String[] expectedPrefixCode=new String[257];
        assertArrayEquals(expectedPrefixCode, frequencyBasedHuffmanCompresser.huffmanCode);

    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleNonLeafNode_ThenPrefixCodeEmpty() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        Node root=new Node(null,null);
        frequencyBasedHuffmanCompresser.rootNode=root;
        frequencyBasedHuffmanCompresser.generatePrefixCode();
        String[] expectedPrefixCode=new String[257];
        assertArrayEquals(expectedPrefixCode, frequencyBasedHuffmanCompresser.huffmanCode);
    }


    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleLeafNode_ThenPrefixCodeMatch() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        Node root=new Node(97,1);
        frequencyBasedHuffmanCompresser.rootNode=root;
        frequencyBasedHuffmanCompresser.generatePrefixCode();
        String[] expectedPrefixCode=new String[257];
        expectedPrefixCode[97]="0";
        assertArrayEquals(expectedPrefixCode, frequencyBasedHuffmanCompresser.huffmanCode);
    }


    @Test
    public void encodeFile_WhenNormalCharacters_ThenCompressedFilesMatch() throws IOException{

        IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        huffmanCompresser.calculateCharacterFrequency(inputStream);
        huffmanCompresser.createHuffmanTree();
        huffmanCompresser.generatePrefixCode();

        InputStream inputStream2 = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
       String  compressFilePath = huffmanCompresser.encodeFile(inputStream2,"createHuffmanTreeNormal.huf.txt");
       String expectedFilePath="ExpectedcreateHuffmanTreeNormal.huf.txt";

           File compressFile=new File(compressFilePath);
           File expectedFile=new File(expectedFilePath);

              assertTrue(compressFile.length()== expectedFile.length());
            byte[] compressFileArray=(new FileInputStream(compressFilePath).readAllBytes());
            byte[] expectedFileArray=(new FileInputStream(expectedFilePath).readAllBytes());
           assertTrue(Arrays.equals(compressFileArray,expectedFileArray));

    }

//    @Test
//    public void encodeFile_WhenRootNodeIsNull_ThenCompressedFilesMatch() throws IOException {
//
//    }


    }