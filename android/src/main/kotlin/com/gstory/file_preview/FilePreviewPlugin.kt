package com.gstory.file_preview

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.gstory.file_preview.utils.FileUtils
import com.tencent.tbs.reader.TbsFileInterfaceImpl
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


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
            val license = call.argument<String>("license")
            TbsFileInterfaceImpl.setLicenseKey(license)
            TbsFileInterfaceImpl.fileEnginePreCheck(mActivity)
            var isInit = TbsFileInterfaceImpl.initEngine(applicationContext)
            Log.d("=====>", "初始化 $isInit")
            result.success(isInit == 0)
        }else if(call.method == "tbsHasInit"){
            val ret = TbsFileInterfaceImpl.initEngine(applicationContext)
            result.success(ret == 0)
        } else if (call.method == "tbsVersion") {
            result.success(TbsFileInterfaceImpl.getVersionName())
        } else if (call.method == "deleteCache") {
            FileUtils.deleteCache(mActivity!!, FileUtils.getDir(mActivity!!))
            result.success(true)
        } else {

        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
