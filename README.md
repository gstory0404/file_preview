# Flutter文档预览插件

<p>
<a href="https://pub.flutter-io.cn/packages/file_preview"><img src=https://img.shields.io/pub/v/file_preview?color=orange></a>
<a href="https://pub.flutter-io.cn/packages/file_preview"><img src=https://img.shields.io/pub/likes/file_preview></a>
<a href="https://pub.flutter-io.cn/packages/file_preview"><img src=https://img.shields.io/pub/points/file_preview></a>
<a href="https://github.com/gstory0404/file_preview/commits"><img src=https://img.shields.io/github/last-commit/gstory0404/file_preview></a>
<a href="https://github.com/gstory0404/file_preview"><img src=https://img.shields.io/github/stars/gstory0404/file_preview></a>
</p>

<img src="https://github.com/gstory0404/file_preview/blob/master/images/android.gif" width="30%">   <img src="https://github.com/gstory0404/file_preview/blob/master/images/ios.gif" width="30%">

## 简介

使用file_preview可以像在使用Flutter weidget一样在andorid、ios页面中预览doc、docx、ppt、pptx、xls、xlsx、pdf等文件。

## 说明
* andorid使用腾讯[TBS](https://cloud.tencent.com/product/tbs)服务，支持doc、docx、rtf、ppt、pptx、xls、xlsx、xlsm、csv、pdf、txt、epub、chm文件的预览
* ⚠️[关于腾讯浏览服务内核SDK-内核文档能力调整公告](https://mp.weixin.qq.com/s/rmSa4Fs77MDdjFioRKwXPA),x5内核版本不再维护（v1分支），新版插件已切换为[新版浏览服务内核](https://cloud.tencent.com/product/tbs) 使用方法不变
* ios使用WKWebView，WKWebView所支持的均可预览
* 
andorid在线预览时会下载文件至本地再进行预览，文件格式获取url中[Content-Type](https://github.com/gstory0404/file_preview/blob/master/android/src/main/kotlin/com/gstory/file_preview/utils/ext.kt)来判断，同url文件再次预览不再下载直接读取本地缓存，如需重新下载则调用删除缓存方法。
ios不受影响。

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
|more| TBS限制不可预览 | WKWebView支持均可预览 |

## 集成步骤
### 1、pubspec.yaml
```Dart
//v2
file_preview: ^latest

//v1
file_preview:
  git:
    url: https://github.com/gstory0404/file_preview.git
    ref: v1
```

### 2、引入
```Dart
import 'package:file_preview/file_preview.dart';
```

### 3、TBS初始化

由于使用android使用TBS服务，所以必须在FilePreviewWidget使用前完成初始化，不然无法加载。

```dart
 await FilePreview.initTBS(license: "your license");
```

### 3、获取TBS版本

```dart
String version = await FilePreview.tbsVersion();
```

### 4、预览文件

```dart
   //使用前进行判断是否已经初始化
    var isInit = await FilePreview.tbsHasInit();
    if (!isInit) {
        await FilePreview.initTBS();
        return;
    }
```
```dart
//文件控制器
FilePreviewController controller = FilePreviewController();

//切换文件
//path 文件地址 https/http开头、文件格式结尾的地址，或者本地绝对路径
controller.showFile(path);

//文件预览widget
FilePreviewWidget(
    controller: controller,
    width: 400,
    height: 600,
    //path 文件地址 https/http开头、文件格式结尾的地址，或者本地绝对路径
    path: widget.path,
    callBack: FilePreviewCallBack(onShow: () {
      print("文件打开成功");
    }, onDownload: (progress) {
      print("文件下载进度$progress");
    }, onFail: (code, msg) {
      print("文件打开失败 $code  $msg");
    }),
),
```

### 5、删除本地缓存
andorid预览在线文件需要先将文件下载到本地/data/user/0/包名/files/file_preview/目录下，
可以通过以下方法删除缓存，仅andorid生效
```dart
await FilePreview.deleteCache();
```

### 6、http配置
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


