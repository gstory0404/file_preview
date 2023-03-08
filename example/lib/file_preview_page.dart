import 'package:file_preview/file_preview.dart';
import 'package:flutter/material.dart';

/// @Author: gstory
/// @CreateDate: 2021/12/27 10:27 上午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

class FilePreviewPage extends StatefulWidget {
  final String title;
  final String path;

  const FilePreviewPage({Key? key, required this.path, required this.title})
      : super(key: key);

  @override
  State<FilePreviewPage> createState() => _FilePreviewPageState();
}

class _FilePreviewPageState extends State<FilePreviewPage> {
  FilePreviewController controller = FilePreviewController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Column(
        children: [
          Container(
            alignment: Alignment.topLeft,
            child: FilePreviewWidget(
              controller: controller,
              width: 400,
              height: 600,
              path: widget.path,
              callBack: FilePreviewCallBack(onShow: () {
                print("文件打开成功");
              }, onDownload: (progress) {
                print("文件下载进度$progress");
              }, onFail: (code, msg) {
                print("文件打开失败 $code  $msg");
              }),
            ),
          ),
          Row(
            children: [
              TextButton(
                onPressed: () {
                  controller
                      .showFile("https://gstory.vercel.app/ceshi/ceshi.docx");
                },
                child: const Text("docx"),
              ),
              TextButton(
                onPressed: () {
                  controller
                      .showFile("https://gstory.vercel.app/ceshi/ceshi.pdf");
                },
                child: const Text("pdf"),
              ),
              TextButton(
                onPressed: () {
                  controller
                      .showFile("https://gstory.vercel.app/ceshi/ceshi.xisx");
                },
                child: const Text("xisx"),
              ),
              TextButton(
                onPressed: () {
                  controller
                      .showFile("https://gstory.vercel.app/ceshi/ceshi.txt");
                },
                child: const Text("txt"),
              ),
              TextButton(
                onPressed: () {
                  controller
                      .showFile("https://gstory.vercel.app/ceshi/ceshi.pptx");
                },
                child: const Text("ppt"),
              ),
            ],
          )
        ],
      ),
    );
  }
}
