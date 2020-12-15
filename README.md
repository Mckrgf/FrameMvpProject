# FrameMvpProject
框架
需要在app目录下gradle设置java版本为1.8

需要统一最低、最高android版本配置

kotlin项目用kapt，java项目也建议添加kotlin插件（annotationprocess命令即可）

由于support library和androidx不能共存，特设两个版本

1.1.7为Android Support库使用

1.1.8为AndroidX库使用

集成步骤

1.添加依赖（app的gradle下）
implementation 'com.github.Mckrgf.FrameMvpProject:module_middleware:1.1.8'
implementation 'com.github.Mckrgf.FrameMvpProject:module_common_view:1.1.8'
implementation 'com.github.Mckrgf.FrameMvpProject:module_apt:1.1.8'
kapt 'com.github.Mckrgf.FrameMvpProject:module_compiler:1.1.8'
android节点下
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
根节点下
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'

2.项目节点下添加maven库
        maven {
            url 'https://maven.google.com/'
            name 'Google'
            //chartview need
            maven { url 'https://jitpack.io' }
            mavenCentral() // add repository
            
3.sync

4.写一个接口，里面是你的网络请求方法。加注解contractfactory

5.写一个接口，networkAPI。管理所有网络请求。用apifactory注解。

6.build

7.Presenter继承基类，实现相关方法。

8.activity中增加注解presenter。实现契约类.view()方法

9.实现逻辑



