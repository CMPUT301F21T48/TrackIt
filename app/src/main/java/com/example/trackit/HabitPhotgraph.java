package com.example.trackit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class HabitPhotgraph {

    private Bitmap photo;
    private String encodedPhoto;


    public Bitmap getPhoto() {
        decodePhoto();
        return photo;
    }


    public void decodePhoto() {
        if (this.encodedPhoto != null) {
            byte [] decodedBytes = Base64.decode(this.encodedPhoto, 0);
            this.photo = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    public void setPhoto(Bitmap photo) {

        if (photo != null) {


            if (photo.getByteCount() > 65536) {
                for (int i = 0; i < 3; ++i) {
                    photo = resizeImage(photo);
                    if (photo.getByteCount() <= 65536) {
                        break;
                    }
                }
            }

            if (photo.getByteCount() <= 65536) {

                this.photo = photo;

                ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
                this.encodedPhoto = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

            } else {
                throw new IllegalArgumentException("Image file must be less than or equal to " +
                        String.valueOf(65536) + " bytes.");
            }

        } else {
            this.photo = null;
            this.encodedPhoto = null;
        }
    }
    private Bitmap resizeImage(Bitmap img) {
        double scaleFactor = 0.95;
        return Bitmap.createScaledBitmap(img, (int) (img.getWidth() * scaleFactor), (int) (img.getHeight() * scaleFactor), true);
    }

    public void deletePhoto() {
        this.photo = null;
        this.encodedPhoto = null;
    }
}
