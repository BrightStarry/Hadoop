package com.zx.hadoop.hive;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;

/**
 * author:ZhengXing
 * datetime:2017-12-31 16:31
 * 使用Thrift直接连接hive.
 */
public class ThriftClient {

    public static void main(String[] args) {
        //创建socket
        TSocket tSocket = new TSocket("106.14.7.29", 10000);

        //创建一个协议
        TProtocol tProtocol = new TBinaryProtocol(tSocket);

        //创建hiveClient
        // TODO 没有这个类.作罢
    }
}
