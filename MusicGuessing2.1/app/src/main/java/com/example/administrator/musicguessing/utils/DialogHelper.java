package com.example.administrator.musicguessing.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.musicguessing.R;
import com.example.administrator.musicguessing.listener.OnDialogListener;

/**
 * Created by Administrator on 2018/11/25.
 */

public class DialogHelper {
    private static AlertDialog mAlertDialog;

    public static void showDialog(Context context, String message, final OnDialogListener listener){
        View dialogView = null;
        dialogView = GameUtil.getView(context, R.layout.dialog_view);
        ImageButton btnOkView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);
        TextView txtMessageView = (TextView) dialogView.findViewById(R.id.text_dialog_message);
        txtMessageView.setText(message);
        btnOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭对话框
                if(mAlertDialog != null){
                    mAlertDialog.cancel();
                }

                //事件回调
                if(listener != null){
                    listener.onClick();
                }
            }
        });

        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAlertDialog != null){
                    mAlertDialog.cancel();
                }
            }
        });

        //创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        mAlertDialog = builder.create();
        mAlertDialog.show();

    }
}
