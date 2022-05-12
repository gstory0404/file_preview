part of 'file_preview.dart';

/// @Author: gstory
/// @CreateDate: 2021/12/27 10:21 上午
/// @Email gstory0404@gmail.com
/// @Description: 文件预览view

class FilePreviewWidget extends StatefulWidget {
  final double width;
  final double height;
  final String path;
  FilePreviewCallBack? callBack;

  FilePreviewWidget(
      {Key? key,
      required this.width,
      required this.height,
      required this.path,
      this.callBack})
      : super(key: key);

  @override
  _FilePreviewWidgetState createState() => _FilePreviewWidgetState();
}

class _FilePreviewWidgetState extends State<FilePreviewWidget> {
  final String _viewType = "com.gstory.file_preview/FilePreviewWidget";

  MethodChannel? _channel;

  @override
  Widget build(BuildContext context) {
    // if (!_isShow) {
    //   return Container();
    // }
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
  }

  //监听原生view传值
  Future<dynamic> _platformCallHandler(MethodCall call) async {
    Map map = call.arguments;
    switch (call.method) {
      case "onShow":
        if(widget.callBack?.onShow != null){
          widget.callBack?.onShow!();
        }
        break;
      case "onDownload":
        if(widget.callBack?.onDownload != null){
          widget.callBack?.onDownload!(map["progress"]);
        }
        break;
      case "onFail":
        if(widget.callBack?.onFail != null){
          widget.callBack?.onFail!(map["code"],map["msg"]);
        }
        break;
    }
  }
}
