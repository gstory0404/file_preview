package com.gstory.file_preview

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
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
        mFlutterPluginBinding?.platformViewRegistry?.registerViewFactory("com.gstory.file_preview/FilePreviewWidget", FilePreviewFactory(mFlutterPluginBinding?.binaryMessenger!!, mActivity!!))

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
            //禁用隐私API的获取
            QbSdk.disableSensitiveApi()
            //BS内核首次使用和加载时，ART虚拟机会将Dex文件转为Oat，该过程由系统底层触发且耗时较长，很容易引起anr问题，解决方法是使用TBS的 ”dex2oat优化方案“。
            // 在调用TBS初始化、创建WebView之前进行如下配置
            val map: HashMap<String, Any> = HashMap()
            map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
            map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
            QbSdk.initTbsSettings(map)
            QbSdk.setDownloadWithoutWifi(true)
            val cb: PreInitCallback = object : PreInitCallback {
                override fun onViewInitFinished(arg0: Boolean) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.e("TBS内核", "onViewInitFinished:$arg0")
                    if (arg0) {
                        mActivity?.runOnUiThread {
                            result.success(true)
                        }
                    } else {
                        QbSdk.disableSensitiveApi()
                        val map: HashMap<String, Any> = HashMap()
                        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
                        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
                        QbSdk.initTbsSettings(map)
                        QbSdk.setDownloadWithoutWifi(true)
                        QbSdk.reset(applicationContext)
                    }
                }

                override fun onCoreInitFinished() {
                    Log.e("TBS内核", "onCoreInitFinished")
                }
            }
            //x5内核初始化接口
            QbSdk.initX5Environment(applicationContext, cb)
            QbSdk.setTbsListener(object : TbsListener {
                override fun onDownloadFinish(i: Int) {
                    //tbs内核下载完成回调
                    Log.e("TBS内核", "下载完成")
                }

                override fun onInstallFinish(i: Int) {
                    //内核安装完成回调，
                    Log.e("TBS内核", "安装完成")
                }

                override fun onDownloadProgress(i: Int) {
                    //下载进度监听
                    Log.e("TBS内核", "下载进度 $i")
                }
            })
        } else if (call.method == "tbsVersion") {
            result.success(QbSdk.getTbsVersion(applicationContext).toString())
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
