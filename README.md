# AutoConfig
Generate config classes from properties file. 

---

```java
    @AutoConfig(
            resourceDir = StandardLocation.CLASS_PATH,
            packageName = "com.example",
            filename = "config.properties
    )
    public interface ApplicationConfig {
        @Property(key = "config.some")
        int getSomeConfig();

        @Property(key = "config.say.hello")
        String getSayHello();
    }
```

*config.properties*

```ini
config.some=1
config.say.hello=Hello
```

## TODO
