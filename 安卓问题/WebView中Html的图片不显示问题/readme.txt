方法1、设置DOM Storage缓存
DomStorage包括Session Storage和Local Storage两种，你还可以这样设置

webview.getSettings().setDatabaseEnabled(true); webview.getSettings().setDatabasePath("/data/data");
settings.setDomStorageEnabled(true);?//设置DOM Storage缓存
方法2、允许混合
websettings.setBlockNetworkImage(false);//不阻塞网络图片
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//允许混合（http，https）
//websettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
websettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
}