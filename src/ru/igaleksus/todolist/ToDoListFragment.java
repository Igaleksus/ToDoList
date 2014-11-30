package ru.igaleksus.todolist;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

/**
 * Created by mac on 01.11.14.
 */
public class ToDoListFragment extends ListFragment {

    private ToDoItem clickedToDoItem;


    OnEditAndDeleteAndCheckedToDoItemListener onEditAndDeleteAndCheckedToDoItemListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onEditAndDeleteAndCheckedToDoItemListener = (OnEditAndDeleteAndCheckedToDoItemListener) activity;
        } catch (ClassCastException ex){
            Log.d("EXCEPTION", "Activity must implement onEditAndDeleteToDoItemListener");
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());

//        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                    clickedToDoItem = (ToDoItem) getListView().getItemAtPosition(i);
//                    clickedToDoItem.setChecked(true);
//
//            }
//        });




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        ListView lv = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        clickedToDoItem = (ToDoItem) lv.getItemAtPosition(acmi.position);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_for_todolist_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.editItem):
                onEditAndDeleteAndCheckedToDoItemListener.onEdit(clickedToDoItem);
                break;
            case (R.id.deleteItem):
                getSelectedItemPosition();
                onEditAndDeleteAndCheckedToDoItemListener.onDelete(clickedToDoItem);
                break;
        }
        return true;
    }

    public interface OnEditAndDeleteAndCheckedToDoItemListener {
        public void onEdit(ToDoItem toDoItem);
        public void onDelete(ToDoItem toDoItem);

    }
}
