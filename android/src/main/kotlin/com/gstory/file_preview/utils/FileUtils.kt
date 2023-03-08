package com.gstory.file_preview.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
    fun deleteCache(context: Context, dir: File) {
        if (!dir.exists() || !dir.isDirectory) {
            return
        }
        for (file in dir.listFiles()!!) {
            if (file.isFile) file.delete()
            else if (file.isDirectory) deleteCache(context, file)
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
    fun downLoadFile(context: Context, downloadUrl: String, callback: DownloadCallback) {
        val url = URL(downloadUrl)
        // 考虑到文件来源可能是私有仓库，此时的路径地址一般为 https://xxx.com/file.ext?sign=sign_value
        // 此时需要移除末尾的 query 参数
        val path = url.path
        val filename = path.substring(path.lastIndexOf('/') + 1)
        val saveFile =
            File(FileUtils.getDir(context).toString() + File.separator + filename)
        //如果文件存在 不再下载 直接读取展示
        if (saveFile.exists()) {
            callback.onFinish(saveFile)
            return
        }
        Thread {
            // 流和链接
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            var connection: HttpURLConnection? = null
            // 下载准备
            var downloadedSize = 0 // 已经下载的文件大小
            var fileTotalSize = 0 // 文件总大小

            // 开始链接
            try {
                connection = url.openConnection() as HttpURLConnection?
                connection?.connectTimeout = 10 * 1000;
                connection?.readTimeout = 10 * 1000;
                connection?.connect();
                // 获取要下载的文件信息
                fileTotalSize = connection?.contentLength!!       // 文件总大小


                inputStream = connection.inputStream;
                outputStream = FileOutputStream(saveFile)


                val buffer = ByteArray(1024 * 4)
                var len: Int
                while (inputStream.read(buffer).also { len = it } > 0) {
                    outputStream.write(buffer, 0, len);
                    downloadedSize += len;
                    // 计算文件下载进度
                    val progress: Int = (downloadedSize * 1.0f / fileTotalSize * 100).toInt()
                    callback.onProgress(progress)
                }
                // 下载成功
                callback.onFinish(saveFile)
            } catch (e: Exception) {
                if (saveFile.exists()) {
                    if (saveFile.delete()) {
                        callback.onFail("下载失败$e")
                    } else {
                        callback.onFail("下载失败$e")
                    }
                } else {
                    callback.onFail("下载失败$e")
                }
            } finally {
                try {
                    inputStream?.close();
                    outputStream?.close();
                    connection?.disconnect();
                } catch (e: Exception) {
                    callback.onFail("IO流关闭失败$e")
                }
            }
        }.start()
    }

    interface DownloadCallback {
        fun onProgress(progress: Int)
        fun onFail(msg: String)
        fun onFinish(file: File)
    }
}