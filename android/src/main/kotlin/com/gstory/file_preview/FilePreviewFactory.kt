package com.gstory.file_preview

import android.app.Activity
import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

/**
 * @Author: gstory
 * @CreateDate: 2021/12/27 10:33 上午
 * @Description: 描述
 */

internal class FilePreviewFactory (private val messenger: BinaryMessenger, private val activity: Activity) : PlatformViewFactory(
        StandardMessageCodec.INSTANCE) {

    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        val params = args as Map<String?, Any?>
        return FilePreview(activity,messenger, viewId, params)
    }
}