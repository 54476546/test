package com.hisense.hitv.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
/**
 * 测试类
 * @author tianyuqi
 */
public class LogFileNameTest {

    @Test
    public void getFileListByPath_A$String() throws Exception {
        // Given
        LogFileName target = new LogFileName();
        String fileName1 = "/usr/LogFileNameTest/getFileListByPath.txt";
        String fileName2 = "/usr/LogFileNameTest/Log/getFileListByPath.txt";
        File file = new File("/usr/LogFileNameTest/Log/logs");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(fileName1);
        if (!file.exists()) {
            file.createNewFile();
        }
        file = new File(fileName2);
        if (!file.exists()) {
            file.createNewFile();
        }
        String filePath = "/usr/LogFileNameTest/";
        // When
        List<String> actual = target.getFileListByPath(filePath);
        // Then
        boolean expected = true;
        List<String> expectedList = new ArrayList<String>();
        expectedList.add(fileName1);
        expectedList.add(fileName2);
        for (int i = 0; i < actual.size(); i++) {
            String s = actual.get(i);
            actual.set(i, s.replaceAll("\\\\", "/"));
        }
        for (int i = 0; i < expectedList.size(); i++) {
            if (!actual.contains(expectedList.get(i))) {
                expected = false;
                break;
            }
        }
        assertThat(true, is(equalTo(expected)));
        new File(fileName1).delete();
        new File(fileName2).delete();
        new File("/usr/LogFileNameTest/Log/logs").delete();
        new File("/usr/LogFileNameTest/Log").delete();
        new File("/usr/LogFileNameTest").delete();
    }

}
