package com.capillary.Compression.wordbasedhuffman.decompression;

import com.capillary.Compression.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.Compression.utils.ByteInputStream;
import com.capillary.Compression.utils.ByteOutputStream;
import com.capillary.Compression.utils.Node;
import com.capillary.Compression.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.Compression.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.Compression.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordBasedHuffmanDecompresser implements IHuffmanDecompresser {
    private ByteInputStream byteInputStream;
    private IHeaderInfoReaderWriter headerInfoReaderWriter;
    private ICompressedFileReaderWriter compressedFileReaderWriter;

    public WordBasedHuffmanDecompresser(){
        headerInfoReaderWriter=new WordHeaderInfoReaderWriter();
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();
    }

    public WordBasedHuffmanDecompresser(IHeaderInfoReaderWriter headerInfoReaderWriter,ICompressedFileReaderWriter compressedFileReaderWriter){
        this.headerInfoReaderWriter=headerInfoReaderWriter;
        this.compressedFileReaderWriter=compressedFileReaderWriter;
    }
    public WordBasedHuffmanDecompresser(ByteInputStream byteInputStream,IHeaderInfoReaderWriter headerInfoReaderWriter,ICompressedFileReaderWriter compressedFileReaderWriter){
        this.byteInputStream=byteInputStream;
        this.headerInfoReaderWriter=headerInfoReaderWriter;
        this.compressedFileReaderWriter=compressedFileReaderWriter;
    }


    @Override
    public Object createHuffmanTree(InputStream fileInputStream) throws IOException {
            byteInputStream = new ByteInputStream(fileInputStream);

            return  headerInfoReaderWriter.readHeaderInfo(byteInputStream);
    }

    @Override
    public Boolean decodeFile(OutputStream fileOutputStream, Object node) throws IOException {
        if (byteInputStream == null || node==null) {
            return false;
        }
        ByteOutputStream byteOutputStream = new ByteOutputStream(fileOutputStream);

        return compressedFileReaderWriter.readCompressedFile(byteInputStream, byteOutputStream,(Node)node);
    }
}
