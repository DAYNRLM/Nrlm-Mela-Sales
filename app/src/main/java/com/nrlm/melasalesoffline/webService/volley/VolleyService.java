package com.nrlm.melasalesoffline.webService.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nrlm.melasalesoffline.utils.AppUtility;

import org.json.JSONObject;

public class VolleyService {
    VolleyResult mResultCallback = null;
    Context mContext;
    AppUtility appUtility;

    public static VolleyService volleyService = null;
    public static VolleyService getInstance(Context context) {
        if (volleyService == null)
            volleyService = new VolleyService(context);
        return volleyService;
    }

    public VolleyService(Context mContext) {
       // this.mResultCallback = mResultCallback;
        this.mContext = mContext;
        appUtility =AppUtility.getInstance();
    }

    public void postDataVolley(String requestType, String url, JSONObject sendObj,VolleyResult mResultCallback) {
        this.mResultCallback = mResultCallback;
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });
            queue.getCache().clear();
            queue.add(jsonObj);
            jsonObj.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 6000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 0;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                    appUtility.showLog("retry policy error:- "+error,VolleyService.class);
                }
            });
        } catch (Exception e) {
            appUtility.showLog("post request Exception:- "+e,VolleyService.class);
        }
    }
}
