package com.gstory.file_preview

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** FilePreviewPlugin */
class FilePreviewPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

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
      QbSdk.initX5Environment(applicationContext, object : PreInitCallback {
        override fun onCoreInitFinished() {
          Log.e("TBS内核", "初始化成功")
        }
        override fun onViewInitFinished(b: Boolean) {
          //这里被回调，并且b=true说明内核初始化并可以使用
          //如果b=false,内核会尝试安装，你可以通过下面监听接口获知
          Log.e("TBS内核", "初始化结果 $b")
          if(!b){
            QbSdk.reset(applicationContext);
          }else{
            mActivity?.runOnUiThread {
              result.success(true)
            }
          }
        }
      })
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

    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
