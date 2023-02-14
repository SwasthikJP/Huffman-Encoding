package com.capillary.Compression.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFileHandler {
    public InputStream getInputStream() throws IOException;
    public OutputStream getOutputStream() throws IOException;
}
