调用更新可能需要的：

1、build中添加对fastjson和Devring的依赖

implementation 'com.alibaba:fastjson:1.2.58'
implementation 'com.alibaba:fastjson:1.1.71.android'
implementation 'com.ljy.ring:devring:1.1.8'//网络访问

2、需要对Devring进行初始化，不过CheckUpUtil中有对它的初始化当然也可以在Application中进行初始化，如果使用CheckUpUtil对Devring的初始化需要在Application中进行相应方法的调用
DevRing.init(this);
DevRing.configureHttp().setBaseUrl(Constant.BASE_URL).setConnectTimeout(60).setIsUseLog(true);
DevRing.configureOther().setIsUseCrashDiary(true);
DevRing.configureImage();

DevRing.create();

3、使用DevRing需要创建一个Interface的类--Service
   在这个里面写上项目中所有需要使用到的接口
样例如下：

    @POST("bussiness/login/login")
    Observable<JSONObject> getDetail(@Body RequestBody map);
		
    @Streaming
    @GET
    Observable<JSONObject> download(@Url String download_url);
