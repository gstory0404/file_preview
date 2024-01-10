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


@Suppress("UNUSED")
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
            if (!TbsFileInterfaceImpl.isEngineLoaded()) {
                val isInit = TbsFileInterfaceImpl.initEngine(applicationContext)
                val map = mutableMapOf<String, String>(
                    "102" to "未设置 licenseKey。",
                    "202" to "请检查调用接口是否正确，应调用 setLicenseKey 接口而不是 setLicense 接口。",
                    "103" to "1. 请检查设备网络是否连通。\n" +
                            "2. 尝试切换网络。",
                    "305" to "1. 请检查设备网络是否连通。\n" +
                            "2. 尝试切换网络。",
                    "212" to "调用量包次数用完。",
                    "322" to "调用量包次数用完。",
                    "4001" to "licenseKey 不存在，请检查设置的 licenseKey 是否正确。",
                    "4002" to "客户端包名和 licenseKey 不匹配。",
                )
                if (isInit != 0) {
                    val value = map[isInit.toString()]
                    if (value != null) {
                        Log.d("=====>", "初始化错误:${value}")
                        result.error(isInit.toString(), value, null)
                    } else {
                        result.success(false)
                    }
                    return
                }
                Log.d("=====>", "初始化成功")
                result.success(true)
            } else {
                Log.d("=====>", "初始化 isEngineLoaded false")
                result.success(false)
            }
        } else if (call.method == "tbsHasInit") {
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
