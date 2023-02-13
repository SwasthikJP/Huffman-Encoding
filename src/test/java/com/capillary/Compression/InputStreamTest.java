package com.capillary.Compression;

import com.capillary.Compression.utils.InputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class InputStreamTest {


    InputStream inputStream;
    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Test
    public void inputStream_WhenInputStreamNull_ThenThrowNullPointerException() throws IOException{
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("inputStream is null");
        inputStream=new InputStream(null);
    }

    @Test
    public void inputStream_WhenFileIsEmpty_ThenThrowIOException() throws IOException {
    expectedException.expect(IOException.class);
    expectedException.expectMessage("File is empty");
        String input="";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        inputStream=new InputStream(byteStream);
    }



        @Test
    public void loadBuffer_WhenNormalFile_ThenMatchBuffer() throws IOException {
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        inputStream=new InputStream(byteStream);
        inputStream.loadBuffer();
        assertEquals(97,inputStream.buffer);
    }

    @Test
    public void loadBuffer_WhenInputStreamClosed_ThenCatchIOException() throws IOException {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("InputStream is closed");
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        inputStream=new InputStream(byteStream);
        inputStream.close();
        inputStream.loadBuffer();
        System.out.println(inputStream.buffer);
    }

    @Test
    public void getBit_WhenNormalStream_ThenMatchBit() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        inputStream=new InputStream(byteStream);
        inputStream.loadBuffer();
        System.out.println(inputStream.buffer);
        assertEquals(0,inputStream.getBit());
//        System.out.println(inputStream.buffer);
    }

    @Test
    public void getBit_WhenEOFReached_ThenReturnValueMatch() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        inputStream=new InputStream(byteStream);
//        System.out.println(inputStream.buffer);
        inputStream.getByte();
        inputStream.loadBuffer();
        assertEquals(-1,inputStream.getBit());
    }

    @Test
    public void getBits_WhenNormalInputStream_ThenReturnBitsMatch() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        inputStream=new InputStream(byteStream);
        inputStream.loadBuffer();
        assertEquals(1,inputStream.getBits(2));
    }

    @Test
    public void getByte_WhenNormalStream_ThenByteMatch() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        inputStream=new InputStream(byteStream);
        assertEquals(97,inputStream.getByte());
    }

    @Test
    public void getByte_WhenInputStreamClosed_ThenCatchIoException() throws IOException{
        expectedException.expect(IOException.class);
        expectedException.expectMessage("InputStream is closed");
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        inputStream=new InputStream(byteStream);
        inputStream.close();
        inputStream.getByte();
    }

    @Test
    public void close_WhenNormalInputStream_ThenCloseTheStream() throws IOException{
        String input="a";
        java.io.InputStream byteStream=new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        inputStream=new InputStream(byteStream);
        inputStream.close();
    }
}