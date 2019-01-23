package com.example.anish.speechtotext2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView mText;
    private SpeechRecognizer sr;
    private static final String TAG = "MyStt3Activity";
    private WebView ourBrow;
    ArrayList<String> data;
    private Intent intent;
    CountDownTimer q;
    private boolean timerRunning = false;
     c l;

    private AudioManager mAudioManager;
    private int mStreamVolume = 0;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton speakButton = (ImageButton) findViewById(R.id.imageButton4);
        mText = (TextView) findViewById(R.id.textView);
        speakButton.setOnClickListener(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        ourBrow = (WebView)findViewById(R.id.webView1);
        ourBrow.getSettings().setJavaScriptEnabled(true);
        ourBrow.loadUrl("file:///android_asset/anish.html");
        ourBrow.addJavascriptInterface(new WebAppInterface(this), "Android");
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());

    }

    class listener implements RecognitionListener
    {
        Context y;

        public listener(){
            y = getApplicationContext();
            l = new c(y,this);

                    q = new CountDownTimer(1000L, 100L)
                    {
                        public void onFinish()
                        {
                            l.run();
                        }
                        public void onTick(long paramAnonymousLong)
                        {
                        }
                    };
        }
        public void onReadyForSpeech(Bundle params)
        {

            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            if(timerRunning){
                q.start();
            }

            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d(TAG,  "error " +  error);
            mText.setText("error " + error);
        }
        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.imageButton4)
        {
            this.intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            this.intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            this.intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            if(timerRunning){
                sr.stopListening();
                q.cancel();
                timerRunning = false;
            }
            else{
                timerRunning = true;
                sr.startListening(intent);

            }

        }
    }
    class c implements Runnable {

        Context a;
        RecognitionListener s;
        public c(Context paramContext , RecognitionListener param){
            this.a = paramContext;
            this.s = param;
        }
        @Override
        public void run() {
            sr = SpeechRecognizer.createSpeechRecognizer(this.a);
            sr.setRecognitionListener(new listener());
            sr.startListening(intent);
        }
    }

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public String showToast() {

            return data.get(0);
        }
    }
}
