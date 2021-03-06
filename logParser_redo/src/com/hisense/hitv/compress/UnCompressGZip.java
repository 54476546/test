package com.hisense.hitv.compress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * 解压缩处理
 * @author zhoudi
 * @version 1.0
 */
public class UnCompressGZip {
    /**
     * backslash
     */
    private static final String BACKSLASH = "/";

    /**
     * 缓冲池1024字节
     */
    private static final int BYTE_BUFFER_1024 = 1024;

    private static final Log LOG = LogFactory.getLog(UnCompressGZip.class);

    /**
     * 文件解压缩
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param unCompressPath 解压路径
     * @param unCompressType 解压类型
     * @return true：成功， false：失败
     */
    public boolean upCompress(String fileName, String filePath, String unCompressPath, String unCompressType) {
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(filePath + BACKSLASH + fileName));
            OutputStream out = new FileOutputStream(unCompressPath + BACKSLASH + fileName + unCompressType);

            byte[] buf = new byte[BYTE_BUFFER_1024];
            int len;
            while ((len = gzipInputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            gzipInputStream.close();
            out.close();

            return true;
        } catch (FileNotFoundException e) {
            LOG.error(e.toString());
            return false;
        } catch (IOException e) {
            LOG.error(e.toString());
            return false;
        }
    }

}
