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


public class FilePreview : NSObject,FlutterPlatformView,WKNavigationDelegate{
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
        self.container.addSubview(self.webView)
        super.init()
        self.channel = FlutterMethodChannel.init(name: "com.gstory.file_preview/FilePreviewWidget_" + String(id), binaryMessenger: binaryMessenger)
        loadWebview()
        
    }
    
    public func view() -> UIView {
        return self.container
    }

    private func loadWebview(){
        print(self.path)
        if(self.path.starts(with: "http")){
            let path2 = self.path.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed) ?? ""
            let url = NSURL(string: path2)
            let request = NSMutableURLRequest(url: url! as URL)
            self.webView.load(request as URLRequest as URLRequest)
        }else{
            let url = NSURL.fileURL(withPath: self.path)
            if(self.path.contains(".txt")){
                let data = NSData.init(contentsOf: url)
                self.webView.load(data! as Data, mimeType: "text/html", characterEncodingName: "UTF-8", baseURL: URL.init(fileURLWithPath: ""))
            }else if(self.path.contains(".pdf")){
                let data = NSData.init(contentsOf: url)
                self.webView.load(data! as Data, mimeType: "application/pdf", characterEncodingName: "UTF-8", baseURL: URL.init(fileURLWithPath: ""))
            }else{
                self.webView.loadFileURL(url, allowingReadAccessTo: url)
            }
        }
        self.channel?.invokeMethod("onShow", arguments: "")
    }
    
//    public func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
//        let webUrlStr = navigationAction.request.url?.absoluteString;
//        print("=======>")
//        print(webUrlStr)
//        if((webUrlStr?.contains(".pdf")) != nil){
//            let data = NSData.init(contentsOf: navigationAction.request.url!)
//            self.webView.load(data! as Data, mimeType: "application/pdf", characterEncodingName: "GBK", baseURL: URL.init(fileURLWithPath: ""))
//        }else if((webUrlStr?.contains(".txt")) != nil){
//            let data = NSData.init(contentsOf: navigationAction.request.url!)
//            self.webView.load(data! as Data, mimeType: "text/html", characterEncodingName: "GBK", baseURL: URL.init(fileURLWithPath: ""))
//        }
//        decisionHandler(.allow)
//    }
}

