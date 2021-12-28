//
//  FilePreviewFactory.swift
//  file_preview
//
//  Created by gstory on 2021/12/27.
//

import Flutter

public class FilePreviewFactory: NSObject,FlutterPlatformViewFactory {
    private var messenger: FlutterBinaryMessenger

    init(messenger: NSObjectProtocol & FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }
    
    public func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return FilePreview(frame,  binaryMessenger: self.messenger,id: viewId, params:args)
    }
    
    public func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
        return FlutterStandardMessageCodec.sharedInstance()
    }
}
