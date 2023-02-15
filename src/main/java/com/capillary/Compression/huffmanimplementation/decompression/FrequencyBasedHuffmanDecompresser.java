package com.capillary.Compression.huffmanimplementation.decompression;
import com.capillary.Compression.huffmanimplementation.huffmanutils.CompressedFileReaderWriterImpl;
import com.capillary.Compression.huffmanimplementation.huffmanutils.PreorderHeaderInfoReaderWriter;
import com.capillary.Compression.utils.ICompressedFileReaderWriter;
import com.capillary.Compression.utils.IHeaderInfoReaderWriter;
import com.capillary.Compression.utils.InputStream;
import com.capillary.Compression.utils.OutputStream;
import com.capillary.Compression.huffmanimplementation.huffmanutils.Node;

import java.io.IOException;

public class FrequencyBasedHuffmanDecompresser implements IHuffmanDecompresser {


   private InputStream inputStream;



    @Override
    public Node createHuffmanTree(java.io.InputStream fileInputStream) throws IOException{
        inputStream = new InputStream(fileInputStream);
        inputStream.loadBuffer();
        IHeaderInfoReaderWriter headerInfoReaderWriter=new PreorderHeaderInfoReaderWriter();
        return  headerInfoReaderWriter.readHeaderInfo(inputStream);
    }

    @Override
    public Boolean decodeFile( java.io.OutputStream fileOutputStream, Object rootNode) throws IOException{

        if (inputStream == null || rootNode==null) {
            return false;
        }
       OutputStream outputStream = new OutputStream(fileOutputStream);

        ICompressedFileReaderWriter compressedFileReaderWriter=new CompressedFileReaderWriterImpl();

        return compressedFileReaderWriter.readCompressedFile(inputStream,outputStream,(Node)rootNode);
    }

}
