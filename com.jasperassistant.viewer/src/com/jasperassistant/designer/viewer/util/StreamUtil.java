package com.jasperassistant.designer.viewer.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    
    public static byte[] toByteArray(InputStream input) throws IOException {
        return toByteArray(input, 1024);
    }
    
    public static byte[] toByteArray(InputStream input, int bufferSize) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Fake code simulating the copy
        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = input.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        input.close();
        return baos.toByteArray();
    }
}
