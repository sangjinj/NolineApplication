package noline.nolineapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class DaemonService extends Service  implements Runnable {
    // 시작 ID
    private int mStartId;
    // 서비스에 대한 스레드에 연결된 Handler. 타이머 이용한 반복 처리시 사용.
    private Handler mHandler;
    // 서비스 동작여부 flag
    private boolean mRunning;
    // 타이머 설정 (2초)
    private static final int TIMER_PERIOD = 10 * 1000;
    private static final int COUNT = 10000;
    private int mCounter;
    private String channel = "A";
    @Override
    public void run() {
        System.out.println(" DaemonService start:::::::::::::::::::::::::::::::::::::::::");
        mHandler.postDelayed(this, TIMER_PERIOD);
    }

    // 서비스를 생성할 때 호출
    public void onCreate() {

        Toast.makeText(this, "[DaenomService] onCreate() 함수 호출", Toast.LENGTH_SHORT).show();
        super.onCreate();
        mHandler = new Handler();
        mRunning = false;
    }

    // 서비스 시작할 때 호출. background에서의 처리가 시작됨.
    // startId : 서비스 시작요구 id. stopSelf에서 종료할 때 사용.
    //onStart는 여러번 호출될 수 있기 때문에 식별자로 사용.

    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "[DaemonService] onStart() 함수 호출", Toast.LENGTH_SHORT).show();
        super.onStart(intent, startId);
        mStartId = startId;
        mCounter = COUNT;

        // 동작중이 아니면 run 메소드를 일정 시간 후에 시작
        if (!mRunning) {
            // this : 서비스 처리의 본체인 run 메소드. Runnable 인터페이스를 구현 필요.
            // postDelayed : 일정시간마다 메소드 호출
            mHandler.postDelayed(this, TIMER_PERIOD);
            mRunning = true;
        }
    }



    // 서비스의 종료시 호출
    public void onDestroy() {
        // onDestroy가 호출되어 서비스가 종료되어도
        // postDelayed는 바로 정지되지 않고 다음 번 run 메소드를 호출.
        Toast.makeText(this, "[DaemonService] onDestroy() 함수 호출", Toast.LENGTH_SHORT).show();
        mRunning = false;
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
