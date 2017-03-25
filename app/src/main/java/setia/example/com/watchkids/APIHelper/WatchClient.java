package setia.example.com.watchkids.APIHelper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by My Computer on 3/25/2017.
 */

public class WatchClient {
    private static WatchAPI REST_CLIENT;
    private static String URL= "http://wkidsapi.azurewebsites.net/index.php/";
    static { //dieksekusi sebelum constructor, tapi hanya sekali untuk semua instans
        setupRestClient();
    }

    private WatchClient() {
        // TODO Auto-generated constructor stub
    }

    public static WatchAPI get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        Retrofit builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        REST_CLIENT = builder.create(WatchAPI.class);
    }
    public static String getURL(){
        return URL;
    }
}
