package com.gstory.file_preview

import android.app.Activity
import android.content.res.AssetManager
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.flutter_pangrowth.utils.UIUtils
import com.gstory.file_preview.utils.FileUtils
import com.gstory.file_preview.utils.fileExt
import com.tencent.tbs.reader.ITbsReader
import com.tencent.tbs.reader.ITbsReaderCallback
import com.tencent.tbs.reader.TbsFileInterfaceImpl
import io.flutter.FlutterInjector
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import java.io.File
import java.io.FileOutputStream


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
    PlatformView, MethodChannel.MethodCallHandler {

    private val TAG = "FilePreview"

    private var mContainer: FrameLayout = FrameLayout(activity)
    private var width: Double = params["width"] as Double
    private var height: Double = params["height"] as Double
    private var path: String = params["path"] as String

    private var channel: MethodChannel?

    init {
        mContainer.layoutParams?.width = (UIUtils.dip2px(activity, width.toFloat())).toInt()
        mContainer.layoutParams?.height = (UIUtils.dip2px(activity, height.toFloat())).toInt()
        mContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
        channel = MethodChannel(messenger, "com.gstory.file_preview/filePreview_$id")
        channel?.setMethodCallHandler(this)
        loadFile(path)
    }

    override fun getView(): View {
        return mContainer
    }


    private fun loadFile(filePath: String) {
        mContainer.removeAllViews()
        //tbs只能加载本地文件 如果是网络文件则先下载
        if (filePath.startsWith("http")) {
            //进度条
            var progressBar = ProgressBar(activity)
            progressBar.indeterminateDrawable =
                activity.resources.getDrawable(R.drawable.progressbar_style)
            mContainer.addView(progressBar)
            //文字
            var mRateText = TextView(activity)
            mRateText.layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            mRateText.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            mRateText.gravity = Gravity.CENTER
            mRateText.setTextColor(Color.parseColor("#cccccc"))
            mRateText.textSize = 12f
            mContainer.addView(mRateText)
            FileUtils.downLoadFile(activity, filePath, object : FileUtils.DownloadCallback {
                override fun onProgress(progress: Int) {
//                    Log.e(TAG, "文件下载进度$progress")
                    activity.runOnUiThread {
                        mRateText.text = "$progress%"
                        var map: MutableMap<String, Any?> = mutableMapOf("progress" to progress)
                        channel?.invokeMethod("onDownload", map)
                    }
                }

                override fun onFail(msg: String) {
                    Log.e(TAG, "文件下载失败$msg")
                    activity.runOnUiThread {
                        var map: MutableMap<String, Any?> =
                            mutableMapOf("code" to 1000, "msg" to msg)
                        channel?.invokeMethod("onFail", map)
                    }
                }

                override fun onFinish(file: File) {
                    Log.e(TAG, "文件下载完成！")
                    activity.runOnUiThread {
                        openFile(file)
                    }
                }

            })
        } else if (filePath.startsWith("assets/")) {
            try {
                val assetManager: AssetManager = activity.assets
                val assetPath = FlutterInjector.instance().flutterLoader()
                    .getLookupKeyForAsset(filePath.replace("assets/", ""))
                var inputStream = assetManager.open(assetPath)
                val mByte = ByteArray(1024)
                var bt = 0
                var file = File(
                    "${FileUtils.getDir(activity)}${File.separator}${filePath.hashCode()}_preview.${
                        FileUtils.getFileType(filePath)
                    }"
                )
                //如果存在直接加载
                if (file.exists()) {
                    openFile(File(file.path))
                    return
                }
                //新建文件
                file.createNewFile()
                var fos = FileOutputStream(file)
                //写入文件
                bt = inputStream.read(mByte);
                while (bt != -1) {
                    fos.write(mByte, 0, bt)
                }
                fos.flush()
                inputStream.close()
                fos.close()
                //打开文件
                openFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            openFile(File(filePath))
        }
    }

    /**
     * 打开文件
     */
    private fun openFile(file: File?) {
        TbsFileInterfaceImpl.getInstance().closeFileReader()
        Log.e(TAG, "打开文件 ${file?.path}")
        mContainer.removeAllViews()
        if (file != null && !TextUtils.isEmpty(file.toString())) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
            val bsReaderTemp =
                FileUtils.getDir(activity).toString() + File.separator + "TbsReaderTemp"
            val bsReaderTempFile = File(bsReaderTemp)
            if (!bsReaderTempFile.exists()) {
                val mkdir: Boolean = bsReaderTempFile.mkdir()
                if (!mkdir) {
                    Log.e(TAG, "创建$bsReaderTemp 失败")
                    var map: MutableMap<String, Any?> =
                        mutableMapOf("code" to 1001, "msg" to "TbsReaderTemp缓存文件创建失败")
                    channel?.invokeMethod("onFail", map)
                }
            }
            //文件格式
            var fileExt = FileUtils.getFileType(file.toString())
            val bool = TbsFileInterfaceImpl.canOpenFileExt(fileExt)
            Log.d(TAG, "文件是否支持$bool  文件路径：$file $bsReaderTemp $fileExt")
            if (bool) {
                //加载文件
                val localBundle = Bundle()
                localBundle.putString("filePath", file.toString())
                localBundle.putString("tempPath", bsReaderTemp)
                localBundle.putString("fileExt", fileExt)
                localBundle.putInt(
                    "set_content_view_height",
                    (height * activity.resources.displayMetrics.density).toInt()
                )
                var ret = TbsFileInterfaceImpl.getInstance()
                    .openFileReader(activity, localBundle, object : ITbsReaderCallback {
                        override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {
                            Log.e(TAG, "文件打开回调 $p0  $p1  $p2")
                            if (p1 is Bundle) {
                                val id = p1.getInt("typeId")
//                            if (ITbsReader.TBS_READER_TYPE_STATUS_UI_SHUTDOWN == id) {
//                                //加密文档弹框取消需关闭activity
//                            }
                                if (ITbsReader.TBS_READER_TYPE_STATUS_UI_SHUTDOWN == id) {
                                    TbsFileInterfaceImpl.getInstance()
                                        .closeFileReader();       //关闭fileReader
                                }
                            }
                        }
                    }, mContainer)
                Log.e(TAG, "文件打开结果 $ret")
                if (ret == 0) {
                    channel?.invokeMethod("onShow", null)
                } else {
                    var map: MutableMap<String, Any?> =
                        mutableMapOf("code" to ret, "msg" to "error:$ret")
                    channel?.invokeMethod("onFail", map)
                }
            } else {
                Log.e(TAG, "文件打开失败！文件格式暂不支持")
                var map: MutableMap<String, Any?> =
                    mutableMapOf("code" to 1002, "msg" to "文件格式不支持或者打开失败")
                channel?.invokeMethod("onFail", map)
            }
        } else {
            Log.e(TAG, "文件路径无效！")
            var map: MutableMap<String, Any?> = mutableMapOf("code" to 1003, "msg" to "本地文件路径无效")
            channel?.invokeMethod("onFail", map)
        }
    }


    override fun dispose() {
        TbsFileInterfaceImpl.getInstance().closeFileReader()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if ("showFile" == call.method) {
            var path = call.arguments as String
            loadFile(path)
        }
    }
}