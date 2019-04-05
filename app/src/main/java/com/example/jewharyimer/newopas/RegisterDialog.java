package com.example.jewharyimer.newopas;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RegisterDialog extends AppCompatDialogFragment {
    private EditText editTextmemb1;
    private ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        editTextmemb1 = view.findViewById(R.id.member1);
        builder.setView(view)
                .setTitle("Add Project Members(1-6)")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(RegisterDialog.this,"AT LEAST ENTER 2 MENBER",Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String memb1 = editTextmemb1.getText().toString();



                        listener.applyTexts(memb1);
                    }
                });


        return builder.create();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }



    public interface ExampleDialogListener {
        void applyTexts(String memb1);
    }

}
