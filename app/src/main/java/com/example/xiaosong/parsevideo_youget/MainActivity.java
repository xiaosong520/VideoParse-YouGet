package com.example.xiaosong.parsevideo_youget;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import xyz.yhsj.parse.entity.ParseResult;
import xyz.yhsj.parse.extractors.QQ;


public class MainActivity extends AppCompatActivity {

    private ParseResult result;
    private ProgressDialog dialog;
    final static int MSG_BACK_PARSE_VIDEO =0x0001;
    final static int MSG_UI_PARSE_VIDEO =0x0002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_parse_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("正在解析...");
                dialog.show();
                mHander.sendEmptyMessageDelayed(MSG_BACK_PARSE_VIDEO,500);
            }
        });

    }

    private Handler mHander = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case  MSG_BACK_PARSE_VIDEO://处理耗时任务
                    ParseVideo();
                    break;
                case  MSG_UI_PARSE_VIDEO://处理完成，回到UI线程更新事件

                    dialog.dismiss();

                    if (result.getCode() == 200) {
                        String info ="解析成功: "
                                +"\n\n视频名称："+result.getData().getTitle()
                                +"\n\nUrl链接列表信息："+result.getData().getUrlList().get(0);
                        setupViews(info);
                    } else {
                        setupViews("解析视频失败");
                    }
                    break;
            }
            return true;
        }
    });


    /**
     * 将数据显示到手机界面上
     */
    private void setupViews(String data){
        EditText textView = (EditText) findViewById(R.id.textView);
        textView.setText(data);
    }

    private void ParseVideo() {
        //处理耗时操作
        new MyThread().start();
    }

    private class MyThread extends Thread {
        public void run() {

            //result = YouKu.INSTANCE.parseResult("http://v.youku.com/v_show/id_XMjczMjU4NzI2NA==.html");
            result = QQ.INSTANCE.parseResult("https://imgcache.qq.com/tencentvideo_v1/playerv3/TPout.swf?max_age=86400&v=20161117&vid=w050031rgt3&auto=0");
            //result = Iqiyi.INSTANCE.parseResult("http://www.iqiyi.com/v_19rr97rjk0.html");
            mHander.sendEmptyMessage(MSG_UI_PARSE_VIDEO);
        }
    }

}
