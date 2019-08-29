1.为WelcomeActivity的设置  android:theme="@style/StartTheme"
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="android:windowTranslucentStatus">true</item>
    </style>

    <style name="StartTheme" parent="@style/AppTheme">
        <item name="android:windowBackground">@drawable/load_four</item>
    </style>
2.将需要的drawable的图片以及效果图copy到项目总
3.复制MvpAdapter到项目中
4.参考WelcomeActivity进行代码的修改