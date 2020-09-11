package com.example.framework.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileName : FileHelper
 * Founder  : jyt
 * Create Date : 2020/9/8 11:27 AM
 * Profile :
 */
public class FileHelper {
    private static volatile FileHelper mInstance = null;
    private SimpleDateFormat simpleDateFormat ;
    public static final int CAMERA_REQUEST_CODE = 1004;

    public static final int ALBUM_REQUEST_CODE = 1005;

    private FileHelper() {

    }

    public static FileHelper getInstance() {
        if (mInstance == null) {

            synchronized (FileHelper.class) {
                if (mInstance == null) {

                    mInstance = new FileHelper();
                }
            }

        }
        return mInstance;
    }

    private File tempFile = null;
    private Uri imageUri;

    public File getTempFile() {
        return tempFile;
    }

    //相机
//    public void toCamera(Activity mActivity){
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        String fileName = simpleDateFormat.format(new Date());
//        tempFile = new File(Environment.getExternalStorageDirectory()+fileName+".jpg");
//
// //       if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
//            imageUri = Uri.fromFile(tempFile);
////        }else{
////            //利用FileProvider
////           imageUri =  FileProvider.getUriForFile(mActivity,mActivity.getPackageName()+".fileprovider",tempFile);
////            Log.d("filename", imageUri.getPath());
////            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|
////                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//            mActivity.startActivityForResult(intent,CAMERA_REQUEST_CODE);
//
//        }
    public void toCamera(Activity mActivity) {
      //  try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String fileName = simpleDateFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(), fileName + ".jpg");
            //兼容Android N
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(tempFile);
            } else {
                //利用FileProvider
                imageUri = FileProvider.getUriForFile(mActivity,
                        mActivity.getPackageName() + ".fileprovider", tempFile);
                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
//        } catch (Exception e) {
//            Toast.makeText(mActivity, "无法打开相机", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//       }
    }

    public void toAlbum(Activity mActivity){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent,ALBUM_REQUEST_CODE);

    }



    public String getRealPathFromURI(Context mcontext, Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader =  new CursorLoader(mcontext,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();

        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }





}
