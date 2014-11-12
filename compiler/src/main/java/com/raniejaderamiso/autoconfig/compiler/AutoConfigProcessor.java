package com.raniejaderamiso.autoconfig.compiler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.auto.common.MoreElements;
import com.raniejaderamiso.autoconfig.AutoConfig;

import static javax.tools.Diagnostic.Kind;

/**
 * @author Ranie Jade Ramiso
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.raniejaderamiso.autoconfig.AutoConfig")
public class AutoConfigProcessor extends AbstractProcessor implements Messager {

    public static final String GENERATED_CLASS_SUFFIX = "$$AutoConfig";
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            if (!roundEnv.processingOver()) {
                doProcess(roundEnv);
            }
        } catch (Throwable throwable) {
            printException(Kind.ERROR, throwable);
        }
        return true;
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg) {
        getMessager().printMessage(kind, msg);
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg, Element e) {
        getMessager().printMessage(kind, msg, e);
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
        getMessager().printMessage(kind, msg, e, a);
    }

    @Override
    public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
        getMessager().printMessage(kind, msg, e, a, v);
    }

    private void doProcess(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoConfig.class);

        for (Element element: elements) {
            if (isInterface(element)) {
                final TypeElement typeElement = MoreElements.asType(element);
                if (isTopLevelType(typeElement)) {
                    final AutoConfig config = element.getAnnotation(AutoConfig.class);

                    processElement(typeElement, config);
                } else {
                    printMessage(Kind.WARNING, "Interface must be top level", typeElement);
                }
            } else {
                printMessage(Kind.WARNING, "Only interfaces are supported.", element);
            }
        }
    }

    private void processElement(TypeElement element, AutoConfig config) {
        final Filer filer = processingEnv.getFiler();

        try {
            final ResourceBundle bundle = getBundle(filer,config);

            final JavaFileObject fileObject = filer.createSourceFile(getGeneratedClassQualifiedName(element));
            final String className = element.getSimpleName().toString() + GENERATED_CLASS_SUFFIX;

            final AutoConfigClassGenerator generator = new AutoConfigClassGenerator(element, this, bundle);

            final Writer writer = fileObject.openWriter();

            generator.generate(writer, className, processingEnv);
        } catch (IOException e) {
            printException(Kind.ERROR, e);
        }
    }

    private String getGeneratedClassQualifiedName(TypeElement element) {
        return element.getQualifiedName().toString() + GENERATED_CLASS_SUFFIX;
    }

    private boolean isInterface(Element element) {
        return element.getKind().isInterface();
    }

    private boolean isTopLevelType(TypeElement element) {
        return element.getNestingKind() == NestingKind.TOP_LEVEL;
    }

    private Messager getMessager() {
        return processingEnv.getMessager();
    }

    private void printException(Kind kind, Throwable throwable) {
        final StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        printMessage(kind, writer.getBuffer().toString());
    }

    private ResourceBundle getBundle(Filer filer, AutoConfig config) throws IOException {

        // ? annotation processor path ?
        final FileObject fileObject = filer.getResource(config.resourceDir(), config.packageName(), config.filename());

        try (InputStream inputStream = fileObject.openInputStream()) {
            return new PropertyResourceBundle(inputStream);
        }
    }
}
