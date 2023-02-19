package com.capillary.Compression.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ByteInputStreamTest {


    ByteInputStream byteInputStream;
    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Test
    public void inputStream_WhenInputStreamNull_ThenThrowNullPointerException() throws IOException{
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("inputStream is null");
        byteInputStream =new ByteInputStream(null);
    }

    @Test
    public void inputStream_WhenFileIsEmpty_ThenThrowIOException() throws IOException {
    expectedException.expect(IOException.class);
    expectedException.expectMessage("File is empty");
        String input="";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        byteInputStream =new ByteInputStream(byteStream);
    }



        @Test
    public void loadBuffer_WhenNormalFile_ThenMatchBuffer() throws IOException {
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        byteInputStream =new ByteInputStream(byteStream);
        byteInputStream.loadBuffer();
        assertEquals(97, byteInputStream.buffer);
    }

    @Test
    public void loadBuffer_WhenInputStreamClosed_ThenCatchIOException() throws IOException {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("InputStream is closed");
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        byteInputStream =new ByteInputStream(byteStream);
        byteInputStream.close();
        byteInputStream.loadBuffer();
        System.out.println(byteInputStream.buffer);
    }

    @Test
    public void getBit_WhenNormalStream_ThenMatchBit() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        byteInputStream =new ByteInputStream(byteStream);
        System.out.println(byteInputStream.buffer);
        assertEquals(0, byteInputStream.getBit());
//        System.out.println(inputStream.buffer);
    }

    @Test
    public void getBit_WhenEOFReached_ThenReturnValueMatch() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        byteInputStream =new ByteInputStream(byteStream);
//        System.out.println(inputStream.buffer);
        byteInputStream.getByte();

        assertEquals(-1, byteInputStream.getBit());
    }

    @Test
    public void getBits_WhenNormalInputStream_ThenReturnBitsMatch() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        byteInputStream =new ByteInputStream(byteStream);

        assertEquals(1, byteInputStream.getBits(2));
    }

    @Test
    public void getByte_WhenNormalStream_ThenByteMatch() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        byteInputStream =new ByteInputStream(byteStream);
        assertEquals(97, byteInputStream.getByte());
    }

    @Test
    public void getByte_WhenInputStreamClosed_ThenCatchIoException() throws IOException{
        expectedException.expect(IOException.class);
        expectedException.expectMessage("InputStream is closed");
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        byteInputStream =new ByteInputStream(byteStream);
        byteInputStream.close();
        byteInputStream.getByte();
    }

    @Test
    public void close_WhenNormalInputStream_ThenCloseTheStream() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        byteInputStream =new ByteInputStream(byteStream);
        byteInputStream.close();
    }
}