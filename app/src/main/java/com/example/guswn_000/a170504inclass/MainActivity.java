package com.example.guswn_000.a170504inclass;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    EditText et;
    WebView webView;
    Animation animtop;
    LinearLayout linearLayout, biglinear;
    ListView listView;
    ArrayList<String> urllist = new ArrayList<>();
    ArrayList<Bookmark> bookmarks = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Web View");
        et = (EditText)findViewById(R.id.editText);
        webView = (WebView) findViewById(R.id.WebView);
        linearLayout = (LinearLayout)findViewById(R.id.linear);
        listView = (ListView)findViewById(R.id.listview);
        biglinear = (LinearLayout)findViewById(R.id.biglinear);

        final ProgressDialog dialog;
        dialog = new ProgressDialog(this);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,urllist);
        listView.setAdapter(adapter);
        setitemlistener();

        //이걸 해줘야 통신 가능
        webView.addJavascriptInterface(new JavaScriptMethods() , "MyApp"); // 앞에꺼는 저 밑에 만들어둔 클래스. 자바스크립트가 얘를 부를떄 쓸 이름이 두번째 인자
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() //webView를 어느 어플로 실행시킬건지? 그 앱이 Webview 클라이언트다
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) //이함수는 반드시 오버라이딩돼야함
            {
                return super.shouldOverrideUrlLoading(view, url); //이렇게하면 이 앱 안에 웹뷰가 뜸
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("Loading...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();

                //로딩시작할때 로딩중...뜨게하기
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                et.setText(url);
            }
        });
        webView.loadUrl("http://mengkkimon.tistory.com");


        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 100) //로딩이 완료되면..100이면 로딩됐다는뜻
                    dialog.dismiss();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result)
            {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });

        animtop = AnimationUtils.loadAnimation(this,R.anim.translate_top);
        animtop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0,1,0,"즐겨찾기목록"); //2번쨰가 아이디,3번째가 순서 4번쨰가 이름
        menu.add(0,2,1,"즐겨찾기추가");

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == 1)
        {
//            webView.loadUrl("file:///android_asset/www/index.html"); // 이러면 index.html이 내 웹뷰에 뜬다
            linearLayout.setAnimation(animtop);
            animtop.start();
            biglinear.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
        else if(item.getItemId() == 2)
        {

            biglinear.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);

            webView.loadUrl("file:///android_asset/www/urladd.html");
            linearLayout.setAnimation(animtop);
            animtop.start();
        }
        return super.onOptionsItemSelected(item);
    }



    Handler myhandler = new Handler();
    class JavaScriptMethods
    {
        JavaScriptMethods(){}
//        @JavascriptInterface // 이렇게 해준애만 웹페이지에서 호출해줄수있다. @Java이거꼭해줄것
//        public void displayToast() //얘는 통신해야하기떄문에 반드시 퍼블릭
//        {
//            myhandler.post(new Runnable() {
//                @Override
//                public void run()
//                {
//                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                    dlg.setTitle("그림변경")
//                            .setMessage("그림을 변경하시겠습니까?")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                    webView.loadUrl("javascript:changeImage()");
//                                }
//                            })
//                            .setNegativeButton("Cancel" , null)
//                            .show();
//                }
//            });
//        }

        @JavascriptInterface
        public void showurllayout()
        {
            myhandler.post(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        }


        @JavascriptInterface //이거안하면 이메소드는 못씀
        public void addbookmark(final String url, final String sname)
        {
            if(bookmarks.size() == 0)
            {
                urllist.add("< "+sname+" >  "+url);
                bookmarks.add(new Bookmark(sname,url));
                adapter.notifyDataSetChanged();
            }
            else
            {
                //중복되는 url 체크
                boolean isexist = false;
                for(int i = 0; i < urllist.size() ; i++)
                {
                    if(bookmarks.get(i).getUrl().equals(url))
                    {
                        isexist = true;
                    }
                }
                if(isexist)
                {
                    webView.loadUrl("javascript:displayMsg()");
                }
                else
                {
                    urllist.add("< "+sname+" >  "+url);
                    bookmarks.add(new Bookmark(sname,url));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    public void onClick(View v)
    {
        if (v.getId() == R.id.button)
        {
            webView.loadUrl(et.getText().toString()); // 이러면 index.html의 체인지이미지 펑션을 가져옴
        }
    }
    public void setitemlistener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                listView.setVisibility(View.INVISIBLE);
                biglinear.setVisibility(View.VISIBLE);
                webView.loadUrl(bookmarks.get(position).getUrl());
//                et.setText(bookmarks.get(position).getUrl());
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제 확인")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                bookmarks.remove(position);
                                urllist.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .setMessage("정말 삭제하시겠습니까?")
                        .show();
                return true;
            }
        });
    }

}
