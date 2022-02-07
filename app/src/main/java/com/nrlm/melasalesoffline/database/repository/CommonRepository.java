package com.nrlm.melasalesoffline.database.repository;

import android.content.Context;

import androidx.room.ColumnInfo;

import com.nrlm.melasalesoffline.R;
import com.nrlm.melasalesoffline.database.AppDataBase;
import com.nrlm.melasalesoffline.database.entity.BillSyncEntity;
import com.nrlm.melasalesoffline.database.entity.MainHelperEntity;
import com.nrlm.melasalesoffline.database.entity.ProductAddTemptableEntity;
import com.nrlm.melasalesoffline.database.entity.ProductDescriptionEntity;
import com.nrlm.melasalesoffline.database.entity.ProductEntity;
import com.nrlm.melasalesoffline.database.entity.ShgDetailEntity;
import com.nrlm.melasalesoffline.fragment.DashBoardFragment;
import com.nrlm.melasalesoffline.utils.AppConstant;
import com.nrlm.melasalesoffline.utils.AppSharedPreferences;
import com.nrlm.melasalesoffline.utils.AppUtility;
import com.nrlm.melasalesoffline.utils.FilesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommonRepository {
    Context mContext;
    AppUtility appUtility;
    AppSharedPreferences appSharedPreferences;
    FilesUtils filesUtils;

    public static CommonRepository commonRepository = null;
    public static CommonRepository getInstance(Context context) {
        if (commonRepository == null)
            commonRepository = new CommonRepository(context);
        return commonRepository;
    }

    public CommonRepository(Context context) {
        this.mContext = context;
        appUtility = AppUtility.getInstance();
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(context);
        filesUtils =FilesUtils.getInstance(context);
    }

    public void saveShgData(ShgDetailEntity shgDetailEntity){
        AppDataBase.getDatabase(mContext).shgDetailDao().insertAll(shgDetailEntity);
    }

    /***get all shg data from main table ****/
    public List<ShgDetailEntity> getAllShgData(){
       List<ShgDetailEntity> shgDetailEntities = AppDataBase.getDatabase(mContext).shgDetailDao().getAllShgData();
       return shgDetailEntities;
    }

    /********fatching mainHelperEntity Data*************/
    public MainHelperEntity mainHelperEntityData()
    {
        MainHelperEntity mainHelperEntities=AppDataBase.getDatabase(mContext).mainHelperDao().getHelperData();
        return mainHelperEntities;
    }
    public String getShgName(String shgRegId){
        String shgName ="";
        ShgDetailEntity shgDetailEntity =  AppDataBase.getDatabase(mContext).shgDetailDao().getShgName(shgRegId);
        String shgcode =shgDetailEntity.getShg_code();
        shgName =shgDetailEntity.getGroup_name()+" ("+shgcode+") "+mContext.getString(R.string.shg);
        return shgName;
    }



    /****delete all data ******/
    public void deleteAllData(){
        AppDataBase.getDatabase(mContext).shgDetailDao().deleteAll();
        AppDataBase.getDatabase(mContext).mainHelperDao().deleteAll();
        AppDataBase.getDatabase(mContext).productDescriptionDao().deleteAll();
        AppDataBase.getDatabase(mContext).productDao().deleteAll();
        AppDataBase.getDatabase(mContext).productTempDao().deleteAll();
    }

    /************save main helper data********/
    public void saveMaineHelper(MainHelperEntity user){
        AppDataBase.getDatabase(mContext).mainHelperDao().insertAll(user);
    }

    /************save product data********/
    public void saveProductdata(ProductEntity user){
        AppDataBase.getDatabase(mContext).productDao().insertAll(user);
    }

    public List<ProductEntity> getAllProductData(){
        List<ProductEntity> productList =AppDataBase.getDatabase(mContext).productDao().getAllShgData();
        return productList;
    }

    public String getProductName(String pId){
        ProductEntity productEntity =AppDataBase.getDatabase(mContext).productDao().getQuantity(pId);
        String productName =productEntity.getProduct_name();
        return productName;

    }

    /******************geting the product type against the productDescriptionId***********/
    public String getProductType(String scanProductId,String productDescriptionId)
    {
        ProductDescriptionEntity productDescriptionEntity= AppDataBase.getDatabase(mContext).productDescriptionDao().getSpecificProductSpec(scanProductId,productDescriptionId);
        String productType=productDescriptionEntity.product_type;
        return productType;
    }

    /*************geting product unit price from the Product Description table************/
    public String getProductPrice(String scanProductId,String productDescriptionId)
    {
        ProductDescriptionEntity productDescriptionEntity= AppDataBase.getDatabase(mContext).productDescriptionDao().getSpecificProductSpec(scanProductId,productDescriptionId);
        String productUnitPrice=productDescriptionEntity.unit_price;
        return productUnitPrice;
    }

    public void updateAvalableQuantity(String productId,String difference){
        AppDataBase.getDatabase(mContext).productDao().updateQuantity(productId,difference);
    }

    public String getQuantity(String pId){
        ProductEntity productEntity =AppDataBase.getDatabase(mContext).productDao().getQuantity(pId);
        String getQuantity =productEntity.getAvailable_quantity();
        return getQuantity;
    }

    /************save product description data********/
    public void saveProductDescdata(ProductDescriptionEntity user){
        AppDataBase.getDatabase(mContext).productDescriptionDao().insertAll(user);
    }

    public List<ProductDescriptionEntity> getAllProductDescData(String pId){
        List<ProductDescriptionEntity> productDescList =AppDataBase.getDatabase(mContext).productDescriptionDao().getAllProductIdDesc(pId);
        return productDescList;
    }

    /****************add productin temp table**************/
    public void saveProductTempData(ProductAddTemptableEntity productAddTemptableEntity){
        AppDataBase.getDatabase(mContext).productTempDao().insertAll(productAddTemptableEntity);
    }

    public List<ProductAddTemptableEntity> getAllTempdata(){
        List<ProductAddTemptableEntity> tempList =AppDataBase.getDatabase(mContext).productTempDao().getAllTempData();
        return tempList;
    }

    public void deletTempData(int uid){
        AppDataBase.getDatabase(mContext).productTempDao().deleteBasedOnUid(uid);
    }

    public void deletAllTempData(){
        AppDataBase.getDatabase(mContext).productTempDao().deleteAll();
    }
    public int getTotalPriceSumFromTemp(){
        List<ProductAddTemptableEntity> tempList =AppDataBase.getDatabase(mContext).productTempDao().getAllTempData();
        int total=0;
        for(int i=0;i<tempList.size();i++){
            total = total+tempList.get(i).getProduct_total_price();
        }
        return total;
    }


    public void getTempUpdateProductQuantity(){
        List<ProductAddTemptableEntity> tempList =AppDataBase.getDatabase(mContext).productTempDao().getAllTempData();
        if(!tempList.isEmpty()){
            int i =0;
            for(ProductAddTemptableEntity pd:tempList){
                String pId =pd.getProduct_id();
                int pQuant  =pd.getProduct_quantity();
                updateProductQuantityData(pId,pQuant);
            }
            AppDataBase.getDatabase(mContext).productTempDao().deleteAll();

        }
    }

    private void updateProductQuantityData(String pid,int pQuant) {
        ProductEntity productEntity = AppDataBase.getDatabase(mContext).productDao().getQuantity(pid);
        int currQuantity = Integer.parseInt(productEntity.getAvailable_quantity());
        currQuantity =currQuantity+pQuant;
        updateAvalableQuantity(pid, String.valueOf(currQuantity));
    }


    /*********************bill sync data tables***************************/
    public void saveBillSyncData(BillSyncEntity billSyncEntity){
        AppDataBase.getDatabase(mContext).billSyncDao().insertAll(billSyncEntity);
    }
    public  List<BillSyncEntity> getBillDataWithStatus(String flag){
        List<BillSyncEntity> list = AppDataBase.getDatabase(mContext).billSyncDao().getAllFlagBillData(flag);
        return list;
    }

    public void updateStatusFlag(String flag,String invoice){
        AppDataBase.getDatabase(mContext).billSyncDao().updateFlageStatus(flag,invoice);
    }

    public JSONObject getUnsyncBillJson(){

        List<BillSyncEntity> list = AppDataBase.getDatabase(mContext).billSyncDao().getAllFlagBillData("0");
        JSONObject getBillJson = new JSONObject();
        try {
            getBillJson.accumulate("data",getBillJsonArray(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getBillJson;

    }

    private JSONArray getBillJsonArray(List<BillSyncEntity> list) {
        JSONArray jsonArray =new JSONArray();
        try{
            for(BillSyncEntity billEntity:list){
                JSONObject billObject =new JSONObject();
                billObject.accumulate("invoice_number",billEntity.getInvoice_number());
                billObject.accumulate("bill_generated_date",billEntity.getBill_generated_date());
                billObject.accumulate("bill_generated_time",billEntity.getBill_generated_time());
                billObject.accumulate("bill_created_date",billEntity.getBill_created_date());
                billObject.accumulate("shg_reg_id",billEntity.getShg_reg_id());
                billObject.accumulate("shg_code",billEntity.getShg_code());
                billObject.accumulate("product_Qty",billEntity.getProduct_Qty());
                billObject.accumulate("product_Id",billEntity.getProduct_Id());
                billObject.accumulate("mela_Id",billEntity.getMela_Id());
                billObject.accumulate("unit_Price",billEntity.getUnit_Price());
                billObject.accumulate("status_flag",billEntity.getStatus_flag());
                billObject.accumulate("product_desc_Id",billEntity.getProduct_desc_Id());
                billObject.accumulate("login_mobile_no",billEntity.getLogin_mobile_no());
                billObject.accumulate("unique_bill_number",billEntity.getUnique_bill_number());
                billObject.accumulate("total_amount",billEntity.getTotal_amount());
                billObject.accumulate("generated_bill_no",billEntity.getGenerated_bill_no());
                jsonArray.put(billObject);
            }
        }catch (Exception e){

        }
        return jsonArray;
    }

    public JSONObject createBillJson() {
        List<BillSyncEntity> billDataList =commonRepository.getBillDataWithStatus("0");
        JSONObject syncDataObject =new JSONObject();
        JSONArray syncDataJSONArray = new JSONArray();

        String productDescriptionKey = "product_description_id";
        String productDescIds = "";

        try {
            for(int i=0;i<billDataList.size();i++){
                JSONObject billObject =new JSONObject();
                productDescIds =billDataList.get(i).getProduct_desc_Id();
                billObject.accumulate("mela_id", AppConstant.MELA_ID);
                billObject.accumulate("bill_id","1" );
                billObject.accumulate("invoice_no",billDataList.get(i).getInvoice_number());
                billObject.accumulate("payment_mode", "Cash/Card/Pos");
                billObject.accumulate("shg_reg_id",billDataList.get(i).getShg_reg_id());//billDataList.get(i).getShg_reg_id()// appSharedPreferences.getShgRegId()
                billObject.accumulate("bill_no", "");//*****
                billObject.accumulate("generated_date",billDataList.get(i).getBill_generated_date());
                billObject.accumulate("created_date", billDataList.get(i).getBill_created_date());
                billObject.accumulate("bill_time", billDataList.get(i).getBill_generated_time());
                billObject.accumulate(productDescriptionKey, productDescIds);
                billObject.accumulate("product_id",billDataList.get(i).getProduct_Id());
                billObject.accumulate("product_quantity",billDataList.get(i).getProduct_Qty());
                billObject.accumulate("unit_price", billDataList.get(i).getUnit_Price());
                billObject.accumulate("mobile_no", billDataList.get(i).getLogin_mobile_no());
                syncDataJSONArray.put(billObject);

            }

            String syncDataJSONArrayString = syncDataJSONArray.toString();
            syncDataObject.accumulate("BILLOFFLINEDATA", syncDataJSONArrayString);
            syncDataObject.accumulate("melaId",AppConstant.MELA_ID );
            syncDataObject.accumulate("mobile",appSharedPreferences.getMobile().equalsIgnoreCase("")? billDataList.get(0).getLogin_mobile_no():appSharedPreferences.getMobile());
            syncDataObject.accumulate("imei_no",appSharedPreferences.getImei().equalsIgnoreCase("")? billDataList.get(0).getImei_number():appSharedPreferences.getImei());
            syncDataObject.accumulate("device_name",appSharedPreferences.getDeviceInfo().equalsIgnoreCase("")? billDataList.get(0).getDevice_info():appSharedPreferences.getDeviceInfo() );
            syncDataObject.accumulate("location_coordinate","00.00");

        } catch (JSONException e) {
            e.printStackTrace();
            appUtility.showLog("bill create JSOn Exception:- "+e, DashBoardFragment.class);
        }
        return syncDataObject;
    }

    public void flagStatusUpdate( String getData) {
        List<BillSyncEntity> billDataList =commonRepository.getBillDataWithStatus("0");

        appUtility.showLog("get Sync Detail:-"+getData,DashBoardFragment.class);
        appUtility.showLog("get bill Sync list size:-"+billDataList.size(),DashBoardFragment.class);

        for(int i=0;i<billDataList.size();i++){
            String invoiceNumber =billDataList.get(i).getInvoice_number();
            commonRepository.updateStatusFlag("1",invoiceNumber);
        }

    }

    public void createUnsyncDataFile(String textData){
        if(!filesUtils.isFolderExist(AppConstant.myAppDir)){
            filesUtils.createNewFolder(AppConstant.myAppDir);
        }else {
            String fileName = AppConstant.myAppDir+AppConstant.commoUnsyncFile;
            filesUtils.createBilltextFile(fileName,commonRepository.getUnsyncBillJson().toString());
           /* if(!filesUtils.isFileExist(AppConstant.commoUnsyncFile)){

            }else {
                String fileName = AppConstant.myAppDir+AppConstant.commoUnsyncFile;
                filesUtils.createBilltextFile(fileName,);
            }*/
        }
    }

    public void insetFiledataInDb(JSONObject response){

        try {
            JSONArray billArray =response.getJSONArray("data");
            for(int i=0;i<billArray.length();i++){
                JSONObject billObject =billArray.getJSONObject(i);
                BillSyncEntity billSyncEntity =new BillSyncEntity();

                billSyncEntity.setInvoice_number(billObject.getString("invoice_number"));
                billSyncEntity.setBill_generated_date(billObject.getString("bill_generated_date"));
                billSyncEntity.setBill_generated_time(billObject.getString("bill_generated_time"));
                billSyncEntity.setBill_created_date(billObject.getString("bill_created_date"));
                billSyncEntity.setShg_reg_id(billObject.getString("shg_reg_id"));
                billSyncEntity.setShg_code(billObject.getString("shg_code"));
                billSyncEntity.setProduct_Qty(billObject.getString("product_Qty"));
                billSyncEntity.setProduct_Id(billObject.getString("product_Id"));
                billSyncEntity.setMela_Id(billObject.getString("mela_Id"));
                billSyncEntity.setUnit_Price(billObject.getString("unit_Price"));
                billSyncEntity.setStatus_flag(billObject.getString("status_flag"));
                billSyncEntity.setProduct_desc_Id(billObject.getString("product_desc_Id"));
                billSyncEntity.setLogin_mobile_no(billObject.getString("login_mobile_no"));
                billSyncEntity.setUnique_bill_number(billObject.getString("unique_bill_number"));
                billSyncEntity.setTotal_amount(Integer.parseInt(billObject.getString("total_amount")));
                billSyncEntity.setGenerated_bill_no(Integer.parseInt(billObject.getString("generated_bill_no")));
                saveBillSyncData(billSyncEntity);
            }
            /****write in file with empty {}******/
            String fileName = AppConstant.myAppDir+AppConstant.commoUnsyncFile;
            filesUtils.createBilltextFile(fileName,"{}");
        } catch (JSONException e) {
            e.printStackTrace();
            appUtility.showLog("Insert JSON Expection::- "+e,CommonRepository.class);
        }
    }

    public String getUniqueBill(){
        String uniqueBill = "";
        List<BillSyncEntity> list = AppDataBase.getDatabase(mContext).billSyncDao().getAllFlagBillData("0");
        for(int i=0;i<list.size();i++){
            uniqueBill = list.get(i).getInvoice_number()+",";
        }
        return  appUtility.removeCommaFromLast(uniqueBill);
    }

}
