package com.example.videoforensicanalysis.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.videoforensicanalysis.R;

public class InputCorpusEnvFragment extends DialogFragment {
    /*
    private static final String TAG = "DialogFragment";
    ImageButton cloth, surgical, n95, ffp2, ffp3, other;
    Button none;

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
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_env_corpus, null);
        return view;
    }
/*
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
                    //   break;
                }

                Intent intent = new Intent();
                intent.putExtra("Mask Type", maskType);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 2, intent);
                dismiss();
            }
        });
    }

    public void createListenerNone(final Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maskType = "None";
                Intent intent = new Intent();
                intent.putExtra("Mask Type", maskType);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 2, intent);
                dismiss();
            }
        });
    }*/
}
