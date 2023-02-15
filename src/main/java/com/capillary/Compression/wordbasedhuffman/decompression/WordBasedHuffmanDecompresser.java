package com.capillary.Compression.wordbasedhuffman.decompression;

import com.capillary.Compression.huffmanimplementation.decompression.IHuffmanDecompresser;
import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;
import com.capillary.Compression.utils.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHeaderInfoReaderWriter;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordBasedHuffmanDecompresser implements IHuffmanDecompresser {
    private com.capillary.Compression.utils.InputStream inputStream;

    @Override
    public Object createHuffmanTree(InputStream fileInputStream) throws IOException {
        try {
            inputStream = new com.capillary.Compression.utils.InputStream(fileInputStream);
            inputStream.loadBuffer();
            IHeaderInfoReaderWriter headerInfoReaderWriter2 = new WordHeaderInfoReaderWriter();
            return (Object) headerInfoReaderWriter2.readHeaderInfo(inputStream);
        }catch (Exception e){
            throw  new IOException(e.getMessage());
        }
    }

    @Override
    public Boolean decodeFile(OutputStream fileOutputStream, Object hashMap) throws IOException {
        if (inputStream == null || hashMap==null) {
            return false;
        }
        com.capillary.Compression.utils.OutputStream outputStream = new com.capillary.Compression.utils.OutputStream(fileOutputStream);

        ICompressedFileReaderWriter compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();

        return compressedFileReaderWriter.readCompressedFile(inputStream,outputStream,(Node)hashMap);
    }
}
