package com.capillary.Compression.wordbasedhuffman.huffmanutils;

import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.Node;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class CompressedWordFileReaderWriterImplTest {

    ICompressedFileReaderWriter compressedFileReaderWriter;

    @Test
    public void readCompressedFile_WhenValidTreeAndContentWithNoPadding_ThenByteStreamMatch() throws IOException{
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        String fileInput="aB 1/a";
        byte[] fileContent={-89,116};//aB 1/a{^} bytes with no padding
//        byte[] fileContent="t".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(fileContent);


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

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

      assertTrue(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,rootNode));
        byteInputStream.close();
        byteOutputStream.closeStream();

        byte[] deCompressedByte=byteArrayOutputStream.toByteArray();
        byte[] expectedByte={97,66,32,49,47,97};

        assertEquals(expectedByte.length,deCompressedByte.length);
        assertArrayEquals(expectedByte,deCompressedByte);
    }

    @Test
    public void readCompressedFile_WhenValidTreeAndContentWithPadding_ThenByteStreamMatch2() throws IOException{
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        String fileInput="aB 1/a";
        byte[] fileContent={-89,-48};//aB 1a{^}  bytes with 2bit padding
//        byte[] fileContent="t".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(fileContent);


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

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertTrue(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,rootNode));
        byteInputStream.close();
        byteOutputStream.closeStream();

        byte[] deCompressedByte=byteArrayOutputStream.toByteArray();
        byte[] expectedByte={97,66,32,49,97};

        assertEquals(expectedByte.length,deCompressedByte.length);
        assertArrayEquals(expectedByte,deCompressedByte);
    }

    @Test
    public void readCompressedFile_WhenInValidTree_ThenReturnFalse() throws IOException{
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        String fileInput="aB 1/a";
        byte[] fileContent={-89,116};//aB 1/a{^}
//        byte[] fileContent="t".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(fileContent);


        Node w1=new Node("a",1);
        Node w2=new Node("{^}",1);
        Node w3=new Node("aB",1);
        Node w4=new Node(" ",1);
        Node w5=new Node("/",1);
        Node w6=new Node("1",1);

        Node p1=new Node(w4,w5);
        Node p3=new Node(w2,w3);
        Node p4=new Node(w1,null);
        Node p2=new Node(p3,p4);
        Node rootNode=new Node(p1,p2);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,rootNode));
    }



    @Test
    public void readCompressedFile_WhenTreeNodeIsNull_ThenReturnFalse() throws IOException{
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        String fileInput="aB 1/a";
        byte[] fileContent={-89,116};//aB 1/a
//        byte[] fileContent="t".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);

        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);


        assertFalse(compressedFileReaderWriter.readCompressedFile(byteInputStream,byteOutputStream,null));
    }






    @Test
    public void writeCompressedFile_WhenNormalWords_ThenCompressedBytesMatch() throws IOException {
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        IHashMap hashMap=new HashMapImpl();

        hashMap.put(" ","00");
        hashMap.put("/","01");
        hashMap.put("{^}","100");
        hashMap.put("aB","101");
        hashMap.put("a","110");
        hashMap.put("1","111");

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1000);

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertTrue(compressedFileReaderWriter.writeCompressedFile(byteInputStream,byteOutputStream,hashMap));
        byteInputStream.close();
        byteOutputStream.closeStream();

        byte[] compressFileArray=byteArrayOutputStream.toByteArray();
        byte[] expectedFileArray={-89,116};//167,116 �t
        for(byte b:compressFileArray)
            System.out.println(b);
       // byte[] expectedFileArray={32,36,16,18,-14,6,-10,-68,-5,2,97,66,64,88,96,38,52,-18,-128};

        assertEquals(expectedFileArray.length,compressFileArray.length);
        assertArrayEquals(compressFileArray,expectedFileArray);

    }


    @Test
    public void writeCompressedFile_WhenIncorrectHashMap_ThenReturnFalse() throws IOException {

        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));


        IHashMap hashMap=new HashMapImpl();

        hashMap.put(" ","00");
        hashMap.put("/","01");
        hashMap.put("aB","101");
        hashMap.put("a","110");
        hashMap.put("1","111");

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1000);

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);
        ByteOutputStream byteOutputStream=new ByteOutputStream(byteArrayOutputStream);

        assertFalse(compressedFileReaderWriter.writeCompressedFile(byteInputStream,byteOutputStream,hashMap));
        byteInputStream.close();
        byteOutputStream.closeStream();



    }


}