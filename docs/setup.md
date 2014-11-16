AutoConfig can be used with build tools that can work with maven dependencies. Make sure that `autoconfig.jar` and
`autoconfig-compiler.jar` is in your compile classpath, the rest is magic. You need at least java `1.6`.

## Dependencies
```xml
<dependency>
    <groupId>io.polymorphicpanda</groupId>
    <artifactId>autoconfig</artifactId>
    <version>${autoconfig.version}</version>
</dependency>
<dependency>
    <groupId>io.polymorphicpanda</groupId>
    <artifactId>autoconfig-compiler</artifactId>
    <version>${autoconfig.version}</version>
    <optional>true</optional>
</dependency>
```
