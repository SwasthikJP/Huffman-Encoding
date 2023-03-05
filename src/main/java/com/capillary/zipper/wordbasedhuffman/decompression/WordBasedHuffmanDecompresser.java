package com.capillary.zipper.wordbasedhuffman.decompression;

import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;
import com.capillary.zipper.utils.Node;
import com.capillary.zipper.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.Checksum;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.CompressedWordFileReaderWriterImpl;
import com.capillary.zipper.wordbasedhuffman.huffmanutils.WordHeaderInfoReaderWriter;

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

    public WordBasedHuffmanDecompresser(InputStream inputStream)throws IOException{
        this.byteInputStream=new ByteInputStream(inputStream);
        headerInfoReaderWriter=new WordHeaderInfoReaderWriter();
        compressedFileReaderWriter=new CompressedWordFileReaderWriterImpl();
    }


    @Override
    public Object createHuffmanTree(InputStream fileInputStream) throws IOException {
            byteInputStream = new ByteInputStream(fileInputStream);
           return headerInfoReaderWriter.readHeaderInfo(byteInputStream);
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
