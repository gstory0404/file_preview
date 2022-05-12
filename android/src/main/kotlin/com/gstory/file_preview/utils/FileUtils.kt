package com.gstory.file_preview.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @Author: gstory
 * @CreateDate: 2021/12/27 11:02 上午
 * @Description: 描述
 */

object FileUtils {

    /**
     * 获取缓存目录
     */
    fun getDir(context: Context): File {
        val dir = File(context.filesDir, "file_preview")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * 删除缓存文件夹
     */
    fun deleteCache(context: Context,dir: File) {
        if (!dir.exists() || !dir.isDirectory) {
            return
        }
        for (file in dir.listFiles()!!) {
            if (file.isFile) file.delete()
            else if (file.isDirectory) deleteCache(context,file)
        }
        dir.delete()
    }


    /**
     * 获取文件格式
     */
    fun getFileType(paramString: String): String? {
        var str = ""
        if (TextUtils.isEmpty(paramString)) {
            return str
        }
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            return str
        }
        str = paramString.substring(i + 1)
        Log.d("FileUtils", "当前文件格式$str")
        return str
    }

    /**
     * 下载文件
     */
    fun downLoadFile(context: Context, url: String, callback: DownloadCallback) {
        var filename = url.substring(url.lastIndexOf('/') + 1)
        var saveFile =
            File(FileUtils.getDir(context).toString() + File.separator + filename)
        //如果文件存在 不再下载 直接读取展示
        if (saveFile.exists()) {
            callback.onFinish(saveFile)
            return
        }
        val request = Request.Builder().url(url).build();
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback.onFail(e.toString())
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                try {
                    `is` = response.body?.byteStream()
                    val total = response.body?.contentLength() ?: 1
                    fos = FileOutputStream(saveFile)
                    var sum = 0
                    while ((`is`?.read(buf).also { len = it ?: 0 }) != -1) {
                        fos.write(buf, 0, len)
                        sum += len
                        val progress = (sum * 1f / total * 100).toInt()
                        //下载进度
                        callback.onProgress(progress)
                    }
                    fos.flush();
                    // 下载完成
                    callback.onFinish(saveFile)

                } catch (e: Exception) {
                    e.printStackTrace()
                    callback.onFail(e.toString())
                } finally {
                    try {
                        `is`?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    interface DownloadCallback {
        fun onProgress(progress: Int)
        fun onFail(msg: String)
        fun onFinish(file: File)
    }
}