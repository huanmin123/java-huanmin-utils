package com.utils.common.file.condense;


import com.utils.common.file.FileUtil;
import com.utils.common.file.WriteFileBytesUtil;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * 将内容进行gz压缩或者将压缩后的内容输出为gz文件,或者打包为tar.gz压缩文件,和解压缩
 */
public class GZIPUtils {
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str
     * @return
     */
    public static byte[] compress(String str) {
        return compress(str, GZIP_ENCODE_UTF_8);
    }

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str
     * @param encoding
     * @return
     */
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (
            GZIPOutputStream gzip=new GZIPOutputStream(out);
        ){
         gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static byte[] compressByte(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (
                GZIPOutputStream gzip = new GZIPOutputStream(out);
        ) {
            gzip.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    //压缩字节然后,在写入到文件中
    public static void compressFileWrite(File file, byte[] bytes, boolean append) {
        byte[] bytes1 = compressByte(bytes);
        WriteFileBytesUtil.writeByte(bytes1, file, append);
    }

    //压缩字符然后,在写入到文件中
    public static void compressFileWrite(File file, String str, boolean append) {
        byte[] bytes1 = compress(str);
        WriteFileBytesUtil.writeByte(bytes1, file, append);
    }

    // 将内容写入文件中,然后对整个文件的全部内容进行压缩,
    @SneakyThrows
    public static void writeGzFile(File outFile, byte[] bytes) {
        try (
                GZIPOutputStream gZipOutputStream = new GZIPOutputStream(new FileOutputStream(outFile));
                BufferedOutputStream fos = new BufferedOutputStream(gZipOutputStream);
        ) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将文件压缩为gz的压缩文件
    @SneakyThrows
    public static void writeGzFile(File outFile, File inputFile) {
        try (
                GZIPOutputStream gZipOutputStream = new GZIPOutputStream(new FileOutputStream(outFile));
                BufferedOutputStream fos = new BufferedOutputStream(gZipOutputStream);
                BufferedInputStream fis = new BufferedInputStream(new FileInputStream(inputFile))
        ) {
            byte[] data = new byte[4096];
            int len;
            while ((len = fis.read(data)) != -1) {
                fos.write(data, 0, len); // 写入数据
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取gz压缩文件内容(按行读取)
    @SneakyThrows
    public static void readGzFile(File file, Consumer<String> consumer) {
        try (
                GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(file));
                InputStreamReader gzipReader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(gzipReader);
        ) {
            String len = null;
            while ((len = bufferedReader.readLine()) != null) {
                consumer.accept(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //一次全部读取gz文件内容
    @SneakyThrows
    public static byte[] readGzFile(File file) {
        try (
                GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(file));
                BufferedInputStream fis = new BufferedInputStream(gzipInputStream)
        ) {
            byte[] body = new byte[fis.available()];
            fis.read(body);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将压缩后的字节进行GZIP解压缩,返回原来的字节
     *
     * @param bytes
     * @return
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    /**
     * 将解压缩后的字节转字符串
     *
     * @param bytes
     * @return
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }

    /**
     * 将解压缩后的字节转字符串,指定编码格式
     *
     * @param bytes
     * @param encoding
     * @return
     */
    private static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(GZIP_ENCODE_UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 压缩文件
     *
     * @param files     指定打包的所有文件
     * @param tarGzPath 指定目标 tar 包的位置
     */
    public static void compressTarGz(List<File> files, String tarGzPath) {
        try (
                // 创建一个 FileOutputStream 到输出文件（.tar.gz）
                FileOutputStream fos = new FileOutputStream(tarGzPath);
                // 创建一个 GZIPOutputStream，用来包装 FileOutputStream 对象
                GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
                // 创建一个 TarArchiveOutputStream，用来包装 GZIPOutputStream 对象
                TarArchiveOutputStream tarOs = new TarArchiveOutputStream(gos);
        ) {
            // 使文件名支持超过 100 个字节
            tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
            //将所有文件迁移到包目录里
            for (File oneFile : files) {
                String parentDirectoryName = FileUtil.filePartInfo(oneFile.getAbsolutePath(), "parentDirectoryName");
                addFilesToTarGZ(oneFile.getPath(), parentDirectoryName + File.separator, tarOs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sourcePath 源文件
     * @param parent     源目录
     * @param tarArchive 压缩输出流
     * @throws IOException
     */
    private static void addFilesToTarGZ(String sourcePath, String parent, TarArchiveOutputStream tarArchive) throws IOException {
        File sourceFile = new File(sourcePath);
        // 获取新目录下的文件名称
        String fileName = parent.concat(sourceFile.getName());
        //打包压缩该文件
        tarArchive.putArchiveEntry(new TarArchiveEntry(sourceFile, fileName));
        if (sourceFile.isFile()) {
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // 写入文件
            IOUtils.copy(bis, tarArchive);
            tarArchive.closeArchiveEntry();
            bis.close();
        }
    }


    /**
     * 解压.tar.gz文件
     *
     * @param sourceFile 需解压文件
     * @param outputDir  输出目录
     * @throws IOException
     */
    public static void unTarGz(String sourceFile, String outputDir) throws IOException {
        TarInputStream tarIn = null;
        File file = new File(sourceFile);
        try {
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(new FileInputStream(file))),
                    1024 * 2);

            createDirectory(outputDir, null);//创建输出目录

            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {

                if (entry.isDirectory()) {//是目录
                    entry.getName();
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];

                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }

                    } catch (IOException ex) {
                        throw ex;
                    } finally {

                        if (out != null) {
                            out.close();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }


}

