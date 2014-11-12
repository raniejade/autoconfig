package com.raniejaderamiso.autoconfig.compiler;

import javax.annotation.Generated;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.ResourceBundle;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.common.Visibility;
import com.raniejaderamiso.autoconfig.Property;
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

        classWriter.emitAnnotation(Generated.class, new String[] {"\"com.raniejaderamiso.autoconfig.AutoConfigProcessor\""});
        classWriter.beginType(
            className, "class", EnumSet.of(modifier, Modifier.FINAL),
            null, element.getSimpleName().toString()
        );

        List<? extends Element> members = processingEnv.getElementUtils().getAllMembers(element);

        for (Element member: members) {
            final TypeMirror typeMirror = member.asType();

            if (typeMirror.getKind() == TypeKind.EXECUTABLE) {
                final Property property = member.getAnnotation(Property.class);

                if (property != null) {
                    classWriter.emitAnnotation(Override.class);
                    writeMethod(
                        classWriter, MoreElements.asExecutable(member), MoreTypes.asExecutable(typeMirror), property
                    );
                    classWriter.emitEmptyLine();
                }
            }
        }
        classWriter.endType();
        classWriter.close();
    }

    private void writeHeaders(JavaWriter classWriter) throws IOException {
        for (String header: HEADERS) {
            classWriter.emitSingleLineComment(header);
        }
    }

    private void writeMethod(JavaWriter writer, ExecutableElement element,
                             ExecutableType type, Property property) throws IOException {

        final TypeMirror returnType = type.getReturnType();
        final Name simpleName = element.getSimpleName();

        final TypeKind returnTypeKind = returnType.getKind();

        writer.beginMethod(
            returnType.toString(), simpleName.toString(), EnumSet.of(Modifier.PUBLIC, Modifier.FINAL)
        );

        writer.emitSingleLineComment("Generated from key '%s'.", property.key());
        if (returnTypeKind.isPrimitive()) {
            writePrimitiveReturn(writer, property, returnTypeKind);
        } else if (returnTypeKind == TypeKind.DECLARED && MoreTypes.isTypeOf(String.class, returnType)) {
            writeStringReturn(writer, property);
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

    private String getValue(String key) {
        return bundle.getString(key);
    }
}
