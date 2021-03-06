#### Flink 分布式计算框架
* 开源流式处理框架
* 批处理: 在预先定义好的时候执行
* 无界的,输入处一直会有新数据输入  

* 安装,解压即可

* 该目录修改默认web界面端口conf/flink-conf.yaml (我目前改为8086)
* 运行 ./bin/start-local.sh 启动.
  输入jps可以查看到jobManager
* 启动失败可在根目录查看到失败日志,通常是内存不足
* 默认web界面端口8081

* 运行word count
>
    进入根目录运行-例子来自官网
    
    ./bin/flink run ./examples/batch/WordCount.jar \
      --input file:///zx4/Dockerfile --output file:///zx4/wordcount_out
>
* 其单词统计源码可在github中找到 https://github.com/apache/flink
> https://github.com/apache/flink/blob/master/flink-examples/flink-examples-batch/src/main/scala/org/apache/flink/examples/scala/wordcount/WordCount.scala

####  Beam 大数据处理神器
* 将批处理/流处理运行在不同的引擎上

* 需要JDK MAVEN
* 运行如下mvn 官网的get started java中的例子
>
    mvn archetype:generate \
          -DarchetypeGroupId=org.apache.beam \
          -DarchetypeArtifactId=beam-sdks-java-maven-archetypes-examples \
          -DarchetypeVersion=2.2.0 \
          -DgroupId=org.example \
          -DartifactId=word-count-beam \
          -Dversion="0.1" \
          -Dpackage=org.apache.beam.examples \
          -DinteractiveMode=false
>
* 在目录中构建了word-count-beam.
* 进入该目录根目录,输入 tree(如果没有该命令,可用yum install tree 安装),查看项目结构
* 可在github查看其源码https://github.com/apache/beam
* 如果要运行这个项目,可运行在不同的引擎上,例如spark,flink等
    * direct直接运行
    >
        mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.WordCount \
             -Dexec.args="--inputFile=/zx4/Dockerfile --output=/zx4/wordcount_out" -Pdirect-runner
    >
    * 使用spark运行 (运行时可在4040端口看到web界面),如果提示内存不足,可在末尾增加命令参数-Dspark.testing.memory=1073741824 申请更多内存
    >
        mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.WordCount \
             -Dexec.args="--runner=SparkRunner --inputFile=/zx4/Dockerfile --output=/zx4/wordcount_out"  \
             -Pspark-runner \
             
    >
    * 使用flink运行
    >
        mvn package exec:java -Dexec.mainClass=org.apache.beam.examples.WordCount \
             -Dexec.args="--runner=FlinkRunner --flinkMaster=127.0.0.1 \
             --filesToStage=target/word-count-beam-bundled-0.1.jar \
             --inputFile=/zx4/Dockerfile  --output=/zx4/wordcount_out" -Pflink-runner
    >
    * 运行成功后,输出目录会有多个out文件,可以使用more out*查看所有out文件
