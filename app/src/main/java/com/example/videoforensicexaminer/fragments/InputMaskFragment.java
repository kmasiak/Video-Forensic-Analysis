package com.example.videoforensicexaminer.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.videoforensicexaminer.R;
import com.example.videoforensicexaminer.activities.MainActivity;

public class InputMaskFragment extends DialogFragment {
    private static final String TAG = "DialogFragment";
    ImageButton cloth, surgical, n95, ffp2, ffp3, other;
    //Button none;

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_mask, null);

        cloth = view.findViewById(R.id.cloth);
        surgical = view.findViewById(R.id.surgical);
        n95 = view.findViewById(R.id.n95);
        ffp2 = view.findViewById(R.id.ffp2);
        ffp3 = view.findViewById(R.id.ffp3);
        other = view.findViewById(R.id.other);
        //none = view.findViewById(R.id.no_mask);

        createListener(cloth);
        createListener(surgical);
        createListener(n95);
        createListener(ffp2);
        createListener(ffp3);
        createListener(other);
        //createListenerNone(none);

        return view;
    }

    public void createListener(final ImageButton btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String maskType = "";

                switch (btn.getId()) {
                    case R.id.cloth:
                        maskType = "Cloth";
                        break;
                    case R.id.surgical:
                        maskType = "Surgical";
                        break;
                    case R.id.n95:
                        maskType = "N95";
                        break;
                    case R.id.ffp2:
                        maskType = "FFP2";
                        break;
                    case R.id.ffp3:
                        maskType = "FFP3";
                        break;
                    case R.id.other:
                        maskType = "Other";
                        break;
                    //case R.id.no_mask:
                    //    maskType = "None";
                    //    break;
                }




                //Intent intent = new Intent();
                //intent.putExtra("Mask Type", maskType);
                //getTargetFragment().onActivityResult(getTargetRequestCode(), 2, intent);
                dismiss();
            }
        });
    }


/*
    public interface OnDataPass {
        public void onDataPass(String data);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }*/

    public void createListenerNone(final Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maskType = "None";
                Intent intent = new Intent();
                intent.putExtra("Mask Type", maskType);
                //getTargetFragment().onActivityResult(getTargetRequestCode(), 2, intent);
                dismiss();
            }
        });
    }
}

