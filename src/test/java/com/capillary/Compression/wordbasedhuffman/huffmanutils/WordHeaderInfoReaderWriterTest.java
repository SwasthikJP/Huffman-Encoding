package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.Node;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class WordHeaderInfoReaderWriterTest {

    IHeaderInfoReaderWriter headerInfoReaderWriter;
    @Rule
    public ExpectedException expectedException=ExpectedException.none();


    Boolean dfs(Node root1, Node root2){
        if(root1==null && root2==null){
            return true;
        }
        if((root1.value!=null && root2.value!=null) && (!root1.value.equals(root2.value)) ){
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
    public void readHeaderInfo_WhenValidHeader_ThenTreeMatch() throws IOException{
        headerInfoReaderWriter=new WordHeaderInfoReaderWriter();

        byte[] headerByteStream={32,36,16,18,-14,6,-10,-68,-5,2,97,66,64,88,96,38,32};
        InputStream inputStream=new ByteArrayInputStream(headerByteStream);
        ByteInputStream byteInputStream=new ByteInputStream(inputStream);


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

//        dfs(headerInfoReaderWriter.readHeaderInfo(byteInputStream));
        assertTrue(dfs(rootNode,headerInfoReaderWriter.readHeaderInfo(byteInputStream)));
    }

    @Test
    public void readHeaderInfo_WhenInCorrectHeader_ThenThrowException() throws IOException{
     expectedException.expect(IOException.class);
     expectedException.expectMessage("Incorrect Header");
        headerInfoReaderWriter=new WordHeaderInfoReaderWriter();

        byte[] headerByteStream={32,36,16,18,-14,6,-10,-68,-5,2,97,66,64,88};
        InputStream inputStream=new ByteArrayInputStream(headerByteStream);
        ByteInputStream byteInputStream=new ByteInputStream(inputStream);


        headerInfoReaderWriter.readHeaderInfo(byteInputStream);

    }

    @Test
    public void writeHeaderInfo_WhenValidHeader_ThenMatchByteStream() throws IOException {
        headerInfoReaderWriter=new WordHeaderInfoReaderWriter();

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1000);
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

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

       assertTrue(headerInfoReaderWriter.writeHeaderInfo(rootNode,byteOutputStream));
       byteOutputStream.closeStream();
       byte[] headerByteStream=byteArrayOutputStream.toByteArray();
        byte[] expectedByteStream={32,36,16,18,-14,6,-10,-68,-5,2,97,66,64,88,96,38,32};
        assertEquals(headerByteStream.length,expectedByteStream.length);
        assertArrayEquals(headerByteStream,expectedByteStream);

    }

    @Test
    public void writeHeaderInfo_WhenTreeNodeIsNUll_ThenReturnFalse() throws IOException {
        headerInfoReaderWriter=new WordHeaderInfoReaderWriter();

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1000);
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);
        assertFalse(headerInfoReaderWriter.writeHeaderInfo(null,byteOutputStream));
    }

}