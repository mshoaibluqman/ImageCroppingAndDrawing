package com.mshoaibnuman.imagecropping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddTextActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ImageView imgCropped1;
    Button btnAddText, btnDone;
    RelativeLayout layBg;
    LinearLayout linearLayoutForDrawing;
    RelativeLayout mDrawingPad;
    TextView myTextView;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        imgCropped1 = (ImageView) findViewById(R.id.imgCropped1);
        btnAddText = (Button) findViewById(R.id.btnAddText);
        btnDone = (Button) findViewById(R.id.btnDone);
        layBg = (RelativeLayout) findViewById(R.id.laybg);
        linearLayoutForDrawing = (LinearLayout) findViewById(R.id.linearLayoutForDrawing);
        mDrawingPad = (RelativeLayout) findViewById(R.id.relativeLayoutForDrwaing);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("img_Url");
        Uri resultUri = Uri.parse(uri);
      imgCropped1.setImageURI(resultUri);

      btnAddText.setOnClickListener(this);
      btnDone.setOnClickListener(this);
        DrawingView mDrawingView=new DrawingView(this);


        mDrawingPad.addView(mDrawingView);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnAddText:

                final EditText myEditText = new EditText(getApplicationContext()); // Pass it an Activity or Context
                myEditText.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                myEditText.setHint("Add Text Here.....");
                myEditText.setSingleLine();

                myEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                mDrawingPad.addView(myEditText);

                myEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            Log.i("Keyboard pressed", "Enter pressed");

                            InputMethodManager inputManager =
                                    (InputMethodManager) getApplicationContext().
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(
                                    AddTextActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                            myTextView = new TextView(getApplicationContext());
                            myTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                            myTextView.setTextSize(18);
                            myTextView.setPadding(20, 35, 20, 20);
                            myTextView.setText(myEditText.getText().toString());

                            mDrawingPad.addView(myTextView);

                            myTextView.setOnTouchListener(AddTextActivity.this);

                            myEditText.setVisibility(View.GONE);

                        }
                        return true;
                    }
                });

                break;

            case R.id.btnDone:

                View z = (RelativeLayout) findViewById(R.id.relativeLayoutForDrwaing);
                z.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(z.getDrawingCache());

//                bitmap = rotateBitmap(bitmap);
//
//                int fromHere = (int) (bitmap.getHeight() * 0.2);
//
//                Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, (int) (bitmap.getHeight() * 0.8), bitmap.getWidth(), fromHere);
//
//                bitmap = rotateBitmap(croppedBitmap);

                Intent intent1 = new Intent(AddTextActivity.this, FabricActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                intent1.putExtra("bitmap", byteArray);

                startActivity(intent1);
                
                break;

                default:

                    break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        mDrawingPad.invalidate();
        return true;
    }

    private Bitmap rotateBitmap(Bitmap bitmap){


// or just load a resource from the res/drawable directory:
        Bitmap myBitmap = bitmap;

// find the width and height of the screen:
        Display d = getWindowManager().getDefaultDisplay();
        int x = d.getWidth();
        int y = d.getHeight();


// scale it to fit the screen, x and y swapped because my image is wider than it is tall
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap, y, x, true);

// create a matrix object
        Matrix matrix = new Matrix();
        matrix.postRotate(180); // anti-clockwise by 90 degrees

// create a new bitmap from the original using the matrix to transform the result
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);


        return rotatedBitmap;
    }
}
