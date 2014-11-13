# AutoConfig
Generate config classes from properties file.

---

## Usage
Define your interface.
```java
package com.autoconfig.example
// ...
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
Create your properties file.
```ini
config.some.int=1
config.say.hello=Hello
```

Make sure `autoconfig.jar` and `autoconfig-compiler.jar` is on your compile classpath.

A class named `<interface-name>$$AutoConfig` will be generated at compile time and will be placed on the same package as the interface.
```java
package com.autoconfig.example
// ...
public final class AppConfig$$AutoConfig implements AppConfig {
    @Override
    public final int getSomeInt() {
        return 1;
    }
    
    @Override
    public final String getSayHello() {
        return "Hello";
    }
}
```

Now, have fun.
```java
public static void main(String[] args) {
    AppConfig config = new AppConfig$$AutoConfig();
}
```

## Supported Types
1. All primitive types.
2. java.lang.String

## Maven
```xml
<dependency>
    <groupId>com.raniejaderamiso</groupId>
    <artifactId>autoconfig</artifactId>
    <version>${autoconfig.version}</version>
</dependency>
<dependency>
    <groupId>com.raniejaderamiso</groupId>
    <artifactId>autoconfig-compiler</artifactId>
    <version>${autoconfig.version}</version>
    <optional>true</optional>
</dependency>
```
