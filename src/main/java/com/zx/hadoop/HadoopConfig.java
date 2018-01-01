package com.zx.hadoop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;
import org.springframework.data.hadoop.fs.FileSystemFactoryBean;

/**
 * author:ZhengXing
 * datetime:2017-12-29 21:42
 * hadoop 配置
 */

@EnableHadoop
@Configuration
public class HadoopConfig extends SpringHadoopConfigurerAdapter {

    @Autowired
    private org.apache.hadoop.conf.Configuration configuration;

    @Bean
    public FileSystemFactoryBean fileSystemFactoryBean() {
        FileSystemFactoryBean fileSystemFactoryBean = new FileSystemFactoryBean();
        fileSystemFactoryBean.setConfiguration(configuration);
        fileSystemFactoryBean.setUser("root");
        return fileSystemFactoryBean;
    }


    @Override
    public void configure(HadoopConfigConfigurer config) throws Exception {
        config
                .fileSystemUri("hdfs://hadoop000:8020");//HDFS地址
    }
}
