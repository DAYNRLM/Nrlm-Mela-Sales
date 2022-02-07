package com.nrlm.melasalesoffline.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.activity.BillGenerateActivity;

public class DialogFactory {

    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogWithStyle;

    public static DialogFactory dialogFactory;
    private androidx.appcompat.app.AlertDialog alertDialog;
    private androidx.appcompat.app.AlertDialog serverAlertDialog;

    AppSharedPreferences appSharedPreferences;
    AppUtility appUtility;
    Context context;

    public static DialogFactory getInstance(Context context) {
        if (dialogFactory == null)
            dialogFactory = new DialogFactory(context);
        return dialogFactory;
    }

    public DialogFactory(Context context) {
        this.context = context;
        appUtility =AppUtility.getInstance();
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(context);
    }

    /**
     * @param context                     Activity/Fragment context
     * @param titleIcon                   Title Icon
     * @param dialogMessage               Message Body of Dialog
     * @param positiveButtonText          Positive Button Text
     * @param dialogPositiveClickListener Positive Button click listener
     * @param negativeButtonText          Negative button text
     * @param dialogNegativeClickListener Negative Button click listner
     * @param isCancellable               Can dialog be canceled by clicking on outside
     * @return Created dialog instance/ object
     */
    public AlertDialog showAlertDialog(Context context, @Nullable int titleIcon, String title , String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, @Nullable String negativeButtonText, @Nullable DialogInterface.OnClickListener dialogNegativeClickListener, boolean isCancellable) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setCancelable(isCancellable);
        builder.setInverseBackgroundForced(true);
        if(title.equals("Server Error!")){
            builder.setTitle(title);
        }

        else {
            builder.setCustomTitle(View.inflate(context, R.layout.costum_title_view,null));
        }

        builder.setMessage(dialogMessage);
        if (titleIcon != 0)
            builder.setIcon(titleIcon);

        builder.setPositiveButton(positiveButtonText, dialogPositiveClickListener);

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener);
        }

        alert = builder.create();
        alert.show();
        return alert;
    }

    public AlertDialog showAlertDialog(Context context, int titleIcon,String title, String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener dialogPositiveClickListener, String negativeButtonText, boolean isCancellable) {

        return showAlertDialog(context, titleIcon,title, dialogMessage, positiveButtonText, dialogPositiveClickListener, negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }, isCancellable);

    }

    public AlertDialog showAlertDialog(Context ctx, int titleIcon,String title, String dialogMessage, String positiveButtonText, boolean isCancellable) {
        return showAlertDialog(ctx, titleIcon, title,dialogMessage, positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }, null, null, isCancellable);
    }


    public AlertDialog showAlertDialog(Context ctx, int titleIcon, String title,String dialogMessage, String positiveButtonText, DialogInterface.OnClickListener onPositiveClickListener, boolean isCancellable) {
        return showAlertDialog(ctx, 0,title,dialogMessage, positiveButtonText, onPositiveClickListener, null, null, isCancellable);
    }

    public Dialog showProgressBar(Context context, String titleMessage, boolean indeterminate){

        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage(titleMessage);
        progressDialog.setIndeterminate(indeterminate);
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        return progressDialog;
    }
    public Dialog showProgressBarWithStyle(Context context, String titleMessage,int progressStyle, boolean indeterminate){

        progressDialogWithStyle=new ProgressDialog(context);
        progressDialogWithStyle.setMessage(titleMessage);
        progressDialogWithStyle.setProgressStyle(progressStyle);
        progressDialogWithStyle.setIndeterminate(indeterminate);
        progressDialogWithStyle.setCancelable(false);
        progressDialogWithStyle.setProgress(0);
        return progressDialogWithStyle;
    }
    public void setProgressOfDialog(){
        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 2;
                        progressDialog.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
    public void showNoInternetDialog(Context context){
        alertDialog= DialogFactory.getInstance(context).showAlertDialog(context,0,"No Internet!","Please open your internet connection.", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        },true);
        alertDialog.show();

    }
    public ProgressDialog showProgressDialog(Context context){
        ProgressDialog progressDialogss = new ProgressDialog(context);
        progressDialogss.setMessage("Loading...");
        progressDialogss.setCancelable(false);
        return progressDialogss;
    }
    public void showServerErrorDialog(Context context,String message,String positiveButtonText ){
        serverAlertDialog= DialogFactory.getInstance(context).showAlertDialog(context,0,"Server Error!",message, positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        },true);
        serverAlertDialog.show();

    }
    public void showServerCridentialDialog(Context context, String title, String massege, String positiveButtonText, String negativeButtonText, @Nullable DialogInterface.OnClickListener dialogNegativeClickListener, boolean isCancleable, boolean finishActivity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTheme);
        builder.setTitle(title);
        builder.setMessage(massege);
        builder.setCancelable(isCancleable);
        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (finishActivity) {
                    System.exit(0);
                } else dialog.dismiss();
            }
        });
        if (dialogNegativeClickListener != null) {
            builder.setNegativeButton(negativeButtonText, dialogNegativeClickListener);
        }
        builder.show();
    }

    /*************material dialogs***************/
    public MaterialAlertDialogBuilder showMaterialDialog(String dialogMsg, int titleIcon ,boolean cancelable,String titelText
            ,String positiveText,DialogInterface.OnClickListener dialogPositiveClickListener
            ,@Nullable String negativeText,@Nullable DialogInterface.OnClickListener negativeClickListener){

        MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setCancelable(cancelable);
        materialAlertDialogBuilder.setIcon(titleIcon);
        materialAlertDialogBuilder.setTitle(titelText);
        materialAlertDialogBuilder.setMessage(dialogMsg);
        materialAlertDialogBuilder.setPositiveButton(positiveText, dialogPositiveClickListener);
        materialAlertDialogBuilder.setNegativeButton(negativeText,negativeClickListener);
        materialAlertDialogBuilder.show();
        return materialAlertDialogBuilder;
    }

}
