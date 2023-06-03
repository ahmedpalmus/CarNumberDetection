package com.example.carnumberdetection;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;


public class PlateDetectionActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private static final String TAG = "PlateDetectionActivity";
    Button mTextButton, save;
    Uri imageCollection;
    ContentValues contentValues;
    String username;
    private Bitmap mBitmap;
    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_detection);
        username = getIntent().getStringExtra("id");

        mImageView = findViewById(R.id.image_view);
        mTextView = findViewById(R.id.text_view);
        mTextButton = findViewById(R.id.button_text);
        save = findViewById(R.id.button_save);


/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "car_image.jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);*/

        mTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTextRecognition();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(400);
            }
        });

/*        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                    Uri uri = getContentResolver().insert(imageCollection, contentValues);
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    boolean err = true;
                    String result = "";
                    if (!mBitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        result = "Couldn't save bitmap";
                    } else {
                        result = "Image Saved";
                        err = false;
                    }
                    Toast.makeText(PlateDetectionActivity.this, result, Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });*/
/*        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        } else {
            dispatchTakePictureIntent();
        }*/


    }

    private void showFileChooser(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PlateDetectionActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(PlateDetectionActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(PlateDetectionActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mBitmap);

        } else if (requestCode == 400 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedimg = data.getData();
            try {
                mImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                mBitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void runTextRecognition() {
        // Replace with code from the codelab to run text recognition.
        InputImage image = InputImage.fromBitmap(mBitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient();
        mTextButton.setEnabled(false);
        save.setEnabled(false);

        recognizer.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text texts) {
                                mTextButton.setEnabled(true);
                                save.setEnabled(true);
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                mTextButton.setEnabled(true);
                                save.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(Text texts) {
        // Replace with code from the codelab to process the text recognition result.
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            return;
        }
        String text = "";

/*        for (int i = 0; i < blocks.size(); i++) {
            List<Text.Line> lines = blocks.get(i).getLines();
            List<Text.Element> elements0 = lines.get(0).getElements();
            List<Text.Element> elements1 = lines.get(1).getElements();
            List<Text.Element> elements2 = lines.get(2).getElements();

            for (int j = 0; j < lines.size(); j++) {
                List<Text.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    text +="block: "+i+"line: "+j+" "+ elements.get(k).getText();
                }
            }
        }*/

        for (int i = 0; i < blocks.size(); i++) {
            List<Text.Line> lines = blocks.get(i).getLines();
            if (lines.size() == 1) {
                List<Text.Element> elements = lines.get(0).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    text += "line: 1 " + elements.get(k).getText();
                }
            } else if (lines.size() >= 2) {
                List<Text.Element> elements1 = lines.get(0).getElements();
                List<Text.Element> elements2 = lines.get(1).getElements();
                if(elements2.size()>elements1.size()){
                    for (int k = 0; k < elements2.size(); k++) {
                        text += elements2.get(k).getText();
                    }
                }else{
                    for (int k = 0; k < elements1.size(); k++) {
                        text += elements1.get(k).getText();
                    }
                }

            }

        }
        String filteredText = text.replaceAll("[^a-zA-Z0-9]", "");
        if (filteredText.length() > 7)
            filteredText = filteredText.substring(0, 7);
        mTextView.setText(text);
        Intent intent = new Intent(PlateDetectionActivity.this, NewReport.class);
        intent.putExtra("number", filteredText);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}