import 'package:file_preview/file_preview.dart';
import 'package:flutter/material.dart';

/// @Author: gstory
/// @CreateDate: 2021/12/27 10:27 上午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

class FilePreviewPage extends StatefulWidget {
  final String path;
  final String title;

  const FilePreviewPage({Key? key, required this.path, required this.title})
      : super(key: key);

  @override
  _FilePreviewPageState createState() => _FilePreviewPageState();
}

class _FilePreviewPageState extends State<FilePreviewPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Container(
        alignment: Alignment.center,
        child: const FilePreviewWidget(
          width: 400,
          height: 700,
          path: "11111",
        ),
      ),
    );
  }
}
