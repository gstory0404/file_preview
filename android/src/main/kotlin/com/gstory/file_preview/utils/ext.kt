package com.gstory.file_preview.utils

import android.text.TextUtils
import android.util.Log
import java.net.URLConnection

/**
 * @Author: gstory
 * @CreateDate: 2023/3/10 14:48
 * @Description: java类作用描述
 */

val contentTypeList: Map<String, String> = mapOf(
    "text/html" to ".html",
    "text/plain" to ".txt",
    "application/pdf" to ".pdf",
    "application/msword" to ".doc",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to ".docx",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.template" to ".dotx",
    "application/vnd.ms-word.document.macroEnabled.12" to ".docm",
    "application/vnd.ms-word.template.macroEnabled.12" to ".dotm",
    "application/x-xls" to ".xls",
    "application/vnd.ms-excel" to ".xls",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to ".xlsx",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.template" to ".xltx",
    "application/vnd.ms-excel.sheet.macroEnabled.12" to ".xlsm",
    "application/vnd.ms-excel.template.macroEnabled.12" to ".xltm",
    "application/vnd.ms-excel.addin.macroEnabled.12" to ".xlam",
    "application/vnd.ms-excel.sheet.binary.macroEnabled.12" to ".xlsb",
    "application/x-ppt" to ".ppt",
    "application/vnd.ms-powerpoint" to ".ppt",
    "application/vnd.openxmlformats-officedocument.presentationml.presentation" to ".pptx",
    "application/vnd.openxmlformats-officedocument.presentationml.template" to ".potx",
    "application/vnd.openxmlformats-officedocument.presentationml.slideshow" to ".ppsx",
    "application/vnd.ms-powerpoint.addin.macroEnabled.12" to ".ppam",
    "application/vnd.ms-powerpoint.presentation.macroEnabled.12" to ".pptm",
    "application/vnd.ms-powerpoint.presentation.macroEnabled.12" to ".potm",
    "application/vnd.ms-powerpoint.slideshow.macroEnabled.12" to ".ppsm",
    "application/epub+zip" to ".epub",
    "application/x-chm" to ".chm",
    "application/rtf" to ".rtf",
)

val fileList: List<String> = listOf(
    "doc",
    "docx",
    "rtf",
    "ppt",
    "pptx",
    "xls",
    "xlsx",
    "xisx",
    "xlsm",
    "csv",
    "pdf",
    "txt",
    "epub",
    "chm"
)

fun URLConnection.fileExt(): String {
    var suffix = FileUtils.getFileType(this.url.toString())
    Log.d("FileUtils===>", "文件格式 从url获取==>.$suffix")
    //如果url获取的格式支持 则直接返回，否则根据suffix来判断
    if (fileList.contains(suffix)) {
        return ".$suffix"
    }
    var type = this.contentType
    if (type.contains(";")) {
        type = type.substring(0, type.indexOf(";"))
    }
    Log.d("FileUtils===>", "文件格式 从contentType获取==>${contentTypeList[type]}")
    return contentTypeList[type]!!
}