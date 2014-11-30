package ru.igaleksus.todolist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by mac on 01.11.14.
 */
public class NewItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_item_fragment, container, false);

        final EditText myEditText = (EditText) view.findViewById(R.id.myEditText);
        final ImageButton pickDateButton = (ImageButton) view.findViewById(R.id.pickDateButton);
        final ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);

        myEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        if ((i == KeyEvent.KEYCODE_DPAD_CENTER) || (i == KeyEvent.KEYCODE_ENTER)) {
                            if (!myEditText.getText().toString().equals("")) {
                                String newItem = myEditText.getText().toString();
                                onNewItemAddedListener.onNewItemAdded(newItem);
                                myEditText.setText("");

                            } else Toast.makeText(getActivity(), "Enter some task first", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }



                return false;
            }
        });

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewItemAddedListener.pickDate();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!myEditText.getText().toString().equals("")) {
                    String newItem = myEditText.getText().toString();
                    onNewItemAddedListener.onNewItemAdded(newItem);
                    myEditText.setText("");
                } else Toast.makeText(getActivity(), "Enter some task first", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }


    public interface OnNewItemAddedListener {
        public void onNewItemAdded(String newItem);
        public void pickDate();


    }

    private OnNewItemAddedListener onNewItemAddedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onNewItemAddedListener = (OnNewItemAddedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement OnNewItemAddedListener");
        }
    }

}
