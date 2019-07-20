package com.example.manthanmkulakarni.asistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    TextToSpeech tts;
    SpeechRecognizer sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1=(Button)findViewById(R.id.button);
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                it.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                it.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                sr.startListening(it);
            }
        });
    }
    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)) {
            sr=SpeechRecognizer.createSpeechRecognizer(this);

            sr.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {

                    List<String> result=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                    processResult(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String s) {
        s.toLowerCase();
        if(s.indexOf("time")!=-1) {
            Calendar c=Calendar.getInstance();
            String time=c.getTime().toString();
            speak(time);
        }
        else if((s.indexOf("name")!=-1)||(s.indexOf("who are you")!=-1)){
            speak("i am your super assistant");
        }
        else if((s.indexOf("day")!=-1)||(s.indexOf("date")!=-1)){
            Calendar c=Calendar.getInstance();
            String d=c.getTime().toString();
            speak(d);
        }
        else  if(s.indexOf("internet")!=-1){
            speak("ok");
            Intent intnt=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com"));
            startActivity(intnt);
        }
        else {
            speak("could not get your command");
        }
    }

    private void initializeTextToSpeech(){
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
               if(tts.getEngines().size()==0){
                   Toast.makeText(MainActivity.this,"This app is not available in this device",Toast.LENGTH_LONG).show();
               }
               else {
                   tts.setLanguage(Locale.ENGLISH);
                   speak("your assistant is ready press the button to give command");
               }
            }
        });
    }

    private  void speak(String s) {
        if(Build.VERSION.SDK_INT>=21){
            tts.speak(s,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else {
            tts.speak(s,TextToSpeech.QUEUE_FLUSH,null);
        }
    }
}
