����1������DOM Storage����
DomStorage����Session Storage��Local Storage���֣��㻹������������

webview.getSettings().setDatabaseEnabled(true); webview.getSettings().setDatabasePath("/data/data");
settings.setDomStorageEnabled(true);?//����DOM Storage����
����2��������
websettings.setBlockNetworkImage(false);//����������ͼƬ
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//�����ϣ�http��https��
//websettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
websettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
}