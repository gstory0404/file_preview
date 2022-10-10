part of 'file_preview.dart';

/// @Author: gstory
/// @CreateDate: 2022/10/8 14:26
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

class FilePreviewController {
  MethodChannel? _methodChannel;

  init(MethodChannel? methodChannel) {
    _methodChannel = methodChannel;
  }

  ///切换文件
  ///
  /// [path] 文件地址 https/http开头、文件格式结尾的地址，或者本地绝对路径
  void showFile(String path) {
    print("切换文件  $path");
    _methodChannel?.invokeMethod("showFile", path);
  }
}
