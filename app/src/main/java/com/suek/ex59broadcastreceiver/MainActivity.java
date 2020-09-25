package com.suek.ex59broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //클릭버튼을 눌렀을때 intent 가 MyReceiver 로 방송을 보내면 MyReceiver 가 방송을 받는다. Received!
    public void clickBtn(View view) {
        //명시적 인텐트로 리시버 실행하기 (운영체제가 보내는 방송은 무조건 명시적)
        //같은 앱안에 있는 리시버만 실행할 수 있음 [***메니페스트에 등록***이 되어있어야함]
        Intent intent= new Intent(this, MyReceiver.class);
        sendBroadcast(intent);
    }



    // Oreo 버전부터 브로드캐스트나 서비스 컴포넌트 사용에 제한을 두고있음(백그라운드에서 너무 리소스를 많이사용해서 배터리문제...)
    // - 운영체제가 방송하는 시스템 브로드캐스트는 정상적으로 동작됨
    // - 개발자의 임의로 보내는 방송은 [동적 리시버등록]을 해야만 사용가능
    // 즉, Manifest.xml 에 리시버를 등록하지않고 JAVA 코드로 리시버를 등록!!! - [동적 리시버등록]
    // 즉, 앱이 켜져 있을때만 묵시적 방송을 듣도록 제약!!

    public void clickBtn2(View view) {
        //묵시적 인텐트로 방식 보내기 : 디바이스에 설치된 모든 앱에게 방송함 -> 다른앱들도 방송을 받을 수 있음.
        Intent intent= new Intent();
        intent.setAction("aaa"); //방송의 액션값(식별값) 지정
        sendBroadcast(intent);
    }


    //액티비티가 화면에 완전히 보여질때 자동으로 발동하는 콜백메소드
    //즉, onCreate() 실행 후 onStart() 실행 후 실행되는 메소드 [lifecycle method]
    @Override
    protected void onResume() {    //onStart() 와 비슷
        super.onResume();

        //동적으로 리시버 등록 [aaa 액션을 듣는]
        myReceiver= new MyReceiver();
        IntentFilter filter= new IntentFilter("aaa");
        registerReceiver(myReceiver,filter);    //myReceiver 등록. filter 에
    }

    //화면에 안보일 때 자동으로 발동하는 메소드
    @Override
    protected void onPause() {
        super.onPause();

        //등록했던 리시버를 등록해제
        unregisterReceiver(myReceiver);   //앱이 꺼저있을 때 방송을 들을수 없도록?
    }
}
