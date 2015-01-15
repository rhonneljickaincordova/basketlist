package com.example.baskestlist.app.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import com.example.baskestlist.app.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Kevin on 1/14/2015.
 */
public class ImageGrabHelper {

    private String srcImage;
    private Context context;

    /**
     * Grabs &lt;?&gt; file from specified URL
     *
     * @param url
     */
    public ImageGrabHelper(Context context, String url) {
        this.srcImage = url;
        this.context = context;
    }

    public Bitmap grabImage() {
        int count = 0;
        String dlDir = context.getCacheDir() + "/images";
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String urlImage = this.srcImage.replace(" ", "%20");
            String filename = urlImage.substring(urlImage.lastIndexOf('/'), urlImage.length());
            URL url = new URL(urlImage);
            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream in = new BufferedInputStream(url.openStream());

            File imageDir = new File(dlDir);
            imageDir.mkdirs();

            OutputStream out = new FileOutputStream(dlDir + "/" + filename);
            byte[] data = new byte[1024];
            while ((count = in.read(data)) != -1) {
                out.write(data, 0, count);
            }
            out.flush();
            out.close();
            in.close();

            Bitmap bmp = BitmapFactory.decodeFile(dlDir + "/" + filename);
            if (bmp != null) {
                return bmp;
            } else {
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.sad_cloud);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
