package com.devandroid.mangakita;

import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.devandroid.mangakita.Adapter.MyComicAdapter;
import com.devandroid.mangakita.Adapter.MySliderAdapter;
import com.devandroid.mangakita.Common.Common;
import com.devandroid.mangakita.Model.Banner;
import com.devandroid.mangakita.Model.Manga;
import com.devandroid.mangakita.Retrofit.IComicAPI;
import com.devandroid.mangakita.Service.PicassoImageLoadingService;

import java.util.List;


import dmax.dialog.SpotsDialog;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity {

    private Slider slider;
    private IComicAPI iComicAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recycle_manga;
    private TextView txt_comic;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init API
        iComicAPI = Common.getAPI();

        //View
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext()))
                {
                    fetchBanner();
                    fetchManga();
                }
                else{
                    Toast.makeText(MainActivity.this, "Periksa jaringan anda", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Default load pertama kali
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext()))
                {
                    fetchBanner();
                    fetchManga();
                }
                else{
                    Toast.makeText(MainActivity.this, "Periksa jaringan anda", Toast.LENGTH_SHORT).show();
                }
            }
        });


        slider = (Slider)findViewById(R.id.banner_slider);
        Slider.init(new PicassoImageLoadingService());

        recycle_manga = (RecyclerView)findViewById(R.id.recycle_manga);
        recycle_manga.setHasFixedSize(true);
        recycle_manga.setLayoutManager(new GridLayoutManager(this,2));

        txt_comic = (TextView)findViewById(R.id.txt_comic);


    }

    private void fetchManga() {
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Tunggu Sebentar ...").setCancelable(false).build();
        if(!swipeRefreshLayout.isRefreshing())
            dialog.show();

        compositeDisposable.add(iComicAPI.getMangaList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Manga>>() {
                    @Override
                    public void accept(List<Manga> mangas) throws Exception {
                        recycle_manga.setAdapter(new MyComicAdapter(getBaseContext(), mangas));
                        txt_comic.setText(new StringBuilder("DAFTAR MANGA (")
                        .append(mangas.size())
                        .append(")"));
                        if (!swipeRefreshLayout.isRefreshing())
                        dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "Terjadi Kesalahan ...", Toast.LENGTH_SHORT).show();
                        if (!swipeRefreshLayout.isRefreshing())
                        dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }));
    }

    private void fetchBanner() {
        compositeDisposable.add(iComicAPI.getBannerList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Banner>>() {
            @Override
            public void accept(List<Banner> banners) throws Exception {
                slider.setAdapter(new MySliderAdapter(banners));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(MainActivity.this, "Error Loading Banner", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
