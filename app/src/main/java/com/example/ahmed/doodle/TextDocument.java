package com.example.ahmed.doodle;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

import com.example.doodle.R;

public class TextDocument extends AppCompatActivity {
    RelativeLayout layout, layoutt;
    private Toolbar toolbar;
    EditText editText;
    Window window;
    ImageButton bold, italic;
    Button set;
    PopupWindow popupWindow;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<Integer> size;
    Spinner spin, fontsize;
    ListView listView;
    String item[] = {"Arial","BodiniMT","Calibri","FranklinGothicDemi","Garamond","LucidaBright","ROCK","TimesNewRoman"};
    int sizeArray[] = new int[20], srt = 7, newposition = 0;
    boolean enabled = false, condition = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_document);
        View v = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN;
        v.setSystemUiVisibility(ui);
        spin = (Spinner)findViewById(R.id.spin);
        fontsize = (Spinner)findViewById(R.id.fontsize);
        bold = new ImageButton(this);
        bold.setBackground(Drawable.createFromPath("@null"));
        italic = new ImageButton(this);
        italic.setBackground(Drawable.createFromPath("@null"));
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item);
        size = new ArrayAdapter<Integer>(this,R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        size.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        for(int i = 0 ; i <= 15 ; i++ ){
            sizeArray[i] = srt;
            size.add(sizeArray[i]);
            srt++;
        }
        listView = new ListView(this);
        set = new Button(this);
        arrayAdapter.addAll(item);
        spin.setAdapter(arrayAdapter);
        fontsize.setAdapter(size);
        layout = (RelativeLayout)findViewById(R.id.activity_main);
        editText = (EditText) findViewById(R.id.ed);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Typeface typeface;
                newposition = position;
                switch (position){
                    case 0:
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/arial.ttf");
                        editText.setTypeface(typeface);
                        break;
                    case 1:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/BOD_R.TTF");
                        editText.setTypeface(typeface);
                        break;
                    case 2:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
                        editText.setTypeface(typeface);
                        break;
                    case 3:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/FRADM.TTF");
                        editText.setTypeface(typeface);
                        break;
                    case 4:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/GARA.TTF");
                        editText.setTypeface(typeface);
                        break;
                    case 5:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/LBRITE.TTF");
                        editText.setTypeface(typeface);
                        break;
                    case 6:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/ROCK.TTF");
                        editText.setTypeface(typeface);
                        break;
                    case 7:
                        typeface = Typeface.createFromAsset(getAssets(), "fonts/times.ttf");
                        editText.setTypeface(typeface);
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fontsize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int px =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,position+7,getResources().getDisplayMetrics());
                editText.setTextSize(px);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bold.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        bold.setImageResource(R.mipmap.bold_a);
                        int start = editText.getSelectionStart();
                        int end = editText.getSelectionEnd();
                        Typeface typeface;
                        Spannable text = new SpannableString(editText.toString());
                        switch (newposition){
                            case 0:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/arialbi.ttf");
                                editText.setTypeface(typeface);
                                break;
                            case 1:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/BOD_B.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 2:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/calibrib.ttf");
                                editText.setTypeface(typeface);
                                break;
                            case 3:
                                Toast.makeText(TextDocument.this,"Bold font is available",Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/GARABD.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 5:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/LBRITED.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 6:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/ROCKB.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 7:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/timesbd.ttf");
                                editText.setTypeface(typeface);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        bold.setImageResource(R.mipmap.bold_b);
                        layoutt.removeView(bold);
                        layoutt.removeView(italic);
                        popupWindow.dismiss();
                        break;
                    default:
                }
                return false;
            }
        });
        italic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        italic.setImageResource(R.mipmap.italic_a);
                        Typeface typeface;
                        switch (newposition){
                            case 0:
                                typeface = Typeface.createFromAsset(getAssets(),"fonts/ariali.ttf");
                                editText.setTypeface(typeface);
                                break;
                            case 1:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/BOD_I.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 2:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/calibrii.ttf");
                                editText.setTypeface(typeface);
                                break;
                            case 3:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/FRADMIT.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 4:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/GARAIT.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 5:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/LBRITEI.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 6:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/ROCKI.TTF");
                                editText.setTypeface(typeface);
                                break;
                            case 7:
                                typeface = Typeface.createFromAsset(getAssets(), "fonts/timesi.ttf");
                                editText.setTypeface(typeface);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        italic.setImageResource(R.mipmap.italic_b);
                        layoutt.removeView(bold);
                        layoutt.removeView(italic);
                        popupWindow.dismiss();
                        break;
                    default:
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*
        switch(item.getItemId()){
            case R.id.formatingOptions:
                RelativeLayout.LayoutParams blp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams itlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutt = new RelativeLayout(this);
                int bId = View.generateViewId();
                int iId = View.generateViewId();
                int uId = View.generateViewId();
                bold.setId(bId);
                italic.setId(iId);
                bold.setImageResource(R.mipmap.bold_b);
                italic.setImageResource(R.mipmap.italic_b);
                bold.setLayoutParams(blp);
                itlp.addRule(RelativeLayout.BELOW,bId);
                italic.setLayoutParams(itlp);
                layoutt.addView(italic);
                layoutt.addView(bold);
                popupWindow = new PopupWindow(layoutt, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pop)));
                popupWindow.setAnimationStyle(R.style.Animation);
                if(Build.VERSION.SDK_INT > 24){
                    popupWindow.setElevation(20);
                }
                popupWindow.setContentView(layoutt);
                popupWindow.showAtLocation(layoutt, Gravity.CENTER,0,0);
                break;
            default:
        }
*/
        if(item.getItemId() == R.id.formatingOptions)
        {
            RelativeLayout.LayoutParams blp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams itlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutt = new RelativeLayout(this);
            int bId = View.generateViewId();
            int iId = View.generateViewId();
            int uId = View.generateViewId();
            bold.setId(bId);
            italic.setId(iId);
            bold.setImageResource(R.mipmap.bold_b);
            italic.setImageResource(R.mipmap.italic_b);
            bold.setLayoutParams(blp);
            itlp.addRule(RelativeLayout.BELOW,bId);
            italic.setLayoutParams(itlp);
            layoutt.addView(italic);
            layoutt.addView(bold);
            popupWindow = new PopupWindow(layoutt, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pop)));
            popupWindow.setAnimationStyle(R.style.Animation);
            if(Build.VERSION.SDK_INT > 24){
                popupWindow.setElevation(20);
            }
            popupWindow.setContentView(layoutt);
            popupWindow.showAtLocation(layoutt, Gravity.CENTER,0,0);
        }
        return super.onOptionsItemSelected(item);
    }

}
