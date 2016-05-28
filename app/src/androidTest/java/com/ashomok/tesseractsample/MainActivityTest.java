package com.ashomok.tesseractsample;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public static final String TEST_IMGS = "test_imgs";
    private static final String TAG = MainActivityTest.class.getSimpleName();

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Tess-two_example/";

    public MainActivityTest() {
        super(MainActivity.class);
    }


    public void testOCR() {

        try {
            ArrayList<String> fileList = getTestImages();

            for (String fileName : fileList) {

                getActivity().outputFileUri = Uri.parse(fileName);

                getActivity().onActivityResult(getActivity().PHOTO_REQUEST_CODE, Activity.RESULT_OK, null);

                Log.d(TAG, getActivity().result);
            }

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private ArrayList<String> getTestImages() {
        //create folders for tessdata files
        prepareDirectories(
                new String[]{DATA_PATH + TEST_IMGS});

        ArrayList<String> files = new ArrayList<String>();

        try {
            AssetManager assetManager = getInstrumentation().getContext().getAssets();
            String fileList[] = assetManager.list(TEST_IMGS);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + TEST_IMGS + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = assetManager.open(TEST_IMGS + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.v(TAG, "Copied " + fileName + "to test_imgs");
                }
                files.add(pathToDataFile);
            }
        } catch (IOException e) {
            Log.e(TAG, "Was unable to copy files to test_imgs " + e.toString());

        }
        return files;
    }

    private void prepareDirectories(String[] paths) {
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path
                            + " on sdcard failed");
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }
    }
}