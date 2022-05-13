package com.gstory.file_preview

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.flutter_pangrowth.utils.UIUtils
import com.gstory.file_preview.utils.FileUtils
import com.tencent.smtt.sdk.TbsReaderView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import java.io.File


/**
 * @Author: gstory
 * @CreateDate: 2021/12/27 10:34 上午
 * @Description: 描述
 */

internal class FilePreview(
        var activity: Activity,
        messenger: BinaryMessenger,
        id: Int,
        params: Map<String?, Any?>
) :
        PlatformView {

    private val TAG = "FilePreview"

    private var mContainer: FrameLayout = FrameLayout(activity)
    private var width: Double = params["width"] as Double
    private var height: Double = params["height"] as Double
    private var path: String = params["path"] as String

    private var tbsReaderView: TbsReaderView

    private var channel : MethodChannel?

    private var readerCallback = object : TbsReaderView.ReaderCallback {
        override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {
//            Log.e(TAG, "文件打开回调$p0 $p1 $p2")
        }
    }

    init {
        mContainer.layoutParams?.width = (UIUtils.dip2px(activity, width.toFloat())).toInt()
        mContainer.layoutParams?.height = (UIUtils.dip2px(activity, height.toFloat())).toInt()
        tbsReaderView = TbsReaderView(activity, readerCallback)
        tbsReaderView.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        tbsReaderView.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        mContainer.addView(tbsReaderView)
        channel = MethodChannel(messenger,"com.gstory.file_preview/FilePreviewWidget_$id")
        loadFile()
    }

    override fun getView(): View {
        return mContainer
    }

    private fun loadFile() {
        if(!TbsManager.instance.isInit){
            var map: MutableMap<String, Any?> = mutableMapOf("code" to 1004,"msg" to "TBS未初始化")
            channel?.invokeMethod("onFail",map)
            return
        }
        //tbs只能加载本地文件 如果是网络文件则先下载
        if (path.startsWith("http")) {
            FileUtils.downLoadFile(activity, path, object : FileUtils.DownloadCallback {
                override fun onProgress(progress: Int) {
//                    Log.e(TAG, "文件下载进度$progress")
                    activity.runOnUiThread {
                        var map: MutableMap<String, Any?> = mutableMapOf("progress" to progress)
                        channel?.invokeMethod("onDownload", map)
                    }
                }

                override fun onFail(msg: String) {
                    Log.e(TAG, "文件下载失败$msg")
                    activity.runOnUiThread {
                        var map: MutableMap<String, Any?> = mutableMapOf("code" to 1000,"msg" to msg)
                        channel?.invokeMethod("onFail",map)
                    }
                }

                override fun onFinish(file: File) {
                    Log.e(TAG, "文件下载完成！")
                    activity.runOnUiThread {
                        openFile(file)
                    }
                }

            })
        } else {
            openFile(File(path))
        }

    }

    /**
     * 打开文件
     */
    private fun openFile(file: File?) {
        if (file != null && !TextUtils.isEmpty(file.toString())) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
            val bsReaderTemp = FileUtils.getDir(activity).toString() + File.separator + "TbsReaderTemp"
            val bsReaderTempFile = File(bsReaderTemp)
            if (!bsReaderTempFile.exists()) {
                val mkdir: Boolean = bsReaderTempFile.mkdir()
                if (!mkdir) {
                    Log.e(TAG, "创建$bsReaderTemp 失败")
                    var map: MutableMap<String, Any?> = mutableMapOf("code" to 1001,"msg" to "文件下载失败")
                    channel?.invokeMethod("onFail",map)
                }
            }
            //加载文件
            val localBundle = Bundle()
            Log.d(TAG, file.toString())
            localBundle.putString("filePath", file.toString())
            localBundle.putString("tempPath", bsReaderTemp)
            val bool = tbsReaderView.preOpen(FileUtils.getFileType(file.toString()), false)
            if (bool) {
                tbsReaderView.openFile(localBundle)
                var map: MutableMap<String, Any?> = mutableMapOf()
                channel?.invokeMethod("onShow",map)
            }else{
                Log.e(TAG, "文件打开失败！")
                var map: MutableMap<String, Any?> = mutableMapOf("code" to 1002,"msg" to "文件格式不支持或者打开失败")
                channel?.invokeMethod("onFail",map)
            }
        } else {
            Log.e(TAG, "文件路径无效！")
            var map: MutableMap<String, Any?> = mutableMapOf("code" to 1003,"msg" to "本地文件路径无效")
            channel?.invokeMethod("onFail",map)
        }
    }


    override fun dispose() {
        tbsReaderView.onStop()
    }
}