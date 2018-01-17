package com.zx;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * author:ZhengXing
 * datetime:2017-11-25 8:55
 * Hadoop HDFS Java API 操作
 */
public class HDFSTest {
    //hadoop地址
    public static final String HDFS_PATH = "hdfs://106.14.7.29:8020";

    //HDFS文件系统
    FileSystem fileSystem = null;
    //配置
    Configuration configuration = null;

    /**
     * 创建HDFS目录
     */
    @Test
    public void mkdir() throws IOException {
        fileSystem.mkdirs(new Path("/zx/test"));
    }

    /**
     * 创建文件
     */
    @Test
    public void create() throws Exception {
        FSDataOutputStream outputStream = fileSystem.create(new Path("/zx/test/cc.txt"));
        outputStream.write("测试创建文件".getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 查看文件内容
     */
    @Test
    public void cat() throws Exception {
        //获取文件输入流
        FSDataInputStream inputStream = fileSystem.open(new Path("/zx/test/a.txt"));
        //输出到控制台
        IOUtils.copyBytes(inputStream,System.out,1024);
        inputStream.close();
    }

    /**
     * 重命名
     */
    @Test
    public void rename() throws Exception {
        Path oldPath = new Path("/zx/test/a.txt");
        Path newPath = new Path("/zx/test/aaa.txt");
        boolean flag = fileSystem.rename(oldPath,newPath);
        System.out.println(flag);
    }

    /**
     * 上传文件到HDFS
     */
    @Test
    public void copyFromLocalFile() throws Exception {
//        Path oldPath = new Path("D:" + File.separator + "doc.txt");
        String file = "D:\\a.txt";
        Path oldPath = new Path(file);
        Path newPath = new Path("/zxx");
        fileSystem.copyFromLocalFile(oldPath,newPath);
    }

    /**
     * 上传文件到HDFS,进度条,
     * 但是这个.....进度,是在上传完成后,才会全部出现的.很坑爹
     */
    @Test
    public void copyFromLocalFileWithProgress() throws Exception {
        String file = "C:\\Users\\97038\\Desktop\\a.txt";
        //输入流
        BufferedInputStream in = new BufferedInputStream(
                new FileInputStream(
                        new File(file)));
        //输出流
        FSDataOutputStream outputStream = fileSystem.create(new Path("/zx/test/a.txt"),
                new Progressable() {
                //进度提醒信息
                    @Override
                    public void progress() {
                        System.out.print(".");
                    }
                });
        //上传
        IOUtils.copyBytes(in,outputStream,10240);
    }

    /**
     * 下载文件到本地
     */
    @Test
    public void copyToLocalFile() throws Exception {
        Path oldPath = new Path("/10000_access.log");
        Path newPath = new Path("D://111.txt");
        /**
         * 不加前后两个boolean参数会出现空指针异常;(上面的copyFromLocalFile方法同样有两个参数)
         * 第一个boolean表示,是否删除hdfs的源文件;
         * 第二个boolean表示,是否使用原生的文件系统
         */
        fileSystem.copyToLocalFile(false,oldPath,newPath,true);
    }

    /**
     * 查看某个目录下所有文件
     */
    @Test
    public void listFiles()throws Exception {
        //获取该目录下所有文件状态
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));


        //遍历
        for (FileStatus fileStatus : fileStatuses) {
            //是否是文件夹
//            String isDir = fileStatus.isDirectory() ? "文件夹" : "文件";
            //文件副本数
            short replication = fileStatus.getReplication();
            long len = fileStatus.getLen();
            //全路径
            String path = fileStatus.getPath().toString();

//            System.out.println(isDir + "\t" + replication + "\t" + len + "\t" + path);
        }
    }

    /**
     * 删除文件
     */
    @Test
    public void delete() throws Exception {
        //是否递归
        boolean delete = fileSystem.delete(new Path("/a.txt"), true);
        System.out.println(delete);

    }




    //准备方法
    @Before
    public void setUp() throws Exception {
        //配置
        configuration = new Configuration();
        //连接到hdfs,如果不写第三个权限用户,会报权限错误,
        //该参数值和linux上的角色有关,哪个角色有操作hadoop的权限,用该角色名即可
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration,"root");
    }

    @After
    public void shutdown() {
        fileSystem = null;
        configuration = null;
        System.out.println("HDFSTest shutdown");
    }
}
