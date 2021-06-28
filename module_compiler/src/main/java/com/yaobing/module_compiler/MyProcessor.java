package com.yaobing.module_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yaobing.module_apt.Router;
import com.yaobing.module_apt.helper.RouterActivityModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({ "com.yaobing.module_apt.Router"})
@AutoService(Processor.class)
class MyProcessor extends AbstractProcessor {

    private void addActivityModel(List<RouterActivityModel> mRouterActivityModels, String viewCode, TypeElement element) {
        RouterActivityModel viewCodeRouterActivityModel = new RouterActivityModel();
        viewCodeRouterActivityModel.setElement(element);
        viewCodeRouterActivityModel.setActionName(viewCode);
        viewCodeRouterActivityModel.setNeedBind(false);
        mRouterActivityModels.add(viewCodeRouterActivityModel);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        String CLASS_NAME = "IntentRouter";
        String PACKAGE_NAME = null;
        TypeSpec.Builder tb = TypeSpec.classBuilder(CLASS_NAME).addModifiers(new Modifier[]{Modifier.PUBLIC, Modifier.FINAL}).addSuperinterface(ClassName.get("com.supcon.common.com_router.api", "IRouter", new String[0])).addJavadoc("@API intent router created by apt\n支持组件化多模块\nadd by wangshizhan\n", new Object[0]);
        com.squareup.javapoet.CodeBlock.Builder staticBuilderGo = CodeBlock.builder();
        ClassName routerManagerClassName = ClassName.get("com.supcon.common.com_router.util", "RouterManager", new String[0]);
        com.squareup.javapoet.MethodSpec.Builder methodBuilder1 = MethodSpec.methodBuilder("go").addJavadoc("@created by apt \n", new Object[0]).addModifiers(new Modifier[]{Modifier.PUBLIC, Modifier.STATIC}).addParameter(ClassName.get("android.content", "Context", new String[0]), "context", new Modifier[0]).addParameter(String.class, "name", new Modifier[0]).addParameter(ClassName.get("android.os", "Bundle", new String[0]), "extra", new Modifier[0]);
        List<ClassName> mList = new ArrayList();
        com.squareup.javapoet.CodeBlock.Builder blockBuilderGo = CodeBlock.builder();
        ClassName mIntentClassName = ClassName.get("android.content", "Intent", new String[0]);
        blockBuilderGo.add("$T intent =new $T();\n", new Object[]{mIntentClassName, mIntentClassName});
        blockBuilderGo.add("if(extra != null)\n", new Object[0]);
        blockBuilderGo.addStatement("\tintent.putExtras(extra)", new Object[0]);
        blockBuilderGo.beginControlFlow(" switch (name)", new Object[0]);
        ArrayList mRouterActivityModels = new ArrayList();

        try {
            Iterator var13 = ElementFilter.typesIn(roundEnvironment.getElementsAnnotatedWith(Router.class)).iterator();

            while(true) {
                TypeElement element;
                ClassName currentType;
                do {
                    if (!var13.hasNext()) {
                        if (mRouterActivityModels.size() == 0) {
                            return true;
                        }

                        var13 = mRouterActivityModels.iterator();

                        while(var13.hasNext()) {
                            RouterActivityModel item = (RouterActivityModel)var13.next();
                            blockBuilderGo.add("\tcase $S: \n", new Object[]{item.getActionName()});
                            blockBuilderGo.addStatement("\t\tintent.setClass(context, $T.class)", new Object[]{item.getElement()});
                            blockBuilderGo.addStatement("\t\tbreak", new Object[0]);
                            staticBuilderGo.addStatement("$T.getInstance().register(\"$L\", $T.class)", new Object[]{routerManagerClassName, item.getActionName(), item.getElement().asType()});
                        }

                        blockBuilderGo.add("default: \n", new Object[0]);
                        blockBuilderGo.addStatement("\t\t$T routerManager = $T.getInstance()", new Object[]{routerManagerClassName, routerManagerClassName});
                        blockBuilderGo.addStatement("\t\tClass destinationClass = routerManager.getDestination(name)", new Object[0]);
                        blockBuilderGo.addStatement("\t\tif(destinationClass == null) return", new Object[0]);
                        blockBuilderGo.addStatement("\t\tintent.setClass(context, destinationClass)", new Object[0]);
                        blockBuilderGo.addStatement("\t\tbreak", new Object[0]);
                        blockBuilderGo.endControlFlow();
                        blockBuilderGo.addStatement("context.startActivity(intent)", new Object[0]);
                        methodBuilder1.addCode(blockBuilderGo.build());
                        tb.addStaticBlock(staticBuilderGo.build());
                        tb.addMethod(methodBuilder1.build());
                        tb.addMethod(MethodSpec.methodBuilder("go").addJavadoc("@created by apt", new Object[0]).addModifiers(new Modifier[]{Modifier.PUBLIC, Modifier.STATIC}).addParameter(ClassName.get("android.content", "Context", new String[0]), "context", new Modifier[0]).addParameter(String.class, "name", new Modifier[0]).addCode("go(context, name, null);\n", new Object[0]).build());
                        tb.addMethod(MethodSpec.methodBuilder("setup").addJavadoc("@created by apt", new Object[0]).addModifiers(new Modifier[]{Modifier.PUBLIC, Modifier.STATIC}).build());
                        JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, tb.build()).build();
                        javaFile.writeTo(processingEnv.getFiler());
                        return true;
                    }

                    element = (TypeElement)var13.next();
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "正在处理: " + element.toString());
                    currentType = ClassName.get(element);
                } while(mList.contains(currentType));

                mList.add(currentType);
                RouterActivityModel mRouterActivityModel = new RouterActivityModel();
                mRouterActivityModel.setElement(element);
                mRouterActivityModel.setActionName(((Router)element.getAnnotation(Router.class)).value());
                mRouterActivityModel.setNeedBind(false);
                mRouterActivityModels.add(mRouterActivityModel);
                String viewCode = ((Router)element.getAnnotation(Router.class)).viewCode();
                if (!"unknown".equals(viewCode)) {
                    if (viewCode.contains(",")) {
                        String[] viewCodes = viewCode.split(",");
                        String[] var19 = viewCodes;
                        int var20 = viewCodes.length;

                        for(int var21 = 0; var21 < var20; ++var21) {
                            String vc = var19[var21];
                            this.addActivityModel(mRouterActivityModels, vc, element);
                        }
                    } else {
                        this.addActivityModel(mRouterActivityModels, viewCode, element);
                    }
                }

                if (PACKAGE_NAME == null) {
                    String temp = element.getEnclosingElement().toString();
                    if (temp.contains(".ui")) {
                        PACKAGE_NAME = temp.substring(0, temp.lastIndexOf(".ui"));
                    } else {
                        PACKAGE_NAME = temp;
                    }
                }
            }
        } catch (FilerException var23) {
        } catch (IOException var24) {
            var24.printStackTrace();
        } catch (Exception var25) {
            var25.printStackTrace();
        }
        return true;
    }
}
