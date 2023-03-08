part of 'file_preview.dart';

/// @Author: gstory
/// @CreateDate: 2021/12/27 10:21 上午
/// @Email gstory0404@gmail.com
/// @Description: 文件预览view

class FilePreviewWidget extends StatefulWidget {
  final double width;
  final double height;
  final String path;
  final FilePreviewController? controller;
  final FilePreviewCallBack? callBack;

  /// 文件预览widget
  ///
  /// [width] 宽
  ///
  /// [height] 高
  ///
  /// [path] 文件地址 https\http开头、文件格式结尾的地址，或者本地绝对路径
  ///
  /// [controller] [FilePreviewController]控制器
  const FilePreviewWidget({
    Key? key,
    required this.width,
    required this.height,
    required this.path,
    this.controller,
    this.callBack,
  }) : super(key: key);

  @override
  FilePreviewWidgetState createState() => FilePreviewWidgetState();
}

class FilePreviewWidgetState extends State<FilePreviewWidget> {
  final String _viewType = "com.gstory.file_preview/filePreview";

  MethodChannel? _channel;

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return SizedBox(
        width: widget.width,
        height: widget.height,
        child: AndroidView(
          viewType: _viewType,
          creationParams: {
            "width": widget.width,
            "height": widget.height,
            "path": widget.path,
          },
          onPlatformViewCreated: _registerChannel,
          creationParamsCodec: const StandardMessageCodec(),
        ),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return SizedBox(
        width: widget.width,
        height: widget.height,
        child: UiKitView(
          viewType: _viewType,
          creationParams: {
            "width": widget.width,
            "height": widget.height,
            "path": widget.path,
          },
          onPlatformViewCreated: _registerChannel,
          creationParamsCodec: const StandardMessageCodec(),
        ),
      );
    } else {
      return Container();
    }
  }

  //注册cannel
  void _registerChannel(int id) {
    _channel = MethodChannel("${_viewType}_$id");
    _channel?.setMethodCallHandler(_platformCallHandler);
    widget.controller?.init(_channel);
  }

  //监听原生view传值
  Future<dynamic> _platformCallHandler(MethodCall call) async {
    switch (call.method) {
      case "onShow":
        if (widget.callBack?.onShow != null) {
          widget.callBack?.onShow!();
        }
        break;
      case "onDownload":
        if (widget.callBack?.onDownload != null) {
          Map map = call.arguments;
          widget.callBack?.onDownload!(map["progress"]);
        }
        break;
      case "onFail":
        if (widget.callBack?.onFail != null) {
          Map map = call.arguments;
          widget.callBack?.onFail!(map["code"], map["msg"]);
        }
        break;
    }
  }
}
