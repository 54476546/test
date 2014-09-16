package com.hisense.hitv.compress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * zip格式文件压缩处理
 * @author zhoudi
 * @version 1.0
 */
public class GZIPCompressImpl implements ICompressLogFile {
    /**
     * backslash
     */
    private static final String STRING = "/";
    /**
     * vertical
     */
    private static final String VERTICAL = "|";
    private static final String ENDFIX = ".tmp";
    private static final Log LOG = LogFactory.getLog(GZIPCompressImpl.class);

    /**
     * 文件压缩处理
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param compressPath 压缩路径
     * @param compressType 压缩类型
     * @return true：成功，false：失败
     */
    @Override
	public boolean compress(String fileName, String filePath, String compressPath, String compressType) {
        FileInputStream in = null;
        BufferedOutputStream out = null;
        String tempOutFileName = null;
        try {
            LOG.info(fileName + VERTICAL + filePath + VERTICAL + compressPath + VERTICAL + compressType);
            tempOutFileName = compressPath + STRING + fileName + compressType + ENDFIX;
            File backPath = new File(compressPath);
            if (!backPath.isDirectory()) {
                backPath.mkdirs();
            }

            in = new FileInputStream(filePath + STRING + fileName);
            out = new BufferedOutputStream(new GZIPOutputStream(
                new FileOutputStream(tempOutFileName)));

            int c;
            while ((c = in.read()) > 0) {
                out.write(c);
            }
        } catch (FileNotFoundException e) {
            LOG.error(e.toString());
            return false;
        } catch (IOException e) {
            LOG.error(e.toString());
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOG.error(e);
                return false;
            }
        }
        // 解压成功后重新命名，去掉结尾的.tmp
        new File(tempOutFileName).renameTo(
            new File(tempOutFileName.substring(
                0, tempOutFileName.lastIndexOf(ENDFIX))));
        return true;
    }
    
    /**
     * 文件解压缩处理
     * @param outFilename 输出文件名
     * @param inFilePath 输入文件全路径
     * @param decompressPath 输出文件全路径
     * @return true：成功，false：失败
     */
    @Override
	public boolean decompress(String outFilename,
        String inFilePath, String decompressPath) {
        GZIPInputStream in = null;
        BufferedOutputStream out = null;
        String tempOutFileName = null;
        try {
            // 打开要被解压的文件
            in = new GZIPInputStream(new FileInputStream(inFilePath));

            // 输出文件流
            File decompressPathDir = new File(decompressPath);
            if (!decompressPathDir.exists()) {
                LOG.info("DecompressPathDir does not exists!!Dir is " + decompressPathDir);
                decompressPathDir.mkdirs();
            }
            tempOutFileName = decompressPath + STRING + outFilename + ENDFIX;
            out = new BufferedOutputStream(new FileOutputStream(tempOutFileName));

            // 写文件
            int len;
            while ((len = in.read()) > 0) {
                out.write(len);
            }
        } catch (IOException e) {
            LOG.error(e);
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LOG.error(e);
                return false;
            }
        }
        // 解压成功后重新命名，去掉结尾的.tmp
        new File(tempOutFileName).renameTo(
            new File(tempOutFileName.substring(
                0, tempOutFileName.lastIndexOf(ENDFIX))));
        return true;
    }

}
