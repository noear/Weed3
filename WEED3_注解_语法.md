# Weed3注解语法


### 例1：变量占位符模式
```java
@Sql("select * from where id=@{id}")
```


### 例2：位置占符模式
```java
@Sql("select * from where id=?")
```

