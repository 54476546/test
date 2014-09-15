package com.hisense.hitv.compress;

/**
 * 文件压缩处理接口
 * @author zhoudi
 * @version 1.0
 */
public interface ICompressLogFile {
    /**
     * 文件压缩处理
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param compressPath 压缩路径
     * @param compressType 压缩类型
     * @return true：成功，false：失败
     */
    public boolean compress(String fileName, String filePath, String compressPath, String compressType);
    /**
     * 文件解压缩处理
     * @param outFilename 输出文件名
     * @param inFilePath 输入文件全路径
     * @param decompressPath 输出文件全路径
     * @return true：成功，false：失败
     */
    public boolean decompress(String outFilename, String inFilePath, String decompressPath);
}
