package ru.igaleksus.todolist;

import android.app.*;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener, PickDateFragment.OnDateSetListener,
        ToDoListFragment.OnEditAndDeleteAndCheckedToDoItemListener, LoaderManager.LoaderCallbacks<Cursor>, ToDoItemAdapter.CheckBoxListener{

    private ArrayList<ToDoItem> todoItems;
    private ToDoItemAdapter aa;

    public Date pickedDate;



    /**
     * Called when the activity is first plannedDate.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.TodoListFragment);

        int resID = R.layout.todolist_item;
        todoItems = new ArrayList<ToDoItem>();

        aa = new ToDoItemAdapter(this, resID, todoItems);
        aa.setCheckBoxListener(this);

        toDoListFragment.setListAdapter(aa);



        getLoaderManager().initLoader(0, null, this);



    }

    @Override
    protected void onResume() {
        super.onResume();
//        getLoaderManager().restartLoader(0, null, null);
    }

    @Override
    public void onNewItemAdded(String newItem) {
        Date currentDate = new Date(System.currentTimeMillis());
//        long date = pickedDate.getTime();
        ToDoItem newToDoItem;
        if (pickedDate == null) {
            pickedDate = currentDate;
            newToDoItem = new ToDoItem(newItem, false);
        } else newToDoItem = new ToDoItem(newItem, pickedDate, false);

        todoItems.add(0, newToDoItem);
        newToDoItem.setId(todoItems.indexOf(newToDoItem));
        aa.notifyDataSetChanged();


        pickedDate = currentDate;

//        ContentResolver contentResolver = getContentResolver();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ToDoContentProvider.KEY_TASK, newItem);
//        contentValues.put(ToDoContentProvider.KEY_PLANNED_DATE, date);
//        contentResolver.insert(ToDoContentProvider.CONTENT_URI, contentValues);
//        getLoaderManager().restartLoader(0, null, this);

    }

    public void pickDate(){
        FragmentManager fm = getFragmentManager();
        DialogFragment dateFragment = new PickDateFragment();
        dateFragment.show(fm, "DATE_PICKER");
    }


    @Override
    public void onDateSet(Calendar date) {
        pickedDate = date.getTime();


    }

    @Override
    public void onEdit(ToDoItem toDoItem) {
        FragmentManager fm = getFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();
        editTaskDialogFragment.setToDoItemAdapter(aa);
        editTaskDialogFragment.setEditableView(toDoItem);
        editTaskDialogFragment.show(fm, "EDIT_DIALOG");
    }

    @Override
    public void onDelete(ToDoItem toDoItem) {
        aa.remove(toDoItem);
        aa.notifyDataSetChanged();
    }



    @Override
    public void checkBoxChanged(ToDoItem toDoItem, CheckBox checkBox) {
        toDoItem.setChecked(checkBox.isChecked());

        aa.notifyDataSetChanged();
    }


    public static class EditTaskDialogFragment extends DialogFragment{
        ToDoItem editableView;
        ToDoItemAdapter toDoItemAdapter;



        public void setToDoItemAdapter(ToDoItemAdapter toDoItemAdapter) {
            this.toDoItemAdapter = toDoItemAdapter;
        }



        public void setEditableView(ToDoItem editableView) {
            this.editableView = editableView;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getDialog().setTitle("Editing Task");
            View view = inflater.inflate(R.layout.edit_task_dialog, container);
            final EditText editTask = (EditText) view.findViewById(R.id.editTask);
            Button okEdit = (Button) view.findViewById(R.id.okEditTask);
            Button cancelEdit = (Button) view.findViewById(R.id.cancelEditTask);



            editTask.setText(editableView.getTask());

            okEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editableView.setTask(editTask.getText().toString());
                    EditTaskDialogFragment.this.dismiss();
                    toDoItemAdapter.notifyDataSetChanged();
                }
            });
            cancelEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskDialogFragment.this.dismiss();


                }
            });

            return view;

        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = new CursorLoader(this, ToDoContentProvider.CONTENT_URI, null, null, null, null);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int keyTaskIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);
//        int keyIsDone = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_IS_DONE);
//        int keyPlannedDate = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_PLANNED_DATE);

        todoItems.clear();

        while (cursor.moveToNext()){
//            boolean isDone = cursor.getInt(keyIsDone) > 0;

            ToDoItem newItem = new ToDoItem(cursor.getString(keyTaskIndex), false);

        }

        aa.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
