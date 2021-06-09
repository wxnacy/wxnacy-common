package com.wxnacy.common.file;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author wxnacy
 */
public class FileUtils {
    public static void main(String[] args) throws IOException {
//        var fu = FileUtils.class.getDeclaredConstructor().newInstance();
//        System.out.println("fu = " + fu);
        String dirPath = "/Users/wxnacy/Downloads/test1";
        String filePath = "/Users/wxnacy/Downloads/image-1.jpg";
        Path path = Path.of("~/Downloads/test1");
        System.out.println("path = " + path);
        System.out.println("path.toAbsolutePath() = " + path.toAbsolutePath());
        List<Path> files= (List<Path>) listAllFile(Paths.get(System.getenv("HOME"), "Downloads/test1"));
        System.out.println("files = " + files);
        

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

    /**
     * 将 byte 数组换转换为 string
     * @param bytes
     * @return
     */
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
    public static Collection<Path> listAllFile(String dirName, FileType fileType) throws IOException {
        var files = listAllFile(dirName);
        Collection<Path> results = new LinkedList<>();
        for (Path file: files ) {
            FileType ft = getFileType(file.toFile());
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
    public static Collection<Path> listAllFile(String dirName, Predicate<Path> predicate) throws IOException {
        Collection<Path> files = listAllFile(dirName);
        var res = new ArrayList<Path>();
        files.iterator().forEachRemaining(file -> {
            if (predicate.test(file)) {
                res.add(file);
            }
        });
        return res;
    }

    /**
     * 遍历文件夹内所有的文件
     * @param path
     * @return
     * @throws IOException
     */
    public static Collection<Path> listAllFile(Path path) throws IOException {
        List<Path> result = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                result.add(file);
                return super.visitFile(file, attrs);
            }
        });

        return result;
    }

    /**
     * 遍历文件夹的所有文件
     * @param dirName 文件夹
     * @return Collection<File>
     */
    public static Collection<Path> listAllFile(String dirName) throws IOException {
        return listAllFile(Path.of(dirName));
    }

    /**
     * 使用路径创建目录
     * @param filePath
     */
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
