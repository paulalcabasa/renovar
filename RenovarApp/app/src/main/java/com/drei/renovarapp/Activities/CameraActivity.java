package com.drei.renovarapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.camerakit.type.CameraFacing;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity {
    private CameraKitView cameraKitView;
    private FloatingActionButton fabCapture,fabFacing;
    private TextView txtStatus, txtMessage;
    private boolean isAfter = false;
    private String PATH_BEFORE, PATH_AFTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        cameraKitView = findViewById(R.id.camera);
        fabCapture = findViewById(R.id.fabCapture);
        txtStatus = findViewById(R.id.txtStatus);
        txtMessage = findViewById(R.id.txtMessage);
        fabFacing = findViewById(R.id.fabFacing);

        cameraKitView.setFacing(CameraKit.FACING_FRONT);

        fabFacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraKitView.getFacing() == CameraKit.FACING_FRONT)
                {
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
                }
                else
                {
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                }
            }
        });

        TherapyDB therapyDB = TherapyDB.getInstance(this);
        therapyDB.open();
        therapyDB.getTherapyList(String.valueOf(getIntent().getIntExtra("id", 0)));
        txtMessage.setText(therapyDB.getTherapyList(String.valueOf(getIntent().getIntExtra("id", 0))).get(0).getMessage());
        therapyDB.close();

        fabCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] capturedImage) {
                        long time_millis = System.currentTimeMillis();

                        File dir = new File(Environment.getExternalStorageDirectory() + "/Renovar");
                        if (!dir.exists()) {
                            try {
                                if (dir.mkdir()) {
                                    System.out.println("Directory created");
                                } else {
                                    System.out.println("Directory is not created");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            File savedPhoto = new File(dir, time_millis + getExtension(isAfter) + ".jpg");
                            try {
                                FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                                outputStream.write(capturedImage);
                                outputStream.close();
                                txtStatus.setText("AFTER THERAPY");
                                if (!isAfter) {
                                    PATH_BEFORE = savedPhoto.getPath();
                                }

                                if (isAfter) {
                                    PATH_AFTER = savedPhoto.getPath();
                                    ResultDB resultDB = ResultDB.getInstance(CameraActivity.this);
                                    resultDB.setPath_before(PATH_BEFORE);
                                    resultDB.setPath_after(PATH_AFTER);
                                    resultDB.setId(getIntent().getIntExtra("id", 0));
                                    resultDB.setTime_millis(time_millis);

                                    resultDB.open();
                                    resultDB.insertRecord();
                                    resultDB.close();

                                    Toast.makeText(CameraActivity.this, "Result Record Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                YoYo.with(Techniques.ZoomIn).duration(1000).playOn(txtStatus);
                                isAfter = true;
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });

    }

    public String getExtension(boolean isAfter) {
        if (isAfter) {
            return "_after";
        } else {
            return "_before";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
