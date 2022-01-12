# Flutter文档预览插件

<p>
<a href="https://pub.flutter-io.cn/packages/file_preview"><img src=https://img.shields.io/badge/file_preview-v0.0.3-success></a>
</p>

<img src="https://github.com/gstory0404/file_preview/blob/master/images/android.gif" width="30%">   <img src="https://github.com/gstory0404/file_preview/blob/master/images/ios.gif" width="30%">

## 简介

使用file_preview可以像在使用Flutter weidget一样在andorid、ios页面中预览doc、docx、ppt、pptx、xls、xlsx、pdf等文件。

## 说明
* andorid使用腾讯TBS服务，支持doc、docx、ppt、pptx、xls、xlsx、pdf、txt、epub文件的预览
* ios使用WKWebView，WKWebView所支持的均可预览

## 版本更新

[更新日志](https://github.com/gstory0404/file_preview/blob/master/CHANGELOG.md)

## 集成步骤
### 1、pubspec.yaml
```Dart
file_preview: ^0.0.3
```
### 2、引入
```Dart
import 'package:file_preview/file_preview.dart';
```

### 3、使用
由于使用android使用TBS服务，所以必须在FilePreviewWidget使用前完成初始化，不然无法加载。
如本地无TBS不存在会在初始化时进行下载，会耗时3-30秒左右
```dart
await FilePreview.initTBS();
```

andorid在build.gradle中开启删除无用资源，打包后可能导致apk无法加载TBS内核库失败，可以如下设置
```dart
buildTypes {
        release {
            //关闭删除无用资源
            shrinkResources false
            //关闭删除无用代码
            minifyEnabled false
            zipAlignEnabled true
        }
    }
```

使用
```dart
 FilePreviewWidget(
          width: 400,//宽
          height: 700,//高
          path: "",//本地路径或者http链接
        )
```

### 4、http配置

* Android

android/app/src/main/res/xml下新建network_config.xml
```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true"/>
</network-security-config>
```

在android/app/src/main/AndroidManifest.xml中使用
```
<application
       android:networkSecurityConfig="@xml/network_config">
```

* ios

ios/Runner/Info.plist中
```
 <key>NSAppTransportSecurity</key>
        <dict>
            <key>NSAllowsArbitraryLoads</key>
            <true/>
        </dict>
```


## 插件链接

|插件|地址|
|:----|:----|
|穿山甲广告插件|[flutter_unionad](https://github.com/gstory0404/flutter_unionad)|
|腾讯优量汇广告插件|[flutter_tencentad](https://github.com/gstory0404/flutter_tencentad)|
|聚合广告插件|[flutter_universalad](https://github.com/gstory0404/flutter_universalad)|
|百度百青藤广告插件|[flutter_baiduad](https://github.com/gstory0404/flutter_baiduad)|
|字节穿山甲内容合作插件|[flutter_pangrowth](https://github.com/gstory0404/flutter_pangrowth)|
|文档预览插件|[file_preview](https://github.com/gstory0404/file_preview)|

## 联系方式
* Email: gstory0404@gmail.com
* Blog：https://gstory.vercel.app/

* QQ群: <a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=4j2_yF1-pMl58y16zvLCFFT2HEmLf6vQ&jump_from=webapi"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="649574038" title="flutter交流"></a>


