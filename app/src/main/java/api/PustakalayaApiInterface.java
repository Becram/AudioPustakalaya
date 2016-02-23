package api;

import java.util.List;

import model.AllAudioBooks;
import model.BookWrapper;
import model.ModelAudioBookDetails;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by bikram on 1/21/16.
 */
public interface PustakalayaApiInterface {

    @GET("/api/editors_pick")
     Call<BookWrapper> getBook();

//    @GET("/api/listAllAudioBook/1/200/date/asc")
//    Call<AllAudioBooks>  getAllAudioBooks();

    @GET("/api/listAllAudioBook/1/200/{sort}/asc")
    Call<AllAudioBooks>  getAllAudioBooks(@Path("sort") String sort);


    @GET("/api/listAllChapters/{bookid}")
   Call<ModelAudioBookDetails>  getAudioBooksDetails(@Path("bookid") String bookid);
//    Call<ModelAudioBookDetails>  getAudioBooksDetails();
 }
