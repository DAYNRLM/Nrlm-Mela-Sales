package com.nrlm.melasalesoffline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.nrlm.melasalesoffline.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintJobInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.repository.CommonRepository;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.Cryptography;
import com.nrlm.melasalesoffline.utils.DateFactory;
import com.nrlm.melasalesoffline.utils.FilesUtils;
import com.nrlm.melasalesoffline.utils.NetworkFactory;
import com.nrlm.melasalesoffline.utils.PrefrenceManager;
import com.nrlm.melasalesoffline.utils.PrintUtil;
import com.nrlm.melasalesoffline.utils.interfaces.PrintCompleteService;
import com.github.barteksc.pdfviewer.PDFView;
import com.nrlm.melasalesoffline.webService.volley.VolleyResult;
import com.nrlm.melasalesoffline.webService.volley.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nrlm.melasalesoffline.utils.AppConstant.myAppDir;


public class BillGenerateActivity extends AppCompatActivity implements PrintCompleteService {

    @BindView(R.id.pdfView)
    PDFView pdfView;

    @BindView(R.id.print_invoice)
    TextView print_invoice;

    @BindView(R.id.home)
    TextView home;

    @BindView(R.id.sendUrlCardView)
    CardView sendUrlCardView;

    @BindView(R.id.toolbar)
    Toolbar  toolbar;
    @BindView(R.id.downLoadPdfBtn)
    TextView   downloadPdf;




    public static Context context;


    //print invoice ===========
    private File pdfFile;
    private WifiManager mWifiManager;
    private String connectionInfo, externalStorageDirectory;
    private Button mBtnPrint;
    private boolean isMobileDataConnection;
    private WifiConfiguration mPrinterConfiguration;
    private List<ScanResult> mScanResults;
    private WifiScanner mWifiScanner;
    private Handler mPrintStartHandler;
    private PrintManager mPrintManager;
    private PrintJob mCurrentPrintJob;
    private List<PrintJob> mPrintJobs;
    private Handler mPrintCompleteHandler;
    private WifiConfiguration mOldWifiConfiguration;

    private String pdfPath,pdfFilename;


    DateFactory dateFactory;
    AppUtility appUtility;
    CommonRepository commonRepository;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;
    VolleyResult mResultCallBack=null;
    VolleyService volleyService;

    String sendUrlStatus ="";
    String getInvoiceNumber ="";
    String shortUrl="";
    String customerGetMobileNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generate);
        ButterKnife.bind(this);
        context=this;
        mPrintStartHandler=new Handler();
        appSharedPreferences = AppSharedPreferences.getsharedprefInstances(context);
        dateFactory =DateFactory.getInstance();
        dateFactory =DateFactory.getInstance();
        appUtility =AppUtility.getInstance();
        commonRepository =CommonRepository.getInstance(BillGenerateActivity.this);
        filesUtils  =FilesUtils.getInstance(BillGenerateActivity.this);
        volleyService =VolleyService.getInstance(BillGenerateActivity.this);


        pdfPath=appSharedPreferences.getPdfPath();
        sendUrlStatus =appSharedPreferences.getSendUrlStatus();
        getInvoiceNumber =appSharedPreferences.getInvoiceNumber();
        setUpToolBar();

        if(sendUrlStatus.equalsIgnoreCase("done")){          //changed for the security audit purpose it's changed to visible
                                                                           //  after that
            sendUrlCardView.setVisibility(View.VISIBLE);
        }

        File file = new File(pdfPath);
        pdfView.fromFile(file).load();

        print_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // printDocument(file);
               // printFile();

                printDocument(new File(pdfPath));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBillGeneratePreference();
                Intent  intent =new Intent(BillGenerateActivity.this,HomeActivity.class);
                startActivity(intent);
               /* startActivity(new Intent(BillGenerateActivity.this,HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));*/
            }
        });

        /*downloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File folder = new File(externalStorageDirectory,pdfPath);
                // File folder = new File(absoluteFilePath);
               // pdfFile = new File(folder, pdfFilename);
                Uri path = Uri.fromFile(folder);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "pdf");
                try {
                    startActivity(pdfOpenintent);
                }
                catch (ActivityNotFoundException e) {
                  Toast.makeText(BillGenerateActivity.this,"something Went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }


    private void setUpToolBar() {
        TextView tv =toolbar.findViewById(R.id.tbTitle);                      //temprory comment
      ImageView iv =toolbar.findViewById(R.id.iconbackImg);

       iv.setVisibility(View.VISIBLE);
        tv.setText("Bill Invoice");

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillGenerateActivity.this,HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
    }
    private void printFile(){

        try {
          //  externalStorageDirectory = Environment.getExternalStoragePublicDirectory(Docu).toString();
           AppUtility.getInstance().showLog("pdfPath"+pdfPath,BillGenerateActivity.class);
           // File folder = new File(appSharedPreferences.getShgFolderPath(),pdfPath);
            // File folder = new File(absoluteFilePath);
            pdfFile = new File(pdfPath);
          //  AppUtility.getInstance().showLog("folder="+folder+"fileName="+pdfFilename,LoginActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiManager.startScan();
        mWifiScanner =new WifiScanner();

        Toast.makeText(BillGenerateActivity.this, "Please, Wait Printer is Preparing", Toast.LENGTH_SHORT).show();
        connectionInfo = PrintUtil.connectionInfo(BillGenerateActivity.this);
        AppUtility.getInstance().showLog("connectionInfo="+connectionInfo,LoginActivity.class);

        if (connectionInfo.equalsIgnoreCase(AppConstant.CONTROLLER_MOBILE)) {
            isMobileDataConnection = true;

            if (mWifiManager.isWifiEnabled() == false) {
                Toast.makeText(BillGenerateActivity.this, "Enabling WiFi..", Toast.LENGTH_LONG).show();
                mWifiManager.setWifiEnabled(true);
            }

            mWifiManager.startScan();

            printerConfiguration();

        } else if (connectionInfo.equalsIgnoreCase(AppConstant.CONTROLLER_WIFI)) {
            PrintUtil.storeCurrentWiFiConfiguration(BillGenerateActivity.this);

            printerConfiguration();

        } else {
            Toast.makeText(BillGenerateActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printerConfiguration() {
        mPrinterConfiguration = PrintUtil.getWifiConfiguration(BillGenerateActivity.this, AppConstant.CONTROLLER_PRINTER);
        if (mPrinterConfiguration == null) {
            showWifiListActivity(AppConstant.REQUEST_CODE_PRINTER);
        } else {
            boolean isPrinterAvailable = false;
             mWifiManager.startScan();
            mWifiScanner =new WifiScanner();
            if (mScanResults.size()>0) {
                for (int i = 0; i < mScanResults.size(); i++) {
                    if (mPrinterConfiguration.SSID.equals("\"" + mScanResults.get(i).SSID + "\"")) {
                        isPrinterAvailable = true;
                        break;
                    }
                }
            }else {
                Toast.makeText(BillGenerateActivity.this,"No wifi Detected.",Toast.LENGTH_SHORT).show();
            }
            if (isPrinterAvailable) {
                connectToWifi(mPrinterConfiguration);
                doPrint();

            } else {
                showWifiListActivity(AppConstant.REQUEST_CODE_PRINTER);
            }

        }
    }
    private void showWifiListActivity(int requestCode) {
        try {
            Intent iWifi = new Intent(BillGenerateActivity.this, WifiListActivity.class);
            startActivityForResult(iWifi, requestCode);
        }catch (ActivityNotFoundException anf){
            AppUtility.getInstance().showLog("ActivityNotFoundException"+anf,BillGenerateActivity.class);
        }

    }
    private void connectToWifi(WifiConfiguration mWifiConfiguration) {
        mWifiManager.enableNetwork(mWifiConfiguration.networkId, true);
    }

    public void switchConnection() {
        if (!isMobileDataConnection) {
            mOldWifiConfiguration = PrintUtil.getWifiConfiguration(BillGenerateActivity.this, AppConstant.CONTROLLER_WIFI);

            boolean isWifiAvailable = false;

            mWifiManager.startScan();

            for (int i = 0; i < mScanResults.size(); i++) {
                if (mOldWifiConfiguration.SSID.equals("\"" + mScanResults.get(i).SSID + "\"")) {
                    isWifiAvailable = true;
                    break;
                }
            }

            if (isWifiAvailable) {
                connectToWifi(mOldWifiConfiguration);
            } else {
                showWifiListActivity(AppConstant.REQUEST_CODE_WIFI);
            }
        } else {
            mWifiManager.setWifiEnabled(false);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessage(int status) {
        mPrintJobs = mPrintManager.getPrintJobs();
        mPrintCompleteHandler=new Handler();
        mPrintCompleteHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPrintCompleteHandler.postDelayed(this, 2000);

                if (mCurrentPrintJob.getInfo().getState() == PrintJobInfo.STATE_COMPLETED) {

                    for (int i = 0; i < mPrintJobs.size(); i++) {
                        if (mPrintJobs.get(i).getId() == mCurrentPrintJob.getId()) {
                            mPrintJobs.remove(i);
                        }
                    }
                    Toast.makeText(BillGenerateActivity.this, "Printed Successfully!", Toast.LENGTH_LONG).show();

                   // switchConnection();

                    mPrintCompleteHandler.removeCallbacksAndMessages(null);
                } else if (mCurrentPrintJob.getInfo().getState() == PrintJobInfo.STATE_FAILED) {
                   // switchConnection();
                    Toast.makeText(BillGenerateActivity.this, "Print Failed!", Toast.LENGTH_LONG).show();
                    mPrintCompleteHandler.removeCallbacksAndMessages(null);
                } else if (mCurrentPrintJob.getInfo().getState() == PrintJobInfo.STATE_CANCELED) {
                    //switchConnection();
                    Toast.makeText(BillGenerateActivity.this, "Print Cancelled!", Toast.LENGTH_LONG).show();
                    mPrintCompleteHandler.removeCallbacksAndMessages(null);
                }
            }
        }, 2000);
    }


    public class WifiScanner extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mScanResults = mWifiManager.getScanResults();
            Log.e("scan result size", "" + mScanResults.size());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void doPrint() {
        mPrintStartHandler.postDelayed(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {

             //   Log.d("PrinterConnection Status", "" + mPrinterConfiguration.status);

                mPrintStartHandler.postDelayed(this, 2000);

                if (mPrinterConfiguration.status == WifiConfiguration.Status.ENABLED) {
                    if (PrintUtil.computePDFPageCount(pdfFile) > 0) {
                        printDocument(pdfFile);
                    } else {
                        Toast.makeText(BillGenerateActivity.this, "Can't print, Page count is zero.", Toast.LENGTH_LONG).show();
                    }
                    mPrintStartHandler.removeCallbacksAndMessages(null);
                } else if (mPrinterConfiguration.status == WifiConfiguration.Status.DISABLED) {
                    Toast.makeText(BillGenerateActivity.this, "Failed to connect to printer!.", Toast.LENGTH_LONG).show();
                    mPrintStartHandler.removeCallbacksAndMessages(null);
                }
            }
        }, 2000);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printDocument(File pdfFile) {
        mPrintManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        mCurrentPrintJob = mPrintManager.print(jobName, new PrintServicesAdapter(BillGenerateActivity.this, pdfFile), null);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* try {
            registerReceiver(mWifiScanner, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mWifiManager.startScan();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mWifiScanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.REQUEST_CODE_PRINTER && resultCode == AppConstant.RESULT_CODE_PRINTER) {
            mPrinterConfiguration = PrintUtil.getWifiConfiguration(BillGenerateActivity.this, AppConstant.CONTROLLER_PRINTER);
            doPrint();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public class PrintServicesAdapter extends PrintDocumentAdapter {
        private Activity mActivity;
        private int pageHeight;
        private int pageWidth;
        private PrintedPdfDocument myPdfDocument;
        private int totalpages = 1;
        private File pdfFile;
        private PrintCompleteService mPrintCompleteService;


        public PrintServicesAdapter(Activity mActivity, File pdfFile) {
            this.mActivity = mActivity;
            this.pdfFile = pdfFile;
            this.totalpages = PrintUtil.computePDFPageCount(pdfFile);
            this.mPrintCompleteService = (PrintCompleteService) mActivity;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes
                , PrintAttributes newAttributes
                , CancellationSignal cancellationSignal
                , LayoutResultCallback callback
                , Bundle extras) {

           try {

               myPdfDocument = new PrintedPdfDocument(mActivity, newAttributes);

               pageHeight =
                       newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
               pageWidth =
                       newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

               if (cancellationSignal.isCanceled()) {
                   callback.onLayoutCancelled();
                   return;
               }

               if (totalpages > 0) {
                   PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                           .Builder(pdfFile.getName())
                           .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                           .setPageCount(totalpages);

                   PrintDocumentInfo info = builder.build();
                   callback.onLayoutFinished(info, true);
               } else {
                   callback.onLayoutFailed("Page count is zero.");
               }
           }catch (Exception e){
               startActivity(new Intent(BillGenerateActivity.this,BillGenerateActivity.class));
           }
        }

        @Override
        public void onWrite(PageRange[] pages
                , ParcelFileDescriptor destination
                , CancellationSignal cancellationSignal
                , WriteResultCallback callback) {

            FileInputStream input = null;
            FileOutputStream output = null;


            try {
                input = new FileInputStream(pdfFile);
                output = new FileOutputStream(destination.getFileDescriptor());
                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }

                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});


            } catch (FileNotFoundException ee) {
                //Catch exception
            } catch (Exception e) {
                //Catch exception
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {
                    mPrintCompleteService.onMessage(AppConstant.PRINTER_STATUS_CANCELLED);
                }
            });
        }

        @Override
        public void onFinish() {
            mPrintCompleteService.onMessage(AppConstant.PRINTER_STATUS_COMPLETED);
        }
    }

    @OnClick(R.id.sendUrlCardView)
    void sendUrlToCustomer(){

        if(NetworkFactory.isInternetOn(BillGenerateActivity.this)){
            MaterialButton cancelBtn,sendBtn;
            TextView invoiceTv;
            TextInputEditText cusMobileEt;
            TextInputLayout cusMobInputLayout;

            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(BillGenerateActivity.this);
            View customLayout = getLayoutInflater().inflate(R.layout.customer_mobile_number_layout, null);
            // ButterKnife.bind(this, customLayout);
            materialAlertDialogBuilder.setView(customLayout);
            materialAlertDialogBuilder.setCancelable(false);
            AlertDialog cusDialog =materialAlertDialogBuilder.show();

            cancelBtn =customLayout.findViewById(R.id.cancelBtn);
            sendBtn =customLayout.findViewById(R.id.sendBtn);
            invoiceTv =customLayout.findViewById(R.id.invoiceTv);
            cusMobileEt =customLayout.findViewById(R.id.cusMobileEt);
            cusMobInputLayout =customLayout.findViewById(R.id.cusMobInputLayout);

            cusMobileEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    cusMobInputLayout.setError(null);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            invoiceTv.setText(getInvoiceNumber);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cusDialog.dismiss();
                }
            });

            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerGetMobileNumber = cusMobileEt.getText().toString();
                    if(customerGetMobileNumber.length()<10){
                        cusMobInputLayout.setError("Enter valid mobile number");
                    }else {
                        if(appUtility.isValid(customerGetMobileNumber)){
                            if(NetworkFactory.isInternetOn(BillGenerateActivity.this)){
                                createShortDynamicLink(cusDialog);
                            }
                        }else {
                            cusMobInputLayout.setError("Invaild mobile number");
                           // cusMobileEt.setText("");
                        }
                    }
                }
            });

        }else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(BillGenerateActivity.this);
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.setMessage("Please enable y" +
                    "our internet...");
            materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            materialAlertDialogBuilder.show();

        }
    }

    private String createShortDynamicLink( AlertDialog cusDialog) {

        ProgressDialog progressDialog =new ProgressDialog(BillGenerateActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String customUrl =AppConstant.CUSTOM_URL+getInvoiceNumber+"&mobile="+appSharedPreferences.getMobile();
       // String customUrl ="http://10.197.183.112:8080/nrlmdev_audit_5_2018/MelaAction.do?methodName=generateMelaBill&invoice_no=24851-INM&mobile=9898989898";
       // String customUrl ="https://nrlm.gov.in";
        try {
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(customUrl))
                   // .setDomainUriPrefix("https://melasalesoffline.page.link")
                    .setDomainUriPrefix("https://melabill.page.link")
                    /*.setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    // Open links with com.example.ios on iOS
                    .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())*/

                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                shortUrl =shortLink.toString();
                                appUtility.showLog("short link Exception:-"+shortUrl,BillGenerateActivity.class);
                                cusDialog.dismiss();
                                callSendUrlApi();
                            } else {
                                appUtility.showLog("short link Exception:-"+task.getException(),BillGenerateActivity.class);
                            }
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            cusDialog.dismiss();
                            appUtility.showLog("SHORT LINK URL "+e,BillGenerateActivity.class);
                            callSendUrlApi();
                            Toast.makeText(BillGenerateActivity.this,"Bill not sent..",Toast.LENGTH_SHORT).show();
                        }
                    });

        }catch (Exception e){
            appUtility.showLog("short link Exception:-"+e,BillGenerateActivity.class);
        }
        return customUrl;
    }

    private void callSendUrlApi() {
        ProgressDialog progressDialog =new ProgressDialog(BillGenerateActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JSONObject sendUrlJson =new JSONObject();
        try {
            sendUrlJson.accumulate("melaId",AppConstant.MELA_ID);
            sendUrlJson.accumulate("mobile",appSharedPreferences.getMobile());
            sendUrlJson.accumulate("imei_no",appSharedPreferences.getImei());
            sendUrlJson.accumulate("device_name",appSharedPreferences.getDeviceInfo());
            sendUrlJson.accumulate("location_coordinate","00.00");
            sendUrlJson.accumulate("custmobile",customerGetMobileNumber);
            sendUrlJson.accumulate("messages",shortUrl);
        } catch (JSONException e) {
            e.printStackTrace();
            appUtility.showLog("URl json create Exception:- "+e,BillGenerateActivity.class);
        }


        /***********making jsonObject encrypted**********/
        JSONObject masterObjectUrlEncrypted = new JSONObject();
        try {
            Cryptography cryptography = new Cryptography();
            String encrypted = cryptography.encrypt(sendUrlJson.toString());
            masterObjectUrlEncrypted.accumulate("data", encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mResultCallBack =new VolleyResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                appUtility.showLog("get response:-" +response,BillGenerateActivity.class);
                progressDialog.dismiss();

                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(BillGenerateActivity.this);
                materialAlertDialogBuilder.setCancelable(false);
                materialAlertDialogBuilder.setMessage("Bill sent successfully...");
                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearBillGeneratePreference();
                        Intent  intent =new Intent(BillGenerateActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                });
                materialAlertDialogBuilder.show();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                appUtility.showLog("volley error:-" +error,BillGenerateActivity.class);
                progressDialog.dismiss();
                MaterialAlertDialogBuilder materialAlertDialogBuilder =new MaterialAlertDialogBuilder(BillGenerateActivity.this);
                materialAlertDialogBuilder.setCancelable(false);
                materialAlertDialogBuilder.setMessage("Server error bill sending problem ");
                materialAlertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                materialAlertDialogBuilder.show();
            }
        };
        volleyService.postDataVolley("send_url_request",AppConstant.BILL_SEND_URL,masterObjectUrlEncrypted,mResultCallBack);
    }

    public void clearBillGeneratePreference(){
        appSharedPreferences.removeSharedPrefKey(PrefrenceManager.getPrefKeyInvoiceNumber());
        appSharedPreferences.removeSharedPrefKey(PrefrenceManager.getPrefKeySendUrlStatus());
        appSharedPreferences.removeSharedPrefKey(PrefrenceManager.getPrefPdfPath());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearBillGeneratePreference();
        startActivity(new Intent(BillGenerateActivity.this,HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}