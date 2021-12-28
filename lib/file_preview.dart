
import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

part 'file_preview_widget.dart';

class FilePreview {
  static const MethodChannel _channel = MethodChannel('file_preview');

  static Future<bool?> initTBS() async {
    final bool? init = await _channel.invokeMethod('initTBS');
    return init;
  }
}
