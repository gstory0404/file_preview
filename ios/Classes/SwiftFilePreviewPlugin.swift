import Flutter
import UIKit

public class SwiftFilePreviewPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "file_preview", binaryMessenger: registrar.messenger())
        let instance = SwiftFilePreviewPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        let filePreviewFactory = FilePreviewFactory(messenger: registrar.messenger())
        registrar.register(filePreviewFactory, withId: "com.gstory.file_preview/FilePreviewWidget")
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if(call.method == "initTBS"){
            result(true)
        }
    }
}
