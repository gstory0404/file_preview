package com.gstory.file_preview

import android.app.Application
import android.content.Context
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import java.util.HashMap

class TbsManager private constructor() {

    var isInit: Boolean = false


    companion object {
        val instance: TbsManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TbsManager()
        }
    }

    fun initTBS(app: Context, callBack: InitCallBack?) {
        if(isInit){
            callBack?.initFinish(true)
            return
        }
        //禁用隐私API的获取
        QbSdk.disableSensitiveApi()
        //BS内核首次使用和加载时，ART虚拟机会将Dex文件转为Oat，该过程由系统底层触发且耗时较长，很容易引起anr问题，解决方法是使用TBS的 ”dex2oat优化方案“。
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map: HashMap<String, Any> = HashMap()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        QbSdk.setDownloadWithoutWifi(true)
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param arg0 是否使用X5内核
             */
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("TBS内核", "onViewInitFinished:$arg0")
                isInit = arg0
                if (arg0) {
                    callBack?.initFinish(true)
                    Log.e("TBS内核", "initFinish:$arg0")
                } else {
                    callBack?.initFinish(false)
                    QbSdk.disableSensitiveApi()
                    val map: HashMap<String, Any> = HashMap()
                    map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
                    map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
                    QbSdk.initTbsSettings(map)
                    QbSdk.setDownloadWithoutWifi(true)
                    QbSdk.reset(app)
                }
            }

            override fun onCoreInitFinished() {
                Log.e("TBS内核", "onCoreInitFinished")
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(app, cb)
        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(i: Int) {
                //tbs内核下载完成回调
                Log.e("TBS内核", "下载完成$i")
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

interface InitCallBack {
    fun initFinish(b: Boolean)
}