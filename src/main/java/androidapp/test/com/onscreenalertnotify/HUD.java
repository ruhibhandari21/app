package androidapp.test.com.onscreenalertnotify;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Natarajan on 15-06-2015.
 */
public class HUD extends Service {

    private WindowManager windowManager;
    private ImageView close, appIcon;
    private RelativeLayout chatheadView;
    private RecyclerView recyclerView;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);



        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH ,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        chatheadView = (RelativeLayout) inflater.inflate(R.layout.activity_alert_dialog, null);
        close=(ImageView)chatheadView.findViewById(R.id.imgCloseIcon);
        appIcon = (ImageView) chatheadView.findViewById(R.id.imgEdtIcon);
        recyclerView=(RecyclerView)chatheadView.findViewById(R.id.recyclerView);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new StickerAdapter());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(@NotNull View view, int position) {
                        Toast.makeText(HUD.this, "clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(@Nullable View view, int position) {

                    }
                }));


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(chatheadView);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://saravananandroid.blogspot.in/"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopService(new Intent(getApplicationContext(), HUD.class));
//                startService(new Intent(getApplicationContext(), HUD.class));
            }
        });

        windowManager.addView(chatheadView, params);
        dragDrop();
    }

    int xOffset = 0, yOffset = 0, x = 0, y = 0;

    public void dragDrop(){

        /*close.setOnTouchListener(new View.OnTouchListener() {
            int prevX,prevY;
            *//**this does not work since framelayout could not be cast to the Window Manager**//*
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                final RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) v.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        par.topMargin += (int) event.getRawY() - prevY;
                        prevY = (int) event.getRawY();
                        par.leftMargin += (int) event.getRawX() - prevX;
                        prevX = (int) event.getRawX();
                        v.setLayoutParams(par);
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        par.topMargin += (int) event.getRawY() - prevY;
                        par.leftMargin += (int) event.getRawX() - prevX;
                        v.setLayoutParams(par);
                        return true;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        prevX = (int) event.getRawX();
                        prevY = (int) event.getRawY();
                        par.bottomMargin = -2 * v.getHeight();
                        par.rightMargin = -2 * v.getWidth();
                        v.setLayoutParams(par);
                        return true;
                    }
                }
                return false;
            }
        });*/

        appIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //* You can play around with the offset to set where you want the users finger to be on the view. Currently it should be centered.*//*
                    xOffset = v.getWidth()/2;
                    yOffset = v.getHeight()/2;
                    x = (int) event.getRawX() - xOffset;
                    y = (int) event.getRawY() - yOffset;
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            x,y,
                            WindowManager.LayoutParams.TYPE_PHONE ,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                            PixelFormat.TRANSLUCENT);
                    params.gravity = Gravity.TOP | Gravity. LEFT;
                    windowManager.updateViewLayout(chatheadView, params);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (chatheadView != null) windowManager.removeView(chatheadView);*/
    }
}

