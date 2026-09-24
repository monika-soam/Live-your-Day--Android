package com.liveyourday.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.ArrayList;

public class MainActivity extends Activity {
    WebView web; SpeechRecognizer recognizer; Intent speechIntent;
    @Override public void onCreate(Bundle b){super.onCreate(b); web=new WebView(this); setContentView(web); WebSettings s=web.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setMediaPlaybackRequiresUserGesture(false); web.setWebViewClient(new WebViewClient()); web.setWebChromeClient(new WebChromeClient()); web.addJavascriptInterface(new VoiceBridge(),"AndroidVoice"); web.loadUrl("file:///android_asset/index.html");}
    void startVoice(){
        if(android.os.Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},42); return;}
        if(!SpeechRecognizer.isRecognitionAvailable(this)){web.evaluateJavascript("window.__lydAndroidError('unavailable')",null);return;}
        if(recognizer!=null) recognizer.destroy(); recognizer=SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new RecognitionListener(){
            public void onReadyForSpeech(Bundle p){web.evaluateJavascript("window.__lydAndroidStart()",null);} public void onBeginningOfSpeech(){} public void onRmsChanged(float r){} public void onBufferReceived(byte[] b){}
            public void onEndOfSpeech(){} public void onPartialResults(Bundle b){send(b);} public void onResults(Bundle b){send(b); web.evaluateJavascript("window.__lydAndroidEnd()",null);} public void onError(int e){web.evaluateJavascript("window.__lydAndroidError(''+"+e+")",null); web.evaluateJavascript("window.__lydAndroidEnd()",null);} public void onEvent(int t,Bundle p){}
        });
        speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-IN"); speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true); recognizer.startListening(speechIntent);
    }
    void send(Bundle b){ArrayList<String> a=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); if(a!=null&&!a.isEmpty()){String t=a.get(0).replace("\\","\\\\").replace("'","\\'"); web.evaluateJavascript("window.__lydAndroidResult('"+t+"')",null);}}
    void stopVoice(){if(recognizer!=null){recognizer.stopListening(); recognizer.destroy(); recognizer=null;}}
    public class VoiceBridge { @JavascriptInterface public void start(){runOnUiThread(()->startVoice());} @JavascriptInterface public void stop(){runOnUiThread(()->stopVoice());} }
    @Override public void onRequestPermissionsResult(int r,String[] p,int[] g){super.onRequestPermissionsResult(r,p,g); if(r==42 && g.length>0&&g[0]==PackageManager.PERMISSION_GRANTED) startVoice(); else web.evaluateJavascript("window.__lydAndroidError('not-allowed')",null);}
    @Override public void onBackPressed(){if(web.canGoBack()) web.goBack(); else super.onBackPressed();}
    @Override protected void onDestroy(){stopVoice(); web.destroy(); super.onDestroy();}
}
