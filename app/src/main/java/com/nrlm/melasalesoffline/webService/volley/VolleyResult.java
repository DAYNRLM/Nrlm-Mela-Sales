package com.nrlm.melasalesoffline.webService.volley;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResult {
    public void notifySuccess(String requestType, JSONObject response);
   // public void notifySuccess(String requestType, String response);
    public void notifyError(String requestType, VolleyError error);
}
