package com.example.ainurbayanova.kolesa.mvp.view.fragments.Little_Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ainurbayanova.kolesa.R;

public class dialog extends DialogFragment{

    IClicked iClicked;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iClicked = (IClicked) activity;
        }
        catch (ClassCastException e){
            throw  new ClassCastException(activity.toString() + " must implement AddingTask");
        }
    }

    public interface  IClicked{
        void add(String text);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)  {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom)
                .setView(dialogView);

        TextView save = dialogView.findViewById(R.id.save);
        TextView cancel = dialogView.findViewById(R.id.cancel);

        final EditText text = dialogView.findViewById(R.id.text);
        final TextInputLayout textLayout = dialogView.findViewById(R.id.textLayout);

        final AlertDialog dialog = adb.create();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(text.getText().toString().trim())){
                    iClicked.add(text.getText().toString());
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity(),"Please fill credentials",Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }



}
