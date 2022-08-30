# Flutter文档预览插件

<p>
<a href="https://pub.flutter-io.cn/packages/file_preview"><img src=https://img.shields.io/badge/file_preview-v1.1.1-success></a>
</p>

<img src="https://github.com/gstory0404/file_preview/blob/master/images/android.gif" width="30%">   <img src="https://github.com/gstory0404/file_preview/blob/master/images/ios.gif" width="30%">

## 简介

使用file_preview可以像在使用Flutter weidget一样在andorid、ios页面中预览doc、docx、ppt、pptx、xls、xlsx、pdf等文件。

## 说明
* andorid使用腾讯[TBS](https://x5.tencent.com/)服务，支持doc、docx、ppt、pptx、xls、xlsx、pdf、txt、epub文件的预览
* ios使用WKWebView，WKWebView所支持的均可预览

在线预览的文件链接必须是.pdf等文件格式结尾的链接可正常预览

## 版本更新

[更新日志](https://github.com/gstory0404/file_preview/blob/master/CHANGELOG.md)

## 支持格式
|格式|android|ios|
|:----|:----:|:----:|
|.doc| ✅ | ✅ |
|.docx| ✅ | ✅ |
|.ppt| ✅ | ✅ |
|.pptx| ✅ | ✅ |
|.xls| ✅ | ✅ |
|.xlsx| ✅ | ✅ |
|.pdf|✅ | ✅ |


## 常见问题
1、TBS初始化失败  
https://docs.qq.com/doc/DYW9QdXJNWFZnbVdz 

## 集成步骤
### 1、pubspec.yaml
```Dart
file_preview: ^1.1.1
```
### 2、引入
```Dart
import 'package:file_preview/file_preview.dart';
```

### 3、TBS初始化

由于使用android使用TBS服务，所以必须在FilePreviewWidget使用前完成初始化，不然无法加载。
如本地无TBS不存在会在初始化时进行下载，会耗时3-30秒左右

以下二选一，自动初始化失败时可进行手动初始化
#### 1、 手动初始化
```dart
await FilePreview.initTBS();
```

#### 2、异步自动初始化，无需在flutter中await等待

android目录下新建App继承FilePreviewApp
```dart

class App : FilePreviewApp() {
}
```
AndroidManifest.xml文件中修改
```dart
   <application
        ...
        android:name=".App"
        ...>
    </application>
```


### 3、使用
andorid在build.gradle中开启删除无用资源，打包后如果遇到apk无法加载TBS内核库，可以尝试如下设置（非必须）
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

#### 1、预览文件
```dart
  //使用前进行判断是否已经初始化
    var isInit = await FilePreview.tbsHasInit();
    if (!isInit) {
        await FilePreview.initTBS();
        return;
    }
```
```dart
     FilePreviewWidget(
              width: 400,//宽
              height: 700,//高
              path: "",//file path or http url
            )
```

#### 2、删除本地缓存
andorid预览在线文件需要先将文件下载到本地/data/user/0/com.gstory.file_preview_example/files/file_preview/目录下，
可以通过以下方法删除缓存
```dart
await FilePreview.deleteCache();
```

### 4、http配置
高版本andorid、ios默认禁用http，可以设置打开防止文件加载失败

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

## 测试文件
https://gstory.vercel.app/ceshi/ceshi.docx         
https://gstory.vercel.app/ceshi/ceshi.pdf       
https://gstory.vercel.app/ceshi/ceshi.xlsx        
https://gstory.vercel.app/ceshi/ceshi.txt    
https://gstory.vercel.app/ceshi/ceshi.pptx      


## 插件链接

|插件|地址|
|:----|:----|
|字节-穿山甲广告插件|[flutter_unionad](https://github.com/gstory0404/flutter_unionad)|
|腾讯-优量汇广告插件|[flutter_tencentad](https://github.com/gstory0404/flutter_tencentad)|
|百度-百青藤广告插件|[baiduad](https://github.com/gstory0404/baiduad)|
|字节-Gromore聚合广告|[gromore](https://github.com/gstory0404/gromore)|
|Sigmob广告|[sigmobad](https://github.com/gstory0404/sigmobad)|
|聚合广告插件(迁移至GTAds)|[flutter_universalad](https://github.com/gstory0404/flutter_universalad)|
|GTAds聚合广告|[GTAds](https://github.com/gstory0404/GTAds)|
|字节穿山甲内容合作插件|[flutter_pangrowth](https://github.com/gstory0404/flutter_pangrowth)|
|文档预览插件|[file_preview](https://github.com/gstory0404/file_preview)|
|滤镜|[gpu_image](https://github.com/gstory0404/gpu_image)|

## 联系方式
* Email: gstory0404@gmail.com
* Blog：https://www.gstory.cn/

* QQ群: <a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=4j2_yF1-pMl58y16zvLCFFT2HEmLf6vQ&jump_from=webapi"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="649574038" title="flutter交流"></a>


