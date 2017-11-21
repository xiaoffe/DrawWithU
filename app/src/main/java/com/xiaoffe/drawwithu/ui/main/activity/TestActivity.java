package com.xiaoffe.drawwithu.ui.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMValueCallBack;
import com.xiaoffe.drawwithu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class TestActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    EditText editText2;
    Button button2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_acivity);
        initView();
        initEvent();
    }

    private void initView(){
        editText = (EditText) findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        editText2 = (EditText) findViewById(R.id.textView2);
        button2 = (Button)findViewById(R.id.button2);
    }

    private void initEvent(){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                List<TIMAddFriendRequest> addList = new ArrayList<TIMAddFriendRequest>();
                TIMAddFriendRequest request = new TIMAddFriendRequest();
                request.setIdentifier(content);
                addList.add(request);
                TIMFriendshipManager.getInstance().addFriend(addList, new TIMValueCallBack<List<TIMFriendResult>>() {
                    @Override
                    public void onError(int i, String s) {
                        Log.d("test", "add ERROR");
                        Toast.makeText(TestActivity.this, "add friend error"+s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(List<TIMFriendResult> timFriendResults) {
                        Log.d("test", "add SUCCESS");
                        Toast.makeText(TestActivity.this, "add friend success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText2.getText().toString();
                TIMFriendshipManager.getInstance().setSelfSignature(content, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.d("test", "setSelfSig error");
                        Toast.makeText(TestActivity.this, "setSelfSig error", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("test", "setSelfSig success");
                        Toast.makeText(TestActivity.this, "setSelfSig success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
