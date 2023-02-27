package com.capillary.zipper.characterbasedhuffman.huffmanutils;

import com.capillary.zipper.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;
import com.capillary.zipper.utils.Node;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class PreorderHeaderInfoReaderWriterTest {

    IHeaderInfoReaderWriter headerInfoReaderWriter;

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
    public void readHeaderInfo_WhenValidHeader_ThenHuffmanTreeMatch()throws IOException {
        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();


        Node l1=new Node(66,1);
        Node l2=new Node(97,1);
        Node l3=new Node(256,1);
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(l2,p1);

        byte[] header={76,41,11,0};

        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(header);
        ByteInputStream byteInputStream=new ByteInputStream(byteArrayInputStream);


        assertTrue(dfs(rootNode,headerInfoReaderWriter.readHeaderInfo(byteInputStream)));

    }



    @Test
    public void readHeaderInfo_WhenInValidHeader_ThenThrowException()throws IOException {

     expectedException.expect(IOException.class);
     expectedException.expectMessage("Incorrect Header");
        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();

        byte[] inValidHeader={76,41};

        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(inValidHeader);
        ByteInputStream byteInputStream=new ByteInputStream(byteArrayInputStream);


       headerInfoReaderWriter.readHeaderInfo(byteInputStream);

    }

    @Test
    public void writeHeaderInfo_WhenValidTree_ThenByteStreamMatch() throws IOException {
        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();

//        IHashMap hashMap=new ArrayBasedHashMap();
//        hashMap.put(97,"0");//a
//        hashMap.put(66,"10");//B
//        hashMap.put(256,"11");//eof

        Node l1=new Node(66,1);//B
        Node l2=new Node(97,1);//a
        Node l3=new Node(256,1);//eof
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(l2,p1);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

     assertTrue(headerInfoReaderWriter.writeHeaderInfo(rootNode,byteOutputStream));

        byteOutputStream.closeStream();
        byte[] result=byteArrayOutputStream.toByteArray();
        byte[] expected={76,41,11,0};

        assertArrayEquals(result,expected);

    }



    @Test
    public void writeHeaderInfo_WhenInValidTree_ThenReturnFalse() throws IOException {

        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();

        Node l2=new Node(97,1);//a
        Node l3=new Node(256,1);//eof
        Node p1=new Node(null,l3);
        Node rootNode=new Node(l2,p1);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(headerInfoReaderWriter.writeHeaderInfo(rootNode,byteOutputStream));

    }


    @Test
    public void writeHeaderInfo_WhenTreeIsNull_ThenReturnFalse() throws IOException {

        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(headerInfoReaderWriter.writeHeaderInfo(null,byteOutputStream));

    }



}