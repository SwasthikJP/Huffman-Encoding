package com.capillary.Compression.wordbasedhuffman.decompression;

import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.IHashMap;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class WordBasedHuffmanDecompresserTest {

    IHuffmanDecompresser wordBasedHuffmanDecompresser;
    @Rule
    public ExpectedException expectedException=ExpectedException.none();

    @Test
    public void createHuffmanTree_WhenValidHeader_ThenReturnNode()throws IOException {

        String fileInput="aB 1/a";
        InputStream inputStream = new ByteArrayInputStream(fileInput.getBytes
                (Charset.forName("UTF-8")));

        Node node=new Node();
        IHeaderInfoReaderWriter headerInfoReaderWriter= spy(IHeaderInfoReaderWriter.class);
        doReturn(node).when(headerInfoReaderWriter).readHeaderInfo(any(ByteInputStream.class));

        wordBasedHuffmanDecompresser=new WordBasedHuffmanDecompresser(headerInfoReaderWriter,null);

        assertEquals(node,wordBasedHuffmanDecompresser.createHuffmanTree(inputStream));
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

        wordBasedHuffmanDecompresser=new WordBasedHuffmanDecompresser(new ByteInputStream(inputStream),null,compressedFileReaderWriter);

        assertEquals(true,wordBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,node));
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

        wordBasedHuffmanDecompresser=new WordBasedHuffmanDecompresser(new ByteInputStream(inputStream),null,compressedFileReaderWriter);

        assertEquals(false,wordBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,null));

    }


    @Test
    public void decodeFile_WhenByteInputStreamIsNull_ThenReturnFalse() throws IOException{

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        ICompressedFileReaderWriter compressedFileReaderWriter= spy(ICompressedFileReaderWriter.class);
        doReturn(true).when(compressedFileReaderWriter).readCompressedFile(any(ByteInputStream.class),any(ByteOutputStream.class),any(Node.class));

        wordBasedHuffmanDecompresser=new WordBasedHuffmanDecompresser(null,null,compressedFileReaderWriter);

        assertEquals(false,wordBasedHuffmanDecompresser.decodeFile(byteArrayOutputStream,null));

    }

}