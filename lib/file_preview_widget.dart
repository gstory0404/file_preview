part of 'file_preview.dart';

/// @Author: gstory
/// @CreateDate: 2021/12/27 10:21 上午
/// @Email gstory0404@gmail.com
/// @Description: 文件预览view

class FilePreviewWidget extends StatefulWidget {
  final double width;
  final double height;
  final String path;

  const FilePreviewWidget(
      {Key? key,
      required this.width,
      required this.height,
      required this.path})
      : super(key: key);

  @override
  _FilePreviewWidgetState createState() => _FilePreviewWidgetState();
}

class _FilePreviewWidgetState extends State<FilePreviewWidget> {
  String _viewType = "com.gstory.file_preview/FilePreviewWidget";

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
            "widget": widget.width,
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
            "widget": widget.width,
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
    switch (call.method) {
    }
  }
}
