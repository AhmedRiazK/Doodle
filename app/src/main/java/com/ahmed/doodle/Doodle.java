package com.ahmed.doodle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

import com.ahmed.doodle.R;

public class Doodle extends AppCompatActivity {
    RelativeLayout relativeLayout, popUpWinLayout, progressLayout;
    PopupWindow popupWindow, progress;
    RelativeLayout.LayoutParams layoutParamse, layoutParamsp, layoutParamsh, layoutParamsi, layoutParamst, poplp, progresslp, backlp, doodlinglp;
    ImageButton eraser;
    DisplayMetrics metrics = new DisplayMetrics();
    ImageButton pencil;
    ImageButton home;
    ImageButton image;
    ImageButton text, back;
    ImageButton pop;
    ImageView imageView;
    Bitmap map;
    String receivedData = null;
    TextView doodling;
    Matrix matrix,savedMatrix;
    PointF start = new PointF(), starta = new PointF();
    Point size;
    Paint dot;
    Display display;
    Resources resources;
    Layout layout;
    Canvas store;
    Path path,npath;
    ArrayList<ImageView> imageViews;
    ArrayList<EditText> txt;
    ArrayList<Integer> arrayListX,arrayListY,idt;
    ArrayList<String> pathData;
    ArrayList<RelativeLayout.LayoutParams> layoutParamstxt;
    ArrayList<Float>  cx,cy;
    ProgressBar progressBar;
    Intent intent;
    int count;
    final int handlerState = 0;
    Handler bluetoothIn;
    private StringBuilder recDataString = new StringBuilder();
    private static final String TAG = "bluetooth2";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String address;
    public boolean receiveData = false, btState;
    int num = 0;
    float mx,my;
    float x,y,pad;
    boolean draw = false, popW = false;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            activityResult -> {
                if(activityResult.getResultCode() != RESULT_OK)
                {
//                        Intent data = activityResult.getData();
//                        data.
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN;
        v.setSystemUiVisibility(ui);
        relativeLayout = new RelativeLayout(this);
        popUpWinLayout = new RelativeLayout(this);
        layout = new Layout(this);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        display.getRealSize(size);
        path = new Path();
        npath = new Path();
        layoutParamst = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamse = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsh = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsi = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        poplp =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        backlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pop = new ImageButton(Doodle.this);
        eraser = new ImageButton(Doodle.this);
        pencil = new ImageButton(Doodle.this);
        image = new ImageButton(Doodle.this);
        home = new ImageButton(Doodle.this);
        text = new ImageButton(Doodle.this);
        back = new ImageButton(Doodle.this);
        pop.setImageResource(R.mipmap.drop);
        pop.setBackground(Drawable.createFromPath("@null"));
        int eraserid = View.generateViewId();
        int pencilid = View.generateViewId();
        int homeid = View.generateViewId();
        int imageid = View.generateViewId();
        int textid = View.generateViewId();
        int popid = View.generateViewId();
        int backid = View.generateViewId();
        pop.setId(popid);
        poplp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        poplp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        pop.setLayoutParams(poplp);
        eraser.setId(eraserid);
        layoutParamse.addRule(RelativeLayout.RIGHT_OF,popid);
        eraser.setLayoutParams(layoutParamse);
        layoutParamsp.addRule(RelativeLayout.RIGHT_OF,eraserid);
        pencil.setId(pencilid);
        pencil.setLayoutParams(layoutParamsp);
        layoutParamsh.addRule(RelativeLayout.RIGHT_OF,pencilid);
        home.setId(homeid);
        home.setLayoutParams(layoutParamsh);
        layoutParamsi.addRule(RelativeLayout.RIGHT_OF,homeid);
        image.setId(imageid);
        image.setLayoutParams(layoutParamsi);
        layoutParamst.addRule(RelativeLayout.RIGHT_OF,imageid);
        back.setId(backid);
        backlp.addRule(RelativeLayout.BELOW,eraserid);
        backlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        back.setLayoutParams(backlp);
        text.setId(textid);
        text.setLayoutParams(layoutParamst);
        resources = getResources();
        store = new Canvas();
        cx = new ArrayList<Float>();
        cy = new ArrayList<Float>();
        imageViews = new ArrayList<ImageView>();
        arrayListX = new ArrayList<Integer>();
        arrayListY = new ArrayList<Integer>();
        pathData = new ArrayList<String>();
        txt = new ArrayList<EditText>();
        idt = new ArrayList<Integer>();
        layoutParamstxt = new ArrayList<RelativeLayout.LayoutParams>();
        pad = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,resources.getDisplayMetrics());
        dot = new Paint();
        dot.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        dot.setStrokeWidth(10);
        dot.setStyle(Paint.Style.STROKE);
        dot.setStrokeJoin(Paint.Join.ROUND);
        dot.setStrokeCap(Paint.Cap.ROUND);
        dot.setAntiAlias(true);
        eraser.setPadding((int)pad,(int)pad,(int)pad,(int)pad);
        pencil.setPadding((int)pad,(int)pad,(int)pad,(int)pad);
        home.setPadding((int)pad,(int)pad,(int)pad,(int)pad);
        image.setPadding((int)pad,(int)pad,(int)pad,(int)pad);
        text.setPadding((int)pad,(int)pad,(int)pad,(int)pad);
        pop.setPadding(1,1,1,1);
        relativeLayout.addView(pop);
        relativeLayout.addView(layout);
        setContentView(relativeLayout);
    }

    public void popUpWindowDismiss(){
        popUpWinLayout.removeView(text);
        popUpWinLayout.removeView(pencil);
        popUpWinLayout.removeView(home);
        popUpWinLayout.removeView(eraser);
        popUpWinLayout.removeView(image);
        popUpWinLayout.removeView(back);
        popupWindow.dismiss();
    }

    public final int handleBtPermission (String permission)
    {
        int result = ActivityCompat.checkSelfPermission(Doodle.this, permission);
        switch (permission)
        {
            case Manifest.permission.BLUETOOTH_CONNECT:
            {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT > 31)
                    {
                        ActivityCompat.requestPermissions(Doodle.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 0);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(Doodle.this, new String[]{Manifest.permission.BLUETOOTH}, 0);
                    }
                }
            }
            case Manifest.permission.BLUETOOTH_SCAN:
            {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT > 31)
                    {
                        ActivityCompat.requestPermissions(Doodle.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 0);
                    }
                }
            }
        }
        return result;
    }

    public class Layout extends View{

        public Layout(Context context) {

            super(context);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onDraw(@NonNull Canvas canvas){
            super.onDraw(canvas);
            if(txt.size() != 0){
                txt.get(txt.size()-1).clearFocus();
            }

            if(draw) {
                canvas.drawPath(path, dot);
            }
            relativeLayout.setOnTouchListener((v, event) -> {
                x = event.getRawX();
                y = event.getRawY();
                draw = true;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    path.moveTo(x,y);
                    path.lineTo(x,y);
                    x = event.getRawX();
                    y = event.getRawY();
                    arrayListX.add((int) x);
                    arrayListY.add((int)y);
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    path.lineTo(x,y);
                    x = event.getRawX();
                    y = event.getRawY();
                    arrayListX.add((int) x);
                    arrayListY.add((int)y);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    x = event.getRawX();
                    y = event.getRawY();
                    arrayListX.add((int) x);
                    arrayListY.add((int)y);
                }
                invalidate();
                return true;
            });

            eraser.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    eraser.setImageResource(R.mipmap.eraser_a);
                    path.reset();
                    arrayListX.clear();
                    invalidate();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    popUpWindowDismiss();
                    popW = false;
                    eraser.setImageResource(R.mipmap.eraser_b);
                }
                return false;
            });
            pencil.setOnTouchListener(new OnTouchListener() {
                @SuppressLint("MissingPermission")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        pencil.setImageResource(R.mipmap.pencil_a);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        btAdapter= BluetoothAdapter.getDefaultAdapter();
                        if(btAdapter.isEnabled()){
                            if (handleBtPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                                @SuppressLint("MissingPermission")
                                Set<BluetoothDevice> deviceset = btAdapter.getBondedDevices();
                                if (deviceset.size() > 0) {
                                    for (BluetoothDevice device : deviceset) {
                                        Log.d("BT Device name",device.getName());// TODO device validation

                                        if(device.getName().compareTo("Doodle") == 0){
                                            Log.d("BT Device address",device.getAddress());
                                            address = device.getAddress();
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            if (handleBtPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                                activityResultLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                            }
                        }
                        if(btAdapter.isEnabled()){
                            if (handleBtPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                                connectToBT();
                                Log.d("BT Device address", address);
                                if (btState) {
                                    TransferData transferData = new TransferData();
                                    transferData.execute();
                                } else {
                                    Toast.makeText(Doodle.this, "Unable to connect to Doodle", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        popUpWindowDismiss();
                        popW = false;
                        pencil.setImageResource(R.mipmap.pencil_b);
                    }
                    return false;
                }
            });
            text.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    text.setImageResource(R.mipmap.text_a);
                    EditText text = new EditText(Doodle.this);
                    txt.add(text);
                    txt.get(txt.size()-1).setBackground(Drawable.createFromPath("@null"));
                    txt.get(txt.size()-1).setTextColor(Color.WHITE);
                    txt.get(txt.size()-1).setText("hello");
                    txt.get(txt.size()-1).setSelection(txt.get(txt.size()-1).length());
                    txt.get(txt.size()-1).requestFocus();
                    relativeLayout.addView(txt.get(txt.size()-1));
                    txt.get(txt.size()-1).setOnTouchListener(new OnTouchListener() {
                        int tap = 0; boolean moved = false;
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(event.getAction() == MotionEvent.ACTION_DOWN){
                                start.set(event.getX(),event.getY());
                                moved = false;
                            }
                            if(event.getAction() == MotionEvent.ACTION_MOVE){
                                tap = 0;
                                moved = true;
                                v.setX(event.getRawX()-start.x);
                                v.setY(event.getRawY()-start.y);
                                tap = 0;
                            }
                            if(event.getAction() == MotionEvent.ACTION_UP){
                                if(start.x == event.getX() && start.y == event.getY()){
                                    if(!moved){
                                        tap++;
                                        if(tap == 1){
                                            v.requestFocus();
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                                        }
                                        if(tap == 2){
                                            relativeLayout.removeView(v);
                                        }
                                    }
                                }
                            }
                            return true;
                        }
                    });
                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    popUpWindowDismiss();
                    popW = false;
                    text.setImageResource(R.mipmap.text_b);
                }
                return true;
            });
            home.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    home.setImageResource(R.mipmap.home_a);
                    String s = "SketchPath", pix = "pix";
                    //File mediaStorageDir = Doodle.this.getExternalFilesDir(null);
                    File mediaStorageDir = new File(Doodle.this.getExternalFilesDir(null),"Doodle");
                    if(!mediaStorageDir.exists()){
                        Toast.makeText(Doodle.this, "File Not found", Toast.LENGTH_SHORT).show();
                        boolean ret = mediaStorageDir.mkdir();
                        Toast.makeText(Doodle.this, "File Not found "+ret, Toast.LENGTH_SHORT).show();
                    }
                    File filepathx = new File(mediaStorageDir,s+".txt");
                    File filepathp = new File(mediaStorageDir,pix+".txt");
                  //  Toast.makeText(Doodle.this,""+size.x+"-"+""+size.y,Toast.LENGTH_SHORT).show();
                    try {
                        FileWriter writerx = new FileWriter(filepathx);
                        FileWriter writerp = new FileWriter(filepathp);
                        for(int i = 0; i < arrayListX.size(); i++ ){
                            writerx.append("x");
                            float resX = arrayListX.get(i)/(float)size.x;
                            float paperX = resX * 295;
                            int xSteps = (int) paperX;
                            writerp.append("x"+arrayListX.get(i));
                            writerx.append(""+xSteps);
                            writerx.append("y");
                            float resY = arrayListY.get(i)/(float)size.y;
                            float paperY = resY * 415 ;
                            int ySteps = (int) paperY;
                         writerp.append("y"+arrayListY.get(i));
                            writerx.append(""+ySteps);
                            writerx.append("\n");
                            writerp.append("\n");
                        }
                        writerx.append("e");
                        writerp.flush();
                        writerp.close();
                        writerx.flush();
                        writerx.close();
                        Toast.makeText(Doodle.this,"created",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("not","created");
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    popUpWindowDismiss();
                    popW = false;
                    home.setImageResource(R.mipmap.home_b);
                }
                return true;
            });
            image.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(txt.size() != 0){
                        txt.get(txt.size()-1).clearFocus();
                    }
                    image.setImageResource(R.mipmap.image_a);
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    popW = false;
                    popUpWindowDismiss();
                    image.setImageResource(R.mipmap.image_b);
                }
                return false;
            });
            back.setOnClickListener(v -> finish());
            pop.setOnClickListener(v -> {
                if(!popW){
                    popUpWinLayout = new RelativeLayout(Doodle.this);
                    eraser.setImageResource(R.mipmap.eraser_b);
                    eraser.setBackground(Drawable.createFromPath("@null"));
                    pencil.setImageResource(R.mipmap.pencil_b);
                    pencil.setBackground(Drawable.createFromPath("@null"));
                    text.setImageResource(R.mipmap.text_b);
                    text.setBackground(Drawable.createFromPath("@null"));
                    home.setImageResource(R.mipmap.home_b);
                    home.setBackground(Drawable.createFromPath("@null"));
                    image.setImageResource(R.mipmap.image_b);
                    image.setBackground(Drawable.createFromPath("@null"));
                    back.setImageResource(R.mipmap.back);
                    back.setBackground(Drawable.createFromPath("@null"));
                    popUpWinLayout.addView(text);
                    popUpWinLayout.addView(pencil);
                    popUpWinLayout.addView(home);
                    popUpWinLayout.addView(eraser);
                    popUpWinLayout.addView(image);
                    popUpWinLayout.addView(back);
                    popupWindow = new PopupWindow(popUpWinLayout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.popUp)));
                    if(Build.VERSION.SDK_INT > 24){
                        popupWindow.setElevation(20);
                    }
                    popupWindow.setAnimationStyle(R.style.Animation);
                    popupWindow.setContentView(popUpWinLayout);
                    popupWindow.showAtLocation(popUpWinLayout, Gravity.CENTER,0,0);
                    popW = true;
                }
            });
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        savedMatrix = new Matrix();
        matrix = new Matrix();
        Bitmap bitmap = null, resized = null;
        imageView = new ImageView(Doodle.this);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            int height = 0, width = 0, minH = 500, dheight = 0;
            float h = 0, w = 0 ;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                if(bitmap.getHeight() - bitmap.getWidth() > 0){
                    if(bitmap.getHeight()>500){
                        dheight = bitmap.getHeight();
                        h = ((float)minH/ (float)dheight);
                        height = 500;
                        w = (float) bitmap.getWidth()*(h);
                        width = (int) w;
                    }
                    else {
                        height = bitmap.getHeight();
                        width = bitmap.getWidth();
                    }
                }
                else if(bitmap.getWidth() - bitmap.getHeight() > 0) {
                    if(bitmap.getWidth()>500){
                        w = ((float)minH/ (float)bitmap.getWidth());
                        width = 500;
                        h = (float) bitmap.getHeight()*(w);
                        height = (int) h;
                    }
                    else {
                        height = bitmap.getHeight();
                        width = bitmap.getWidth();
                    }
                }
                else{
                    height = 500;
                    width = 500;
                }
                resized = Bitmap.createScaledBitmap(bitmap,width,height,true);
                map = resized;
                imageView.setImageBitmap(resized);
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                matrix = imageView.getImageMatrix();
            }catch (Exception e){
                Log.d("no","image");
            }
            imageViews.add(imageView);
            relativeLayout.addView(imageViews.get(imageViews.size()-1));


            imageViews.get(imageViews.size()-1).setOnTouchListener(new View.OnTouchListener() {
                int pointerIndex, id, myIndex, action = 0, tap = 0;
                int mpointer = 0;
                float endx = 0, endy = 0;
                boolean moved = false, scaled = false;
                Bitmap p;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ImageView im = (ImageView)v;
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            scaled = false;
                            moved = false;
                            pointerIndex = MotionEventCompat.getActionIndex(event);
                            myIndex = pointerIndex;
                            start.set(event.getX(),event.getY());
                            int pointer = MotionEventCompat.getActionIndex(event);
                            map = ((BitmapDrawable)im.getDrawable()).getBitmap();
                            // txt.setText(""+MotionEventCompat.getX(event,pointer)+":"+""+MotionEventCompat.getY(event,pointer));
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            int pointerx = MotionEventCompat.getActionIndex(event);
                            mpointer = MotionEventCompat.findPointerIndex(event,MotionEventCompat.getPointerId(event,pointerx));
                            endx = MotionEventCompat.getX(event,pointerx);
                            endy = MotionEventCompat.getY(event,pointerx);
                            scaled = true;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            mpointer = 0;

                        case MotionEvent.ACTION_MOVE:
                            tap = 0;
                            moved = true;
                            if(mpointer == 0 && !scaled){
                                v.setX(event.getRawX()-start.x);
                                v.setY(event.getRawY()-start.y);
                                tap = 0;
                            }
                            if(mpointer == 1){
                                float dx,dy;
                                dx = MotionEventCompat.getX(event,mpointer) - endx;
                                dy = MotionEventCompat.getY(event,mpointer) - endy;
                                p = Bitmap.createScaledBitmap(map,map.getHeight()+(int)dx,map.getWidth()+(int)dy,true);
                                ((ImageView) v).setImageBitmap(p);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            scaled = false;
                            if(event.getX() == start.x && event.getY() == start.y){
                                if(!moved){
                                    tap++;
                                    if(tap == 2){
                                        relativeLayout.removeView(v);
                                        tap = 0;
                                    }
                                }
                            }
                            break;
                    }

                    return true;
                }

            });

        }

    }

    @SuppressLint("MissingPermission")
    public void connectToBT(){
        Log.d(TAG, "...onResume - try connect...");
        BluetoothDevice device = btAdapter.getRemoteDevice("00:28:13:00:21:FE");
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onStart() and socket create failed: " + e.getMessage() + ".");
        }
        btAdapter.cancelDiscovery();
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            btState = true;
            Log.d(TAG, "....Connection ok...");

        } catch (IOException e) {
           btState = false;
        }
        Log.d(TAG, "...Create Socket...");
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        receiveData = true;
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            while (receiveData) {
                try {
                    bytes = mmInStream.read(buffer);
                    receivedData = new String(buffer, 0, bytes);
                    //bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    Log.d("message",receivedData);
                   // receiveData = false;
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (Exception e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
            return (BluetoothSocket) m.invoke(device, MY_UUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e);
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    public class TransferData extends AsyncTask<Void,  Void, Void> {
        boolean first = true;
        int i = 3;
        String check;
        FileReader fr;
        BufferedReader bufreader;
        @SuppressLint("SetTextI18n")
        @Override
        protected  void onPreExecute(){
            String path = Environment.getExternalStorageDirectory().toString()+"/Doodle/SketchPath.txt";
            try {
                check = null;
                fr = new FileReader(path);
                bufreader = new BufferedReader(fr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("file","not found");
            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
            doodling = new TextView(Doodle.this);
            doodling.setText("Doodling...");
            progressLayout = new RelativeLayout(Doodle.this);
            progresslp  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            doodlinglp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            progressBar = new ProgressBar(Doodle.this);
            int pid = View.generateViewId();
            int did = View.generateViewId();
            doodling.setId(did);
            progressBar.setId(pid);
            doodling.setTextColor(getResources().getColor(R.color.fore));
            progresslp.addRule(RelativeLayout.CENTER_IN_PARENT);
            progressBar.setLayoutParams(progresslp);
            doodlinglp.addRule(RelativeLayout.BELOW,pid);
            doodlinglp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            doodling.setLayoutParams(doodlinglp);
            progressBar.setProgress(0);
            doodling.setTextSize(20);
            doodling.setPadding(3,3,3,3);
            progressLayout.addView(doodling);
            progressLayout.addView(progressBar);
            progress = new PopupWindow(progressLayout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            progress.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            progress.setAnimationStyle(R.style.Animation);
            progress.setContentView(progressLayout);
            progress.showAtLocation(progressLayout, Gravity.CENTER,0,0);
            super.onPreExecute();
        }

        @Override
        protected  Void doInBackground(Void args[]){
            try {
                while((check = bufreader.readLine()) != null){
                    mConnectedThread.write(check);
                    Thread.sleep(3);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressBar.setProgress(count);
            super.onProgressUpdate(values);
        }

        @Override
        protected  void onPostExecute(Void a){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try     {
                btSocket.close();
                btState = false;
                Log.d("bt","closed");
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
            }
            progressLayout.removeView(progressBar);
            progress.dismiss();
            super.onPostExecute(a);
        }


    }

}
