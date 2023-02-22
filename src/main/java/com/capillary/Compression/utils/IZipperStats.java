package com.capillary.Compression.utils;
public interface IZipperStats {

    void startTimer();

    void stopTimer();

    void displayDecompressionStats(String compressFilepath, String decompressFilepath);

    void displayCompressionStats(String decompressFilePath, String compressFilePath);

    void calculateAverageCodeLength(IHashMap frequencyMap,IHashMap hashCodeMap);

    void calcHeaderSize(ByteOutputStream byteOutputStream);

    void calcCompressedBodySize(ByteOutputStream byteOutputStream);

    long calcAverageWordLength(IHashMap frequencyMap);

    void displayTimeTaken(String methodName);


}
