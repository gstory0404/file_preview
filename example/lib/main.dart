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
  bool isInit = false;
  String? version;

  @override
  void initState() {
    _tbsVersion();
    // _initTBS();
    super.initState();
  }

  void _initTBS() async {
    isInit = await FilePreview.initTBS();
    if (mounted) {
      setState(() {});
    }
  }

  void _tbsVersion() async {
    version = await FilePreview.tbsVersion();
    isInit = await FilePreview.tbsHasInit();
    if (mounted) {
      setState(() {});
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
              child: const Text('初始化TBS'),
              onPressed: () async {
                _initTBS();
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('TBS调试页面'),
              onPressed: () async {
                await FilePreview.tbsDebug();
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('检测TBS是否初始化成功'),
              onPressed: () async {
                isInit = await FilePreview.tbsHasInit();
                setState(() {});
              },
            ),
            MaterialButton(
              color: Colors.blue,
              textColor: Colors.white,
              child: const Text('在线docx预览'),
              onPressed: () async {
                isInit = await FilePreview.tbsHasInit();
                setState(() {});
                if (!isInit) {
                  _initTBS();
                  return;
                }

                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) {
                      return const FilePreviewPage(
                        title: "docx预览",
                        path: "https://gstory.vercel.app/ceshi/ceshi.docx",
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
                        path: "https://gstory.vercel.app/ceshi/ceshi.xlsx",
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
                        path: "https://gstory.vercel.app/ceshi/ceshi.txt",
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
                FilePickerResult? result =
                    await FilePicker.platform.pickFiles();
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
            MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('清理本地缓存文件（android有效）'),
                onPressed: () async {
                  var delete = await FilePreview.deleteCache();
                  if (delete) {
                    print("缓存文件清理成功");
                  }
                }),
            MaterialButton(
                color: Colors.blue,
                textColor: Colors.white,
                child: const Text('在线视频(仅支持ios)'),
                onPressed: () async {
                  if (Platform.isIOS) {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) {
                          return const FilePreviewPage(
                            title: "在线视频(仅支持ios)",
                            path:
                                "https://v.gsuus.com/play/xbo8Dkdg/index.m3u8",
                          );
                        },
                      ),
                    );
                  }
                }),
          ],
        ),
      ),
    );
  }
}
