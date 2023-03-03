package com.capillary.zipper.wordbasedhuffman.huffmanutils;

import com.capillary.zipper.utils.ByteInputStream;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ChecksumTest {

    Checksum checksum;
    @Test
    public<T> void calcCheckSum_WhenValidInputStream_ThenCheckSumMatch() throws Exception {
       checksum=new Checksum();
        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));



        List<T> expected=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte) 74, (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));

        ByteInputStream byteInputStream=new ByteInputStream(inputStream);
      List<T> list=checksum.calcCheckSum(byteInputStream);
      for(int i=0;i<list.size();i++){
          assertEquals( (expected.get(i)),(list.get(i)));
      }
    }



    @Test
    public<T> void calcCheckSum_WhenInputStreamIsNull_ThenCheckSumMatch() throws Exception {
        checksum=new Checksum();
        List<T> expected=new ArrayList<>();

        List<T> list=checksum.calcCheckSum(null);
        assertTrue(expected.equals(list));
    }

    @Test
    public <T> void writeCheckSum_WhenValidCheckSum_ThenOutputStreamMatch()throws IOException {
        checksum=new Checksum();
        List<T> expected=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte) 74, (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        checksum.writeCheckSum(expected,outputStream);
        byte[] result= outputStream.toByteArray();
        assertEquals(expected.size(),result.length);
        for(int i=0;i<result.length;i++){
            assertEquals(expected.get(i),result[i]);
        }
    }

    @Test
    public <T> void writeCheckSum_WhenOutputStreamNull_ThenOutputStreamMatch()throws IOException {
        checksum=new Checksum();
        List<T> expected=new ArrayList<>();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        checksum.writeCheckSum(expected,null);
        byte[] result= outputStream.toByteArray();
        assertEquals(0,result.length);
    }

    @Test
    public <T> void readCheckSum_WhenValidCheckSum_ThenListMatch()throws IOException {
        checksum=new Checksum();
        List<T> expected=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte) 74, (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));

        byte[] input={74, 28,115,-68,92,26,-86,111,18,99,35,-111,78,16,18,89};
        ByteArrayInputStream inputStream=new ByteArrayInputStream(input);
      List<T> result=checksum.readCheckSum(inputStream);

        assertEquals(expected.size(),result.size());
        for(int i=0;i<result.size();i++){
            assertEquals(expected.get(i),result.get(i));
        }
    }

    @Test
    public <T> void readCheckSum_WhenInputStreamNull_ThenListMatch()throws IOException {
        checksum=new Checksum();

        List<T> result=checksum.readCheckSum(null);

        assertEquals(0,result.size());

    }

    @Test
    public<T> void validateCheckSum_WhenIdentiticalCheckSum_ThenReturnTrue() {
        checksum=new Checksum();
        List<T> checkSum1=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte) 74, (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));
        List<T> checkSum2=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte) 74, (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));

       assertTrue(checksum.validateCheckSum(checkSum1,checkSum2));
    }

    @Test
    public<T> void validateCheckSum_WhenNonIdentiticalCheckSum_ThenReturnFalse() {
        checksum=new Checksum();
        List<T> checkSum1=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));
        List<T> checkSum2=new ArrayList<>((Collection<? extends T>) Arrays.asList( (byte) 74, (byte)28,(byte)115,(byte)-68,(byte)92,(byte)26,(byte)-86,(byte)111,(byte)18,(byte)99,(byte)35,(byte)-111,(byte)78,(byte)16,(byte)18,(byte)89));

        assertFalse(checksum.validateCheckSum(checkSum1,checkSum2));
    }
}