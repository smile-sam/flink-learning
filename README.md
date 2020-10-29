# flink-learning
## ERROR
####
Caused by: org.apache.logging.log4j.LoggingException: log4j-slf4j-impl cannot be present with log4j-to-slf4j

需要引入
```
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
 </dependency>
```

#### Specifying keys via field positions is only valid for tuple data types. Type: GenericType<scala.Tuple2>
```
使用api错误 引用错误
应该import org.apache.flink.api.java.tuple.Tuple2，结果import scala.Tuple2。
```
