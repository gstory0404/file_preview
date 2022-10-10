package com.gstory.file_preview

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.gstory.file_preview.utils.FileUtils
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.HashMap


/** FilePreviewPlugin */
class FilePreviewPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private var applicationContext: Context? = null
    private var mActivity: Activity? = null
    private lateinit var channel: MethodChannel
    private var mFlutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mActivity = binding.activity
        mFlutterPluginBinding?.platformViewRegistry?.registerViewFactory(
            "com.gstory.file_preview/filePreview",
            FilePreviewFactory(mFlutterPluginBinding?.binaryMessenger!!, mActivity!!)
        )

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        mActivity = binding.activity
//        Log.e("FlutterUnionadPlugin->","onReattachedToActivityForConfigChanges")
    }

    override fun onDetachedFromActivityForConfigChanges() {
        mActivity = null
//        Log.e("FlutterUnionadPlugin->","onDetachedFromActivityForConfigChanges")
    }

    override fun onDetachedFromActivity() {
        mActivity = null
//        Log.e("FlutterUnionadPlugin->","onDetachedFromActivity")
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "file_preview")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext
        mFlutterPluginBinding = flutterPluginBinding
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "initTBS") {
            if (TbsManager.instance.isInit) {
                result.success(true)
            } else {
                TbsManager.instance.initTBS(applicationContext!!, object : InitCallBack {
                    override fun initFinish(b: Boolean) {
                        mActivity?.runOnUiThread {
                            result.success(b)
                        }
                    }
                })
            }
        } else if (call.method == "tbsHasInit") {
            result.success(TbsManager.instance.isInit)
        } else if (call.method == "deleteCache") {
            FileUtils.deleteCache(mActivity!!,FileUtils.getDir(mActivity!!))
            result.success(true)
        } else if (call.method == "tbsVersion") {
            result.success(QbSdk.getTbsSdkVersion().toString())
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
