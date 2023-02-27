package com.capillary.zipper.zipper;

import com.capillary.zipper.utils.IFileHandler;

public interface IZipperApp {

    void compress(IFileHandler fileHandler);

    void decompress(IFileHandler fileHandler);

}
