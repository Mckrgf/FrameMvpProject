package com.yaobing.module_middleware.Utils;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.yaobing.module_middleware.BaseApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by guojun on 2018/1/19.
 */
public class WriteAllData {
    public static boolean writeStringToFile(String path, String fileName, String message)
            throws IOException {
        boolean result;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            File realFile = new File(path + "/" + fileName);
//            realFile.createNewFile();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(realFile),encoding));
//            bufferedWriter.write(message);
//            bufferedWriter.close();
            realFile.createNewFile();
            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(realFile);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(message.getBytes());
            // 最后关闭文件输出流
            outStream.close();

            updateGallery(path + "/" + fileName);

            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    public static void updateGallery(String filename)//filename是我们的文件全名，包括后缀哦
    {
        MediaScannerConnection.scanFile(BaseApp.getInstance().getApplicationContext(),
                new String[] { filename }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

}
