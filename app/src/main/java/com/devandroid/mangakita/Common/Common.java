package com.devandroid.mangakita.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.devandroid.mangakita.Model.Chapter;
import com.devandroid.mangakita.Model.Manga;
import com.devandroid.mangakita.Retrofit.IComicAPI;
import com.devandroid.mangakita.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static Manga selected_manga;
    public static Chapter selected_chapter;
    public static int chapter_index =-1;
    public static List<Chapter> chapterList = new ArrayList<>();

    public static IComicAPI getAPI(){
        return RetrofitClient.getInstance().create(IComicAPI.class);
    }

    public static String formatString(String name) {
        // Jika nama chapter terlalu panjang
        StringBuilder finalResult = new StringBuilder(name.length() > 15 ? name.substring(0,45)+"...":name);
        return finalResult.toString();
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if(networkInfo != null)
            {
                for (int i = 0; i<networkInfo.length;i++)
                    if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return  true;
            }
        }
        return false;
    }
}
