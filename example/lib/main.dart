import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:file_preview_example/file_preview_page.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:file_preview/file_preview.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  bool? isInit;
  String? version;

  @override
  void initState() {
    _initTBS();
    super.initState();
  }

  void _initTBS() async {
    isInit = await FilePreview.initTBS();
    version = await FilePreview.tbsVersion();
    if(mounted){
      setState(() {

      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('File Preview'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('TBS初始化 $isInit'),
            Text('TBS版本号 $version'),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('在线docx预览'),
              onPressed: () async {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) {
                      return const FilePreviewPage(
                        title: "docx预览",
                        path:
                            "https://gstory.vercel.app/ceshi/ceshi.docx",
                      );
                    },
                  ),
                );
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('在线pdf预览'),
              onPressed: () async {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) {
                      return const FilePreviewPage(
                        title: "在线pdf预览",
                        path: "https://gstory.vercel.app/ceshi/ceshi.pdf",
                      );
                    },
                  ),
                );
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('在线xlsx预览'),
              onPressed: () async {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) {
                      return const FilePreviewPage(
                        title: "在线xlsx预览",
                        path:
                            "https://gstory.vercel.app/ceshi/ceshi.xlsx",
                      );
                    },
                  ),
                );
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('在线txt预览'),
              onPressed: () async {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) {
                      return const FilePreviewPage(
                        title: "在线txt预览",
                        path:
                        "https://gstory.vercel.app/ceshi/ceshi.txt",
                      );
                    },
                  ),
                );
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('在线ppt预览'),
              onPressed: () async {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) {
                      return const FilePreviewPage(
                        title: "在线pdf预览",
                        path: "https://gstory.vercel.app/ceshi/ceshi.pptx",
                      );
                    },
                  ),
                );
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('本地文件预览'),
              onPressed: () async {
                FilePickerResult? result = await FilePicker.platform.pickFiles();
                if (result != null) {
                  File file = File(result.files.single.path!);
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) {
                        return FilePreviewPage(
                          title: "本地文件预览",
                          path: file.path,
                        );
                      },
                    ),
                  );
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}
