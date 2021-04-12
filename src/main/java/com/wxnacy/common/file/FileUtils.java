package com.wxnacy.common.file;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author wxnacy
 */
public class FileUtils {
    public static void main(String[] args) throws IOException {
        String dirPath = "/Users/wxnacy/Downloads/test1";
        String filePath = "/Users/wxnacy/Downloads/image-1.jpg";
        Collection<File> files = listAllFile(dirPath, file -> file.getAbsolutePath().contains("txt"));
        files.forEach( item -> {
            System.out.println(item.getAbsolutePath());
        });

    }

    public static FileType getFileType(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        return getFileType(is);
    }

    public static FileType getFileType(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        return getFileType(is);
    }

    public static FileType getFileType(InputStream is) throws IOException {
        String type = getFileTypeString(is);
        for (FileType ft :
                FileType.values()) {
            for (String t :
                    ft.getNames()) {
                if (type == t) {
                    return ft;
                }
            }

        }
        return null;
    }

    public static String getFileTypeString(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        return getFileTypeString(is);
    }

    public static String getFileTypeString(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        return getFileTypeString(is);
    }

    public static String getFileTypeString(InputStream is) throws IOException {
        byte[] bytes = new byte[16];
        is.read(bytes);
//        FileUtils.converBytesToHexToString()
        String fileHexString = converBytesToHexToString(bytes);
        Iterator<Map.Entry<String, String>> iterator = FileConstants.FILE_TYPE_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            if ( fileHexString.toUpperCase().startsWith(value)) {
                return key;
            }
        }
        return null;
    }

    private static String converBytesToHexToString(byte[] bytes ) {
        StringBuilder sb = new StringBuilder();
        for (byte b :
                bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 遍历文件夹所有文件，并使用 FileType 过滤
     * @param dirName 文件夹
     * @param fileType 文件类型
     * @return Collection<File>
     * @throws IOException
     */
    public static Collection<File> listAllFile(String dirName, FileType fileType) throws IOException {
        var files = listAllFile(dirName);
        Collection<File> results = new LinkedList<>();
        for (File file: files ) {
            FileType ft = getFileType(file);
            if ( ft == fileType) {
                results.add(file);
            }
        }
        return results;
    }

    /**
     * 使用过滤器来遍历文件夹
     * @param dirName 文件夹
     * @param predicate 过滤函数式接口
     * @return Collection<File>
     */
    public static Collection<File> listAllFile(String dirName, Predicate<File> predicate) {
        Collection<File> files = listAllFile(dirName);
        var res = new ArrayList<File>();
        files.iterator().forEachRemaining(file -> {
            if (predicate.test(file)) {
                res.add(file);
            }
        });
        return res;
    }

    /**
     * 遍历文件夹的所有文件
     * @param dirName 文件夹
     * @return Collection<File>
     */
    public static Collection<File> listAllFile(String dirName) {
        File file = new File(dirName);
        LinkedList<File> results = new LinkedList<>();
        if (!file.exists()) {
            return results;
        }

        LinkedList<File> dirList = new LinkedList<>();
        for (File subFile :
                file.listFiles()) {
            if ( subFile.isDirectory()) {
                dirList.push(subFile);
            } else {
                results.add(subFile);
            }
        }

        while (!dirList.isEmpty()) {
            var removeFile = dirList.removeFirst();
            for (File subFile :
                    removeFile.listFiles()) {
                if (subFile.isDirectory()) {
                    dirList.push(subFile);
                } else {
                    results.add(subFile);
                }
            }
        }

        return results;
    }

    public static void makeDir(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }

        if (filePath.endsWith("/")) {
            file.mkdirs();
            return;
        }

        File dirFile = new File(file.getParent());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

}
