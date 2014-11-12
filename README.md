# AutoConfig
Generate config classes from properties file.

```java
@AutoConfig(
    resourceDir = StandardLocation.CLASS_PATH,
    packageName = "com.example",
    filename = "config.properties"
)
public interface AppConfig {
    @Property(key = "config.some.int")
    int getSomeInt();
    
    @Property(key = "config.say.hello")
    String getSayHello();
}
```

## TODO
