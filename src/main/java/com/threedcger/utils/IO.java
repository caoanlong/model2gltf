package com.threedcger.utils;

import java.net.URI;

public class IO {
    public static URI getParent(URI uri) {
        if (uri.getPath().endsWith("/")) return uri.resolve("..");
        return uri.resolve(".");
    }
    public static String extractFileName(URI uri) {
        String s = uri.toString();
        int lastSlashIndex = s.lastIndexOf('/');
        if (lastSlashIndex != -1) return s.substring(lastSlashIndex + 1);
        return s;
    }
    public static String stripFileNameExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0) return fileName;
        return fileName.substring(0, lastDotIndex);
    }
}
