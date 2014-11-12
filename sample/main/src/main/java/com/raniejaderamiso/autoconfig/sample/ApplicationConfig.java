package com.raniejaderamiso.autoconfig.sample;

import javax.tools.StandardLocation;

import com.raniejaderamiso.autoconfig.AutoConfig;
import com.raniejaderamiso.autoconfig.Property;

/**
 * @author Ranie Jade Ramiso
 */
@AutoConfig(
    resourceDir = StandardLocation.CLASS_PATH,
    filename = "config.properties"
)
public interface ApplicationConfig {
    @Property(key = "config.int")
    int getInteger();

    @Property(key = "config.long")
    long getLong();

    @Property(key = "config.double")
    double getDouble();

    @Property(key = "config.char")
    char getChar();

    @Property(key = "config.boolean")
    boolean getBoolean();

    @Property(key = "config.string")
    String getString();
}
