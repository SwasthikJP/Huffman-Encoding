package com.capillary.Compression;

import com.capillary.Compression.huffmanimplementation.Node;
import com.capillary.Compression.huffmanimplementation.huffmancompression.FrequencyBasedHuffmanCompresser;
import com.capillary.Compression.huffmanimplementation.huffmancompression.IHuffmanCompresser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.io.InputStream;
import java.nio.charset.Charset;
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

        int[] result=new int[257];
        result[97]=2;
        result[66]=1;
        result[32]=1;
        result[49]=1;
        result[47]=1;
        result[256]=1;

        assertArrayEquals(result, frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream));

    }

    @Test
    public void calculateCharacterFrequency_WhenCharactersUNICODE_ThenCountShouldMatch() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
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
        assertArrayEquals(result, frequencyBasedHuffmanCompresser.calculateCharacterFrequency(inputStream));

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
        int[] characterFrequency=new int[257];
        characterFrequency[97]=1;
        characterFrequency[66]=1;
        characterFrequency[256]=1;

        Node l1=new Node(66,1);
        Node l2=new Node(97,1);
        Node l3=new Node(256,1);
        Node p1=new Node(l1,l3);
        Node expectedRoot=new Node(l2,p1);
        assertEquals(true, dfs(expectedRoot, frequencyBasedHuffmanCompresser.createHuffmanTree(characterFrequency)));
    }

    @Test
    public void createHuffmanTree_WhenFrequencyMapIsEmpty_ThenRootNodeIsNull() throws IOException{
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        int[] characterFrequency=new int[257];

        assertEquals(null,frequencyBasedHuffmanCompresser.createHuffmanTree(characterFrequency));

    }



    @Test
    public void generatePrefixCode_WhenNormalCharacter_ThenMatchPrefixCode() throws IOException{
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
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
        assertArrayEquals(expectedPrefixCode,frequencyBasedHuffmanCompresser.generatePrefixCode(rootNode));
    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsNull_ThenReturnEmptyArray() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        assertArrayEquals(new String[257],frequencyBasedHuffmanCompresser.generatePrefixCode(null));

    }

    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleNonLeafNode_ThenPrefixCodeEmpty() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        Node rootNode=new Node(null,null);

        String[] expectedPrefixCode=new String[257];
        assertArrayEquals(expectedPrefixCode, frequencyBasedHuffmanCompresser.generatePrefixCode(rootNode));
    }


    @Test
    public void generatePrefixCode_WhenRootNodeIsSingleLeafNode_ThenPrefixCodeMatch() throws IOException {
        frequencyBasedHuffmanCompresser=new FrequencyBasedHuffmanCompresser();
        Node rootNode=new Node(97,1);

        String[] expectedHashCode=new String[257];
        expectedHashCode[97]="0";
        assertArrayEquals(expectedHashCode, frequencyBasedHuffmanCompresser.generatePrefixCode(rootNode));
    }


    @Test
    public void encodeFile_WhenNormalCharacters_ThenCompressedFilesMatch() throws IOException{

        IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        String[] hashCode=new String[257];
        hashCode[97]="0";
        hashCode[66]="10";
        hashCode[256]="11";

        Node l1=new Node(66,1);
        Node l2=new Node(97,1);
        Node l3=new Node(256,1);
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(l2,p1);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
       assertTrue(huffmanCompresser.encodeFile(inputStream,byteArrayOutputStream,hashCode,rootNode));

            byte[] compressFileArray=byteArrayOutputStream.toByteArray();
            byte[] expectedFileArray={76,41,11,0,88};
        assertEquals(compressFileArray.length,expectedFileArray.length);
        assertTrue(Arrays.equals(compressFileArray,expectedFileArray));

    }

    @Test
    public void encodeFile_WhenRootNodeIsNull_ThenThrowNullPointerException() throws IOException {
      expectedException.expect(NullPointerException.class);
      expectedException.expectMessage("Root node is null");
        IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();
        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);

        String[] hashCode=new String[257];
        hashCode[97]="0";



        huffmanCompresser.encodeFile(inputStream,byteArrayOutputStream,hashCode,null);
    }

    @Test
    public  void encodeFile_WhenFileIsEmpty_ThenThrowIOException() throws IOException{
        expectedException.expect(IOException.class);
        expectedException.expectMessage("File is empty");
        IHuffmanCompresser huffmanCompresser = new FrequencyBasedHuffmanCompresser();
        String fileInput="";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        huffmanCompresser.encodeFile(inputStream,byteArrayOutputStream,null,null);
    }


    }