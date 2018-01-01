package com.zx.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Random;

/**
 * author:ZhengXing
 * datetime:2017-11-28 19:36
 * 使用MapReduce开发单词统计
 */
public class WordCountApp {

    /**
     * Map:读取输入的文件
     *
     * LongWritable: 输入K1.一个文本中的x-x+1000这样的索引,表示取文本第x到x+1000的字符
     * Text: 输入V1,文本数据
     * Text:输出K2,每个单词
     * LongWritable:输出V2,每个单词出现的次数
     *
     * Mapper类:
     * setup()任务开始时执行;
     * cleanup()任务结束时执行;
     * map()任务主体;
     * run()模版方法,依次调用setup()map()cleanup()
     *
     */
    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        LongWritable one = new LongWritable(1);

        //重写map方法
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //输入的是一行文本,转为string
            String line = value.toString();
            //将文本按照空格分割成若干单词
            String[] words = line.split(" ");

            //遍历每个单词
            for (String word : words) {
                //通过上下文把处理结果输出
                //表示该单词,出现了一次
                //该输出也就是Reduce的输入
                context.write(new Text(word),one);
            }
        }
    }

    /**
     * Reduce:归并操作,累加每个单词的出现次数,统计总次数
     *
     * Text:Mapper的K2,作为输入的key
     * LongWritable:Mapper的V2,作为输入的value
     * Text: 输出的Key,就是每个单词
     * LongWritable: 输出的Value,就是每个单词的总次数
     */
    public static class MyReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

        /**
         * 重写reduce方法
         * @param key
         * @param values 之所以是集合,是因为如果出现两次hello单词,就有两个value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            //遍历每个值,累计总次数
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }
            //输出结果,key还是那个单词,value就是其出现的总次数
            context.write(key,new LongWritable(sum));
        }
    }

    /**
     * Partitioner:指定MapTask任务的输出交由哪个ReduceTask处理
     * 其泛型对应 MapTask的输出
     */
    public static class MyPartitioner extends Partitioner<Text,LongWritable>{

        @Override
        public int getPartition(Text key, LongWritable value, int i) {
            return (int)(Math.random()*2);
        }
    }
    /**
     * 定义Driver:封装MapReduce的所有信息
     */
    public static void main(String[] args) throws Exception {
        //创建配置类
        Configuration configuration = new Configuration();

        //如果输出路径下,有上次执行完的输出文件,再次执行会报错
        //所以判断是否存在文件,存在的话删除一下
        Path outputPath = new Path(args[1]);
        FileSystem fileSystem = FileSystem.get(configuration);
        if (fileSystem.exists(outputPath)){
            fileSystem.delete(outputPath, true);
            System.out.println("输出目录存在文件,已经删除.");
        }

        //创建任务,使用配置类和job名字
        Job job = Job.getInstance(configuration, "wordCount");

        //设置其主类(处理类)
        job.setJarByClass(WordCountApp.class);

        //设置作业处理的输入路径,从外部传入
        //FileInputFormat需要使用mapreduce包下的,mapred包下是旧的
        //传入job,和文件路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));

        //设置map相关的参数
        //设置mapper的类类型
        job.setMapperClass(MyMapper.class);
        //设置mapper输出的key的类类型
        job.setMapOutputKeyClass(Text.class);
        //设置mapper输出的value的类类型
        job.setMapOutputValueClass(LongWritable.class);

        //设置Reduce相关参数
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置combiner处理类,用于在本地提前处理相同单词的次数累加,其逻辑和reduce一样
        job.setCombinerClass(MyReducer.class);

        //设置Partitioner,指定任务分配
        job.setPartitionerClass(MyPartitioner.class);
        //设置x个reduce,和partitioner处理类中的任务索引i大小一致
        job.setNumReduceTasks(2);

        //设置作业处理的输出路径,从外部传入
        FileOutputFormat.setOutputPath(job,outputPath);

        //退出
        System.exit(job.waitForCompletion(true) ? 0: 1);
    }
}
