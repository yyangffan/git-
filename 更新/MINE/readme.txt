1、首先在Manifest中添加必要的权限
1）联网权限必须
  <uses-permission android:name="android.permission.INTERNET"/>
2）存储及阅读权限也是必须
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
3）在版本较高时需要安装权限--并非隐私权限但是必须
  <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>

2、添加xml包到res中，并且在Manifest中添加如下
   注意“包名”位置，替换成自己的包名
 	<provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="包名.fileProvider"
            tools:replace="android:authorities"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
        </provider>
3、layout中包含弹窗的必要布局，drawable中包含layout中必须的配置
4、由于存储为隐私权限需要进行动态申请可依赖PictureSelector因为它其中有对动态权限三方的依赖，可以拿来使用
  implementation 'com.github.LuckSiege.PictureSelector:picture_library:v2.2.3'
动态权限使用示例：
 /*请求存储权限*/
    private void rxPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                    new UpdateManager(MainActivity.this).checkUpdate();
                } else {
                    Toast.makeText(MainActivity.this, "需要该权限才能正常使用", Toast.LENGTH_SHORT).show();
                    rxPermission();
                }
            }
        });
    }
5、具体的使用方法详看UpdateManager.java以及示例代码说明

