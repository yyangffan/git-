1、在xml中创建network_security_config：

2、Manifest的application标签中添加如下：
android:networkSecurityConfig="@xml/network_security_config"
android:usesCleartextTraffic="true"

3、将targetSdkVersion降到27或以下
