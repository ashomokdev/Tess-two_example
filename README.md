# Tess-two_example
tess-two usage example. This Android project uses Tesseract for performing OCR. 
[apk in Google Play Market](https://play.google.com/store/apps/details?id=com.ashomok.tesseractsample)
![alt tag](http://s32.postimg.org/dzcyc1fet/image.png)


##Usefull info
#####What I need to start use Tesseract classes in my Android project:
add to ```build.gradle```:

```
dependencies {
    compile 'com.rmtheis:tess-two:5.4.1'
}
```

That's all!

#####Why this example working only with images, captured in landscape mode? And not working if captured image in portrait mode?

This code does not contain the setting correct orientation to Bitmap. Try to add:
```
try {
    ExifInterface exif = new ExifInterface(_path);
    int exifOrientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL);

    Log.v(TAG, "Orient: " + exifOrientation);

    int rotate = 0;

    switch (exifOrientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
            rotate = 90;
            break;
        case ExifInterface.ORIENTATION_ROTATE_180:
            rotate = 180;
            break;
        case ExifInterface.ORIENTATION_ROTATE_270:
            rotate = 270;
            break;
    }

    Log.v(TAG, "Rotation: " + rotate);

    if (rotate != 0) {

        // Getting width & height of the given image.
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting pre rotate
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);

        // Rotating Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
    }
}
```

#####Why I see black screen when OCR procesing?
You need to do OCR in extra thread. Implementation of this is out off topic. Reed about AsyncTask in Android.
