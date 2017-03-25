package setia.example.com.watchkids.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import setia.example.com.watchkids.APIHelper.WatchClient;
import setia.example.com.watchkids.Helper.PreferenceManager;
import setia.example.com.watchkids.Model.Respond;
import setia.example.com.watchkids.R;
import setia.example.com.watchkids.SQLHelper.SQLManager;

/**
 * Created by My Computer on 3/25/2017.
 */

public class ParentGetKidsService extends Service {
    private static final String TAG = "MyService";
    private boolean isRunning = false;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            int i = 1;
            while (isRunning) {
                synchronized (this) {
                    try {
                        wait(5*1000);
                        WatchClient.get().getKidsLocation(PreferenceManager.getId()).enqueue(new retrofit2.Callback<Respond>() {
                            @Override
                            public void onResponse(Call<Respond> call, Response<Respond> response) {
                                if(response.body().getError().equals(false)){
                                    SQLManager.openKC();
                                    SQLManager.updateDataKC(response.body().getDataKids());
                                    SQLManager.closeKC();
                                }else{
                                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Respond> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Tidak terhubung dengan jaringan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        isRunning = true;

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}
