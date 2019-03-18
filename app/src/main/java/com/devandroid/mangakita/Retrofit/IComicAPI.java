package com.devandroid.mangakita.Retrofit;

import com.devandroid.mangakita.Model.Banner;
import com.devandroid.mangakita.Model.Chapter;
import com.devandroid.mangakita.Model.Link;
import com.devandroid.mangakita.Model.Manga;

import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IComicAPI {
    @GET("banner")
    Observable<List<Banner>> getBannerList();

    @GET("manga")
    Observable<List<Manga>> getMangaList();

    @GET("chapter/{comicid}")
    Observable<List<Chapter>> getChapterList(@Path("comicid")int comicid);

    @GET("links/{chapterid}")
    Observable<List<Link>> getImageList(@Path("chapterid")int chapterid);
}
