package io.polymorphicpanda.autoconfig.compiler;

import javax.annotation.Generated;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementKindVisitor8;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.common.Visibility;
import io.polymorphicpanda.autoconfig.Property;
import com.squareup.javawriter.JavaWriter;

/**
 * @author Ranie Jade Ramiso
 */
public class AutoConfigClassGenerator {
    private final TypeElement element;
    private final Messager messager;
    private final ResourceBundle bundle;

    private static final List<String> HEADERS = Arrays.asList(
        "GENERATED CLASS",
        "DO NOT EDIT - ANY CHANGES WILL BE LOST"
    );

    public AutoConfigClassGenerator(TypeElement element, Messager messager, ResourceBundle bundle) {
        this.element = element;
        this.messager = messager;
        this.bundle = bundle;
    }

    public void generate(Writer writer, String className, ProcessingEnvironment processingEnv) throws IOException {
        final PackageElement packageElement = MoreElements.getPackage(element);
        final Visibility visibility = Visibility.ofElement(element);

        final JavaWriter classWriter = new JavaWriter(writer);

        writeHeaders(classWriter);

        classWriter.emitPackage(packageElement.toString());

        final ClassBlueprintBuilder builder = new ClassBlueprintBuilder();

        for (Element e: element.getEnclosedElements()) {
            e.accept(builder, null);
        }

        final ClassBlueprint blueprint = builder.build();

        final String packageQualifiedName = packageElement.getQualifiedName().toString();

        // generate imports
        for (TypeMirror importType: blueprint.getImports()) {
            if (importType.getKind() == TypeKind.DECLARED) {
                final TypeElement typeElement = MoreTypes.asTypeElement(processingEnv.getTypeUtils(), importType);

                if (typeElement.getKind() == ElementKind.ENUM) {
                    final String importQualifiedName = typeElement.getQualifiedName().toString();
                    final String importSimpleName = typeElement.getSimpleName().toString();

                    // ignore import if same package
                    if (!(packageQualifiedName + "." + importSimpleName).equals(importQualifiedName)) {
                        classWriter.emitImports(importQualifiedName);
                    }
                }
            }
        }

        classWriter.emitEmptyLine();

        Modifier modifier;

        switch (visibility) {
            case PUBLIC:
                modifier = Modifier.PUBLIC;
                break;
            case DEFAULT:
            default:
                modifier = Modifier.DEFAULT;
                break;
        }

        classWriter.emitAnnotation(Generated.class, new String[] {"\"io.polymorphicpanda.autoconfig.AutoConfigProcessor\""});
        classWriter.beginType(
            className, "class", EnumSet.of(modifier, Modifier.FINAL),
            null, element.getSimpleName().toString()
        );

        for (Map.Entry<Property, ExecutableElement> entry: blueprint.getMethods().entrySet()) {
            final Property property = entry.getKey();
            final ExecutableElement executableElement = entry.getValue();

            classWriter.emitAnnotation(Override.class);
            writeMethod(classWriter, executableElement, property, processingEnv);
            classWriter.emitEmptyLine();
        }
        classWriter.endType();
        classWriter.close();
    }

    private void writeHeaders(JavaWriter classWriter) throws IOException {
        for (String header: HEADERS) {
            classWriter.emitSingleLineComment(header);
        }
    }

    private void writeMethod(JavaWriter writer, ExecutableElement element, Property property,
                             ProcessingEnvironment processingEnv) throws IOException {

        final ExecutableType type = MoreTypes.asExecutable(element.asType());
        final TypeMirror returnType = type.getReturnType();
        final Name simpleName = element.getSimpleName();

        final TypeKind returnTypeKind = returnType.getKind();

        writer.beginMethod(
            returnType.toString(), simpleName.toString(), EnumSet.of(Modifier.PUBLIC, Modifier.FINAL)
        );

        writer.emitSingleLineComment("Generated from key '%s'.", property.key());
        if (returnTypeKind.isPrimitive()) {
            writePrimitiveReturn(writer, property, returnTypeKind);
        } else if (returnTypeKind == TypeKind.DECLARED) {
            final TypeElement elementType = MoreTypes.asTypeElement(processingEnv.getTypeUtils(), returnType);

            if (MoreTypes.isTypeOf(String.class, returnType)) {
                writeStringReturn(writer, property);
            } else if (elementType.getKind() == ElementKind.ENUM) {
                writeEnumConstantReturn(writer, property);
            }
        }

        writer.endMethod();
    }

    private void writePrimitiveReturn(JavaWriter writer, Property property, TypeKind returnTypeKind) throws IOException {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("return ");

        if (returnTypeKind == TypeKind.CHAR) {
            buffer.append("'%s'");
        } else {
            buffer.append("%s");
        }

        writer.emitStatement(buffer.toString(), getValue(property.key()));
    }

    private void writeStringReturn(JavaWriter writer, Property property) throws IOException {
        writer.emitStatement("return \"%s\"", getValue(property.key()));
    }

    private void writeEnumConstantReturn(JavaWriter writer, Property property) throws IOException {
        writer.emitStatement("return %s", getValue(property.key()));
    }

    private String getValue(String key) {
        return bundle.getString(key);
    }

    private class ClassBlueprintBuilder extends ElementKindVisitor8<Void, Void> {
        private final Set<TypeMirror> imports = new HashSet<>();
        private final Map<Property, ExecutableElement> methods = new HashMap<>();

        @Override
        public Void visitExecutableAsMethod(ExecutableElement e, Void aVoid) {
            final Property property = e.getAnnotation(Property.class);

            if (property != null && e.getParameters().isEmpty()) {
                final TypeMirror returnType = e.getReturnType();
                imports.add(returnType);

                methods.put(property, e);
            }
            return null;
        }

        public ClassBlueprint build() {
            return new ClassBlueprint(imports, methods);
        }
    }
}
