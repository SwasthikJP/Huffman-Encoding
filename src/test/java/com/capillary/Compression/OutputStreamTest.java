package com.capillary.Compression;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class OutputStreamTest {

    OutputStream outputStream;

    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    @Test
    public void flushBuffer_WhenBufferFull_ThenEmptyBuffer()throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);

     outputStream=new OutputStream(byteArrayOutputStream);
     outputStream.writeBits(97,8);
     outputStream.flushBuffer();
     assertEquals(0,outputStream.bufferSize);
    }

    @Test
    public void writeBit_WhenOneBit_ThenBufferMatch() throws IOException{
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);

        outputStream=new OutputStream(byteArrayOutputStream);
        outputStream.writeBit(1);
        outputStream.writeBit(0);
        assertEquals(2,outputStream.buffer);
    }

    @Test
    public void writeBit_WhenBufferIsFull_ThenFlushBuffer() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);

        outputStream=new OutputStream(byteArrayOutputStream);
        outputStream.writeBits(97,7);
        outputStream.writeBit(1);
        assertEquals(0,outputStream.buffer);
    }

        @Test
    public void writeBits_WhenNormalBits_ThenMatchBuffer() throws IOException{
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
            outputStream=new OutputStream(byteArrayOutputStream);
            outputStream.writeBits(97,7);
            assertEquals(97,outputStream.buffer);
    }

    @Test
    public void writeBits_WhenLengthIsNegative_ThenCatchException() throws IOException {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Negative Length");
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        outputStream=new OutputStream(byteArrayOutputStream);
        outputStream.writeBits(97,-1);
    }

        @Test
    public void testWriteBitsString_WhenNormalByteString_ThenBufferMatch() throws IOException{
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
            outputStream=new OutputStream(byteArrayOutputStream);
            outputStream.writeBits("1100001",7);
            assertEquals(97,outputStream.buffer);
    }

    @Test
    public void writeByte_WhenNormalByte_ThenWriteByte() throws IOException{
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        outputStream=new OutputStream(byteArrayOutputStream);
        outputStream.writeByte(97);
        outputStream.closeStream();
        byte[] result=byteArrayOutputStream.toByteArray();
        byte[] expected={97};
        assertArrayEquals(expected,result);
    }

    @Test
    public void closeStream_WhenBufferEmpty_ThenCloseStream()throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        outputStream=new OutputStream(byteArrayOutputStream);
        assertEquals(0,outputStream.bufferSize);
        outputStream.closeStream();
    }

    @Test
    public void closeStream_WhenBufferSizeLessThanEight_ThenAddPaddingToBuffer()throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(1);
        outputStream=new OutputStream(byteArrayOutputStream);
        outputStream.writeBits(2,7);
        outputStream.closeStream();
        byte[] expected={4};
        assertArrayEquals(expected,byteArrayOutputStream.toByteArray());
    }
}