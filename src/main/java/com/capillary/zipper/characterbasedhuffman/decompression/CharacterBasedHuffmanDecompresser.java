package com.capillary.zipper.characterbasedhuffman.decompression;
import com.capillary.zipper.characterbasedhuffman.huffmanutils.CompressedFileReaderWriterImpl;
import com.capillary.zipper.characterbasedhuffman.huffmanutils.PreorderHeaderInfoReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.ICompressedFileReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.IHeaderInfoReaderWriter;
import com.capillary.zipper.commonhuffmaninterfaces.IHuffmanDecompresser;
import com.capillary.zipper.utils.ByteInputStream;
import com.capillary.zipper.utils.ByteOutputStream;
import com.capillary.zipper.utils.Node;

import java.io.IOException;

public class CharacterBasedHuffmanDecompresser implements IHuffmanDecompresser {


   private ByteInputStream byteInputStream;
    private IHeaderInfoReaderWriter headerInfoReaderWriter;
    private ICompressedFileReaderWriter compressedFileReaderWriter;

    public CharacterBasedHuffmanDecompresser(){
        headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();
        compressedFileReaderWriter=new CompressedFileReaderWriterImpl();
    }

    public CharacterBasedHuffmanDecompresser(IHeaderInfoReaderWriter headerInfoReaderWriter,ICompressedFileReaderWriter compressedFileReaderWriter){
        this.headerInfoReaderWriter=headerInfoReaderWriter;
        this.compressedFileReaderWriter=compressedFileReaderWriter;
    }
    public CharacterBasedHuffmanDecompresser(ByteInputStream byteInputStream,IHeaderInfoReaderWriter headerInfoReaderWriter,ICompressedFileReaderWriter compressedFileReaderWriter){
        this.byteInputStream=byteInputStream;
        this.headerInfoReaderWriter=headerInfoReaderWriter;
        this.compressedFileReaderWriter=compressedFileReaderWriter;
    }




    @Override
    public Object createHuffmanTree(java.io.InputStream fileInputStream) throws IOException{
        byteInputStream = new ByteInputStream(fileInputStream);

        return  headerInfoReaderWriter.readHeaderInfo(byteInputStream);
    }

    @Override
    public Boolean decodeFile( java.io.OutputStream fileOutputStream, Object rootNode) throws IOException{

        if (byteInputStream == null || rootNode==null) {
            return false;
        }
        ByteOutputStream byteOutputStream = new ByteOutputStream(fileOutputStream);

        return compressedFileReaderWriter.readCompressedFile(byteInputStream, byteOutputStream,(Node)rootNode);
    }

}
