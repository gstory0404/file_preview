//
//  FilePreview.swift
//  file_preview
//
//  Created by gstory on 2021/12/27.
//

import Foundation
import Flutter
import UIKit
import WebKit


public class FilePreview : NSObject,FlutterPlatformView{
    private var container : UIView
    
    let width : Float
    let height :Float
    let path : String
    
    var webView:WKWebView
    
    private var channel : FlutterMethodChannel?
    
    init(_ frame : CGRect,binaryMessenger: FlutterBinaryMessenger , id : Int64, params :Any?) {
        let dict = params as! NSDictionary
        self.width = Float(dict.value(forKey: "width") as! Double)
        self.height = Float(dict.value(forKey: "height") as! Double)
        self.path = dict.value(forKey: "path") as! String
        self.container = UIView(frame: frame)
        self.webView = WKWebView(frame:CGRect(x:0, y:0, width:Int(self.width), height:Int(self.height)))
        super.init()
        self.webView.navigationDelegate = self
        self.container.addSubview(self.webView)
        self.channel = FlutterMethodChannel.init(name: "com.gstory.file_preview/filePreview" + "_" + String(id), binaryMessenger: binaryMessenger)
        self.initMethodCall()
        showFile(filePath: self.path)
    }
    
    public func view() -> UIView {
        return self.container
    }
    
    //监听flutter传过来的参数
    private func initMethodCall(){
        self.channel?.setMethodCallHandler { (call, result) in
            switch call.method {
            case "showFile":
                self.showFile(filePath: call.arguments as! String)
                break
            default:
                break
            }
            
        }
    }
    
    //显示文件
    private func showFile(filePath:String){
        print(filePath)
        self.channel?.invokeMethod("onStart", arguments: nil)
        if(filePath.starts(with: "http")){
            let path2 = filePath.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed) ?? ""
            let url = NSURL(string: path2)
            let request = NSMutableURLRequest(url: url! as URL)
            self.webView.load(request as URLRequest as URLRequest)
        }else{
            let url = NSURL.fileURL(withPath:filePath)
            let ext = url.pathExtension.lowercased();
            if(ext == "txt"){
                let data = NSData.init(contentsOf: url)
                self.webView.load(data! as Data, mimeType: "text/html", characterEncodingName: "UTF-8", baseURL: URL.init(fileURLWithPath: ""))
            }else if(ext == "pdf"){
                let data = NSData.init(contentsOf: url)
                self.webView.load(data! as Data, mimeType: "application/pdf", characterEncodingName: "UTF-8", baseURL: URL.init(fileURLWithPath: ""))
            }else{
                self.webView.loadFileURL(url, allowingReadAccessTo: url)
            }
        }
        
    }
}

extension FilePreview : WKNavigationDelegate{
    
    // 当内容开始返回时调用
    public func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        print("file开始加载")
    }
    
    // 页面加载完毕时调用
    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        print("file加载完成")
        self.channel?.invokeMethod("onShow", arguments: nil)
    }
    
    //跳转失败时调用
    public func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print("file加载失败")
        let map : NSDictionary = ["code":error.localizedDescription.count,
                                  "msg":error.localizedDescription.description]
        self.channel?.invokeMethod("onFail", arguments: map)
    }
}


