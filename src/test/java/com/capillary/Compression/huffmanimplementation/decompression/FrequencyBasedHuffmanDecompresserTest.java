package com.capillary.Compression.huffmanimplementation.decompression;

import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FrequencyBasedHuffmanDecompresserTest {

    FrequencyBasedHuffmanDecompresser frequencyBasedHuffmanDecompresser;

    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    Boolean dfs(Node root1, Node root2) {
        if (root1 == null && root2 == null) {
            return true;
        }
        if((root1.value!=null && root2.value!=null) && (!root1.value.equals(root2.value))){
            return false;
        }
        Boolean result = true;
//        System.out.println(root1.value);
        if (root1.left != null) {
            if (root2.left == null) {
                result = result && false;
            } else {
                result = result && dfs(root1.left, root2.left);
            }
        }
        if (root1.right != null) {
            if (root2.right == null) {
                result = result && false;
            } else {
                result = result && dfs(root1.right, root2.right);
            }
        }

        return result;
    }

    void dfs(Node root){
        if(root==null){
            return;
        }
        System.out.println(root.value);
        if(root.left!=null){
            dfs(root.left);
        }
        if(root.right!=null){
            dfs(root.right);
        }
    }

    @Test
    public void createHuffmanTree_WhenNormalEncodedFile_ThenHuffmanTreeMatch() throws IOException {

        byte[] fileInputByteArray={76,41,11,0,88};
        InputStream inputStream = new ByteArrayInputStream(fileInputByteArray);

        frequencyBasedHuffmanDecompresser = new FrequencyBasedHuffmanDecompresser();

        Node l1 = new Node(66, 1);
        Node l2 = new Node(97, 1);
        Node l3 = new Node(256, 1);
        Node p1 = new Node(l1, l3);
        Node expectedRoot = new Node(l2, p1);
//        dfs(frequencyBasedHuffmanDecompresser.rootNode);
        assertTrue(dfs(expectedRoot, frequencyBasedHuffmanDecompresser.createHuffmanTree(inputStream)));

    }


    @Test
    public void createHuffmanTree_WhenInputStreamNull_ThenCatchException() throws IOException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("inputStream is null");
        frequencyBasedHuffmanDecompresser = new FrequencyBasedHuffmanDecompresser();

        frequencyBasedHuffmanDecompresser.createHuffmanTree(null);
    }

    @Test
    public void createHuffmanTree_WhenIncorrectHeader_Then() throws IOException {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Incorrect Header");
        byte[] fileInputByteArray={6,2}; //aB
        InputStream inputStream = new ByteArrayInputStream(fileInputByteArray);

        frequencyBasedHuffmanDecompresser = new FrequencyBasedHuffmanDecompresser();
        frequencyBasedHuffmanDecompresser.createHuffmanTree(inputStream);
    }

        @Test
    public void decodeFile_WhenNormalEncodedFile_ThenDecompressedFileMatch() throws IOException {


        byte[] fileInputByteArray={76,41,11,0,88}; //aB
        InputStream inputStream = new ByteArrayInputStream(fileInputByteArray);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);

        frequencyBasedHuffmanDecompresser = new FrequencyBasedHuffmanDecompresser();

        assertTrue(frequencyBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,frequencyBasedHuffmanDecompresser.createHuffmanTree(inputStream)));

        String decompressedFile="aB";
        byte[] decompressedFileByteArray=decompressedFile.getBytes(Charset.forName("UTF-8"));
        byte[] compressFileArray = (byteArrayOutputStream.toByteArray());

        assertEquals(compressFileArray.length,decompressedFileByteArray.length);
        assertTrue(Arrays.equals(compressFileArray,decompressedFileByteArray));
    }

    @Test
    public void decodeFile_WhenInputStreamIsNull_ThenReturnNull() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        frequencyBasedHuffmanDecompresser = new FrequencyBasedHuffmanDecompresser();
        Node l1 = new Node(66, 1);
        Node l2 = new Node(97, 1);
        Node l3 = new Node(256, 1);
        Node p1 = new Node(l1, l3);
        Node rootNode = new Node(l2, p1);
        assertFalse(frequencyBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,rootNode));
    }

    @Test
    public void decodeFile_WhenRootNodeIsNull_ThenReturnNull() throws IOException {

        byte[] fileInputByteArray={76,41,11,0,88};
        InputStream inputStream = new ByteArrayInputStream(fileInputByteArray);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        frequencyBasedHuffmanDecompresser = new FrequencyBasedHuffmanDecompresser();
        frequencyBasedHuffmanDecompresser.createHuffmanTree(inputStream);
        assertFalse(frequencyBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,null));
    }


    }