# TIM SDK （Mac）

## 下载地址

### 基础版
[最新 ImSDKForMac.framework 下载](https://im.sdk.qcloud.com/download/standard/5.1.62/TIM_SDK_Mac_latest_framework.zip)

### 增强版
[最新 ImSDKForMac_Plus.framework 下载](https://sdk-im-1252463788.cos.ap-hongkong.myqcloud.com/download/plus/5.9.1872/ImSDKForMac_Plus_5.9.1872.framework.zip)

### C接口版
[最新C接口下载](https://im.sdk.cloud.tencent.cn/download/plus/5.9.1872/cross_platform/ImSDK_Mac_C_5.9.1872.framework.zip)

### C++接口版
[最新C++接口下载](https://im.sdk.cloud.tencent.cn/download/plus/5.9.1872/cross_platform/ImSDK_Mac_CPP_5.9.1872.framework.zip)

## cocoaPods 集成
如果使用基础版 SDK，请您按照如下方式设置 Podfile 文件

```
source 'https://github.com/CocoaPods/Specs.git'
platform :macos, '10.10'

target 'TUIKitDemo' do
   pod 'TXIMSDK_Mac'

end

```

如果使用增强版 SDK，请您按照如下方式设置 Podfile 文件

```
source 'https://github.com/CocoaPods/Specs.git'
platform :macos, '10.10'

target 'TUIKitDemo' do
   pod 'TXIMSDK_Plus_Mac'

end

```
