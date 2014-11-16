package io.polymorphicpanda.autoconfig.compiler;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import io.polymorphicpanda.autoconfig.Property;

/**
 * @author Ranie Jade Ramiso
 */
public class ClassBlueprint {
    private final Set<TypeMirror> imports;
    private final Map<Property, ExecutableElement> methods;

    public ClassBlueprint(Set<TypeMirror> imports, Map<Property, ExecutableElement> methods) {
        this.imports = imports;
        this.methods = methods;
    }

    public Set<TypeMirror> getImports() {
        return Collections.unmodifiableSet(imports);
    }

    public Map<Property, ExecutableElement> getMethods() {
        return Collections.unmodifiableMap(methods);
    }
}
