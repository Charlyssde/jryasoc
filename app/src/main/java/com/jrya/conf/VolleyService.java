package com.jrya.conf;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyService {

    private static VolleyService mVolleyS = null;

    private RequestQueue mRequestQueue;

    private VolleyService (Context context){ mRequestQueue = Volley.newRequestQueue(context);}

    public static VolleyService getInstance(Context context){
        if(mVolleyS == null){
            mVolleyS = new VolleyService(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void addToQueue(Request request){
        if(request != null){
            request.setTag(this);
            if(mRequestQueue == null){
                mRequestQueue = getRequestQueue();
            }

            mRequestQueue.add(request);
        }
    }
}
