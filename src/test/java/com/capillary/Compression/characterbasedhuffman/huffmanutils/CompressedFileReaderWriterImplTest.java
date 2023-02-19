package com.capillary.Compression.characterbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.HashMapImpl;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class CompressedFileReaderWriterImplTest {

    ICompressedFileReaderWriter compressedFileReaderWriter;

    @Test
    public void readCompressedFile_WhenValidTreeAndContentWithPadding_ThenByteStreamMatch() throws IOException{
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        byte[] fileInput={88};//byte with 3bit padding
        InputStream inputStream = new ByteArrayInputStream(fileInput);

        Node l1=new Node(66,1);//B
        Node l2=new Node(97,1);//a
        Node l3=new Node(256,1);//eof
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(l2,p1);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertTrue(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,rootNode));
        byteInputStream.close();
        byteOutputStream.closeStream();

        byte[] result= byteArrayOutputStream.toByteArray();
        byte[] expected={97,66};//aB

        assertArrayEquals(expected,result);
    }

    @Test
    public void readCompressedFile_WhenValidTreeAndContentNoPadding_ThenByteStreamMatch2() throws IOException{
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        byte[] fileInput={11};//equals complete byte with no padding
        InputStream inputStream = new ByteArrayInputStream(fileInput);

        Node l1=new Node(66,1);//B
        Node l2=new Node(97,1);//a
        Node l3=new Node(256,1);//eof
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(l2,p1);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertTrue(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,rootNode));
        byteInputStream.close();
        byteOutputStream.closeStream();

        byte[] result= byteArrayOutputStream.toByteArray();
        byte[] expected={97,97,97,97,66};//aaaaB

        assertArrayEquals(expected,result);
    }

    @Test
    public void readCompressedFile_WhenInValidTree_ThenReturnFalse() throws IOException{
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        byte[] fileInput={88};
        InputStream inputStream = new ByteArrayInputStream(fileInput);

        Node l1=new Node(66,1);//B
        Node l2=new Node(97,1);//a
        Node l3=new Node(256,1);//eof
        Node p1=new Node(l1,l3);
        Node rootNode=new Node(null,p1);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,rootNode));
        byteInputStream.close();
        byteOutputStream.closeStream();

    }


    @Test
    public void readCompressedFile_WhenTreeIsNull_ThenReturnFalse() throws IOException{
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        byte[] fileInput={88};
        InputStream inputStream = new ByteArrayInputStream(fileInput);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,null));
        byteInputStream.close();
        byteOutputStream.closeStream();

    }



    @Test
    public void writeCompressedFile_WhenValidHashMap_ThenByteStreamMatch() throws IOException {
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        IHashMap hashMap=new ArrayBasedHashMap();
        hashMap.put(97,"0");//a
        hashMap.put(66,"10");//B
        hashMap.put(256,"11");//eof

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertTrue(compressedFileReaderWriter.writeCompressedFile(byteInputStream,byteOutputStream,hashMap));
        byteInputStream.close();
        byteOutputStream.closeStream();

        byte[] result= byteArrayOutputStream.toByteArray();
        byte[] expected={88};

        assertArrayEquals(expected,result);
    }


    @Test
    public void writeCompressedFile_WhenInValidHashMap_ThenReturnFalse() throws IOException {
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        String fileInput="aB";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        IHashMap hashMap=new ArrayBasedHashMap();
        hashMap.put(97,"0");//a
        hashMap.put(256,"11");//eof

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(compressedFileReaderWriter.writeCompressedFile(byteInputStream,byteOutputStream,hashMap));
        byteInputStream.close();
        byteOutputStream.closeStream();
    }


}