# XML configuration to Java classes converter

This project converts XML configuration (Spring beans definitions and Hibernate ORM mapping) to Java classes.

## Hibernate converter
```java
public class HibernateUsage {
    public static void main(String[] args){
        File baseDir = new File("<path-to-project>\\src\\main\\java");
        File outputBaseDir = new File("<path-to-output-dir>");
        HibernateConfigConverter hibernateConfigConverter = new HibernateConfigConverter(outputBaseDir);
        hibernateConfigConverter.convert(baseDir, ".*\\.hbm\\.xml");  
    }
}
```

## Spring beans converter
```java
public class SpringBeansUsage {
    public static void main(String[] args){
        File baseFile = new File("<path-to-project>src\\main\\java");
        SpringBeansConverter springBeansConverter = new SpringBeansConverter(Arrays.asList(
                new File("<path-to-project>\\src\\main\\java"),
                new File("<path-to-subproject>\\src\\main\\java"),
                new File("<path-to-subproject>\\src\\main\\java")
                //... as many subprojects to lookup as you have
        ));
        springBeansConverter.convert(baseFile, ".*\\.xml");        
    }
}
```