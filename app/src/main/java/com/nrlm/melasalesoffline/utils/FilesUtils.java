package com.nrlm.melasalesoffline.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FilesUtils {
    Context context;
    AppUtility appUtility;
    AppSharedPreferences appSharedPreferences;


    public static FilesUtils filesUtils = null;
    public static FilesUtils getInstance(Context context) {
        if (filesUtils == null)
            filesUtils = new FilesUtils(context);
        return filesUtils;
    }

    public FilesUtils(Context context) {
        this.context = context;
        appUtility =AppUtility.getInstance();
        appSharedPreferences =AppSharedPreferences.getsharedprefInstances(context);
    }


    /**********check folder ********/
    public boolean isFolderExist(String folderName){
        boolean folderExist =false;
        File newFile = new File(folderName);
        if(newFile.exists()){
            folderExist =true;
        }
        return folderExist;
    }

    public void createNewFolder(String folderName){
        File file = new File(folderName);
        if (!file.exists()) {
           boolean b= file.mkdirs();
            Toast.makeText(context,""+b,Toast.LENGTH_LONG).show();
        }

    }

    /************check files*************/
    public boolean isFileExist(String fileName){
        boolean isFileExist=false;
        File newFile = new File( fileName);
        if(newFile.exists()){
            isFileExist = true;

        }
        return isFileExist;
    }

    /*********create text file in folder*************/
    public void createBilltextFile(String fileName , String fileValue){
        File file = new File(fileName);
        if(file.exists()){
            try  {
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                outputWriter.write(String.valueOf(fileValue));
                outputWriter.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                file.createNewFile();
                file.getAbsoluteFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                outputWriter.write(String.valueOf(fileValue));
                outputWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void createTextFile(String folderName,String fileName,String fileValue){
        File parentFolder = new File(folderName);
        if(parentFolder.exists()){
            File newFile = new File(parentFolder, fileName);
            if(newFile.exists()){
                try  {
                    FileOutputStream fOut = new FileOutputStream(newFile);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                    outputWriter.write(String.valueOf(fileValue));
                    outputWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                try {
                    newFile.createNewFile();
                    newFile.getAbsoluteFile();
                    FileOutputStream fOut = new FileOutputStream(newFile);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                    outputWriter.write(String.valueOf(fileValue));
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //folder name with complet absolute path
  /*  public void createFile(String folderName,String fileName, String overWriteValue){

        File imagesFolder = new File(folderName);
        if(!imagesFolder.exists()){
            imagesFolder.mkdirs();
        }else {

            File newFile = new File(imagesFolder, fileName);
            if(!newFile.exists()){
                try {
                    newFile.createNewFile();
                    newFile.getAbsoluteFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try  {
                    FileOutputStream fOut = new FileOutputStream(newFile);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                    outputWriter.write(String.valueOf(overWriteValue));
                    outputWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                try  {
                    FileOutputStream fOut = new FileOutputStream(newFile);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
                    outputWriter.write(String.valueOf(overWriteValue));
                    outputWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }*/

    public String getPath(String folderName){
        String path =AppConstant.myAppDir+folderName;
        return path;
    }


    public boolean createFolder(String dirPath){
        boolean ispathExist=false;
        File newFile = new File(dirPath);
        if(newFile.exists()){
            ispathExist = true;
        }else {
            newFile.mkdir();
            ispathExist = true;
        }
        return ispathExist;
    }


    public String read_file(String fileName ) {

        File newFile = new File(fileName);
        if(newFile.exists()){
            try {
                FileInputStream fis1 = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis1, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
                return "";
            } catch (UnsupportedEncodingException e) {
                return "";
            } catch (IOException e) {
                return "";
            }
        }
        else {
            return "No Backup Found.";
        }
    }

}
