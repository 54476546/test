package com.hisense.hitv.compress;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

import org.junit.Test;
/**
 * 压缩接口测试类
 * @author tianyuqi
 */
public class ICompressLogFileTest {

    @Test
    public void decompress_A$String$String$String() throws Exception {
        // Given
        ICompressLogFile target = new GZIPCompressImpl();
        BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(
            new FileOutputStream("/usr/ICompressLogFileTest.log.gz")));
        out.write(("1234" + "\n").getBytes());
        out.write("5678".getBytes());
        out.close();
        String outFilename = "ICompressLogFileTest.log";
        String inFilePath = "/usr/ICompressLogFileTest.log.gz";
        String decompressPath = "/usr";
        // When
        boolean actual = target.decompress(outFilename, inFilePath, decompressPath);
        // Then
        boolean expected = true;
        assertThat(actual, is(equalTo(expected)));
        new File("/usr/ICompressLogFileTest.log.gz").delete();
        new File("/usr/ICompressLogFileTest.log").delete();
    }
}
