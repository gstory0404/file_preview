package com.gstory.file_preview

import io.flutter.app.FlutterApplication

/**
 * @Author: gstory
 * @CreateDate: 2022/1/17 3:03 下午
 * @Description: 描述
 */

open class FilePreviewApp : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        //初始化TBS
        TbsManager.instance.initTBS(this,null)
    }
}