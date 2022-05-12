part of 'file_preview.dart';

/// @Author: gstory
/// @CreateDate: 2022/5/12 14:15
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

///显示
typedef OnShow = void Function();

///失败
typedef OnFail = void Function(int code, dynamic message);

///下载进入
typedef OnDownload = void Function(int code);

class FilePreviewCallBack {
  OnShow? onShow;
  OnFail? onFail;
  OnDownload? onDownload;

  FilePreviewCallBack({this.onShow, this.onFail, this.onDownload});
}
