package id.arei.home.wallpaperapplication.base_api;

public class UtilsAPI {
    public static final String BASE_URL_API = "https://www.googleapis.com/blogger/v3/blogs/5179248854081608777/";

    public static BaseAPIService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseAPIService.class);
    }
}