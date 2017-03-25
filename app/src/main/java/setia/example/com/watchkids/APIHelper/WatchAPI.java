package setia.example.com.watchkids.APIHelper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import setia.example.com.watchkids.Model.Respond;

/**
 * Created by My Computer on 3/25/2017.
 */

public interface WatchAPI {
    @GET("getLogin/{username}/{password}")
    Call<Respond> Login(@Path("username") String username, @Path("password") String password);
//    @GET("contacts.json")
//    Call<List<Contact>> ContactsList();
//
//    @GET("contacts/{id}.json")
//    Call<Contact> ContactDetail(@Path("id") int id);
//
//    @Multipart
//    @POST("contacts.json")
//    Call<Contact> CreateContact(@Part("contact[first_name]") RequestBody firstName,
//                                @Part("contact[last_name]") RequestBody lastName,
//                                @Part("contact[email]") RequestBody email,
//                                @Part("contact[phone_number]") RequestBody phoneNumber,
//                                @Part("contact[favorite]") RequestBody favorite);
//
//    @POST("contacts.json")
//    Call<Contact> CreateContact(@Body UploadContact contact);
}
