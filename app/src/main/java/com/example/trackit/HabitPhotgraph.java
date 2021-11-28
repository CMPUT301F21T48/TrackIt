package com.example.trackit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class HabitPhotgraph extends Activity {

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

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                }
//                else
//                {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                }
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, 1);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}
