package com.mshoaibnuman.imagecropping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agsw.FabricView.FabricView;

public class FabricActivity extends AppCompatActivity {

    FabricView fabricView;
    Button btn;
    Bitmap largeIcon;
    RelativeLayout rlAddText;
    TextView myTextView;
    private int _xDelta;
    private int _yDelta;
    ViewGroup _root;

    ImageView imgScreenshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabric);


        rlAddText = (RelativeLayout) findViewById(R.id.rlAddText);

        imgScreenshot = (ImageView) findViewById(R.id.imgScreenshot);


        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imgScreenshot.setImageBitmap(bmp);



    }
}
