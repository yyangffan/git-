���ø��¿�����Ҫ�ģ�

1��build����Ӷ�fastjson��Devring������

implementation 'com.alibaba:fastjson:1.2.58'
implementation 'com.alibaba:fastjson:1.1.71.android'
implementation 'com.ljy.ring:devring:1.1.8'//�������

2����Ҫ��Devring���г�ʼ��������CheckUpUtil���ж����ĳ�ʼ����ȻҲ������Application�н��г�ʼ�������ʹ��CheckUpUtil��Devring�ĳ�ʼ����Ҫ��Application�н�����Ӧ�����ĵ���
DevRing.init(this);
DevRing.configureHttp().setBaseUrl(Constant.BASE_URL).setConnectTimeout(60).setIsUseLog(true);
DevRing.configureOther().setIsUseCrashDiary(true);
DevRing.configureImage();

DevRing.create();

3��ʹ��DevRing��Ҫ����һ��Interface����--Service
   ���������д����Ŀ��������Ҫʹ�õ��Ľӿ�
�������£�

    @POST("bussiness/login/login")
    Observable<JSONObject> getDetail(@Body RequestBody map);
		
    @Streaming
    @GET
    Observable<JSONObject> download(@Url String download_url);
