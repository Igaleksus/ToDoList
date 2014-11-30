package ru.igaleksus.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class PickDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private OnDateSetListener onDateSetListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onDateSetListener = (OnDateSetListener) activity;
        } catch (ClassCastException ex){
            throw new ClassCastException(activity.toString() + "must implement ");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        onDateSetListener.onDateSet(c);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public interface OnDateSetListener {
        public void onDateSet(Calendar date);
    }
}