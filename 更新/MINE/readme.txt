1��������Manifest����ӱ�Ҫ��Ȩ��
1������Ȩ�ޱ���
  <uses-permission android:name="android.permission.INTERNET"/>
2���洢���Ķ�Ȩ��Ҳ�Ǳ���
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
3���ڰ汾�ϸ�ʱ��Ҫ��װȨ��--������˽Ȩ�޵��Ǳ���
  <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>

2�����xml����res�У�������Manifest���������
   ע�⡰������λ�ã��滻���Լ��İ���
 	<provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="����.fileProvider"
            tools:replace="android:authorities"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
        </provider>
3��layout�а��������ı�Ҫ���֣�drawable�а���layout�б��������
4�����ڴ洢Ϊ��˽Ȩ����Ҫ���ж�̬���������PictureSelector��Ϊ�������жԶ�̬Ȩ����������������������ʹ��
  implementation 'com.github.LuckSiege.PictureSelector:picture_library:v2.2.3'
��̬Ȩ��ʹ��ʾ����
 /*����洢Ȩ��*/
    private void rxPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {
                    new UpdateManager(MainActivity.this).checkUpdate();
                } else {
                    Toast.makeText(MainActivity.this, "��Ҫ��Ȩ�޲�������ʹ��", Toast.LENGTH_SHORT).show();
                    rxPermission();
                }
            }
        });
    }
5�������ʹ�÷����꿴UpdateManager.java�Լ�ʾ������˵��

