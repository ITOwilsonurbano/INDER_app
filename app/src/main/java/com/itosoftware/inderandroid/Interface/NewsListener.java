package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Communes.Communes;
import com.itosoftware.inderandroid.Api.News.New;

import java.util.ArrayList;

/**
 * Created by administrador on 9/11/15.
 */

public interface NewsListener {
    public void onfinishedNews(ArrayList<New> datos, Integer maxPage);
    public void onfinishedNew(New notice);
}

