package com.capillary.Compression.zipper;

import com.capillary.Compression.utils.IFileHandler;

public interface IZipperApp {

    void compress(IFileHandler fileHandler);

    void decompress(IFileHandler fileHandler);

}
