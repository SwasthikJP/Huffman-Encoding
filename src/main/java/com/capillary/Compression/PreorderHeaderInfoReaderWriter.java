package com.capillary.Compression;
public class PreorderHeaderInfoReaderWriter implements IHeaderInfoReaderWriter {

    @Override
    public Node readHeaderInfo(InputStream inputStream) {

        if (inputStream.getBit() == 0) {
            return new Node(readHeaderInfo(inputStream), readHeaderInfo(inputStream));
        }
        return new Node(inputStream.getBits(9), 0);
    }

    @Override
    public void writeHeaderInfo(Node node, OutputStream outputStream) {
        if (node == null) {
            return;
        }
        if (node.isLeafNode) {
            outputStream.writeBit(1);
            outputStream.writeBits(node.value, 9);
        } else {
            outputStream.writeBit(0);
            writeHeaderInfo(node.left, outputStream);
            writeHeaderInfo(node.right, outputStream);
        }

    }

}
