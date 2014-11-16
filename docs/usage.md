## Define your config interface.
```java
package io.polymorphicpanda.autoconfig.example

import io.polymorphicpanda.autoconfig.example.somepackage.SomeEnum

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
    
    @Property(key = "config.some.enum")
    SomeEnum getSomeEnum();
}
```

## Create your properties file.

```ini
config.some.int=1
config.say.hello=Hello
config.some.enum=SomeEnum.SOME_VALUE
```

Make sure you have done everything in [Setting Up](#setup.md)

## Have fun
A class will be generated at compile time which can be loaded via `AutoConfigLoader`.

```java
public static void main(String[] args) {
    AppConfig config = AutoConfigLoader.load(AppConfig.class);
}
```
