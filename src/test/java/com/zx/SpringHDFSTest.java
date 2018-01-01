package com.zx;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * author:ZhengXing
 * datetime:2017-12-29 21:46
 * spring 结合 hadoop 操作 hdfs测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringHDFSTest {

    @Autowired
    private FileSystem fileSystem;

    @Autowired
//    private FsShell fsShell;

//    @Test
//    public void test2() {
//        for (FileStatus fileStatus : fsShell.lsr("/output")) {
//            System.out.println(">" + fileStatus.getPath());
//        }
//    }

    @Test
    public void test() throws IOException {
        fileSystem.mkdirs(new Path("/ccc"));

        String file = "C:\\Users\\97038\\Desktop\\a.txt";
        Path oldPath = new Path(file);
        Path newPath = new Path("/ccc");
        fileSystem.copyFromLocalFile(oldPath,newPath);
    }

}
