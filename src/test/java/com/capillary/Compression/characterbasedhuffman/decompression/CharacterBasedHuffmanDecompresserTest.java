package com.capillary.Compression.characterbasedhuffman.decompression;

import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class CharacterBasedHuffmanDecompresserTest {

    CharacterBasedHuffmanDecompresser characterBasedHuffmanDecompresser;

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
    public void createHuffmanTree_WhenValidHeader_ThenReturnNode()throws IOException {

        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        Node node=new Node();
        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
        doReturn(node).when(headerInfoReaderWriter).readHeaderInfo(any(ByteInputStream.class));

        characterBasedHuffmanDecompresser=new CharacterBasedHuffmanDecompresser(headerInfoReaderWriter,null);

        assertEquals(node,characterBasedHuffmanDecompresser.createHuffmanTree(inputStream));
    }

    @Test
    public void decodeFile_WhenValidCompressedFile_ThenReturnTrue() throws IOException{

        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        Node node=new Node();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ICompressedFileReaderWriter compressedFileReaderWriter= spy(ICompressedFileReaderWriter.class);
        doReturn(true).when(compressedFileReaderWriter).readCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(Node.class));

        characterBasedHuffmanDecompresser=new CharacterBasedHuffmanDecompresser(new ByteInputStream(inputStream),null,compressedFileReaderWriter);

        assertEquals(true,characterBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,node));
    }

    @Test
    public void decodeFile_WhenNodeIsNull_ThenReturnFalse() throws IOException{
        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));
        Node node=new Node();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ICompressedFileReaderWriter compressedFileReaderWriter= spy(ICompressedFileReaderWriter.class);
        doReturn(true).when(compressedFileReaderWriter).readCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(Node.class));

        characterBasedHuffmanDecompresser=new CharacterBasedHuffmanDecompresser(new ByteInputStream(inputStream),null,compressedFileReaderWriter);

        assertEquals(false,characterBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,null));

    }


    @Test
    public void decodeFile_WhenByteInputStreamIsNull_ThenReturnFalse() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ICompressedFileReaderWriter compressedFileReaderWriter= spy(ICompressedFileReaderWriter.class);
        doReturn(true).when(compressedFileReaderWriter).readCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(Node.class));

        characterBasedHuffmanDecompresser=new CharacterBasedHuffmanDecompresser(null,null,compressedFileReaderWriter);

        assertEquals(false,characterBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,null));

    }

    }