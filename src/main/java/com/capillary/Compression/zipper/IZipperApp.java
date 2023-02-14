package com.capillary.Compression.zipper;

import com.capillary.Compression.utils.IFileHandler;

public interface IZipperApp {

    String compress(IFileHandler fileHandler);

    String decompress(IFileHandler fileHandler);

}
