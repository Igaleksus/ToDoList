package ru.igaleksus.todolist;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mac on 03.11.14.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    CheckBoxListener checkBoxListener;



    int resource;
    public ToDoItemAdapter(Context context, int resource, List<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LinearLayout toDoView;
        ToDoItem item = getItem(position);

        String taskString = item.getTask();
        Date plannedDate = item.getPlannedDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(plannedDate);

        if (convertView == null){
            toDoView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, toDoView, true);
        } else {
            toDoView = (LinearLayout) convertView;
        }

        final CheckBox checkBox = (CheckBox) toDoView.findViewById(R.id.done_checkbox);

        TextView dateView = (TextView) toDoView.findViewById(R.id.rowDate);
        final TextView taskView = (TextView) toDoView.findViewById(R.id.todo_list_item_view);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ToDoItem toDoItem = getItem(position);
                CheckBox checkBox1 = (CheckBox) compoundButton;
                checkBoxListener.checkBoxChanged(toDoItem, checkBox1);
                checkBox1.setEnabled(false);

            }
        });


        checkBox.setChecked(item.isChecked());
        if (item.isChecked()) {
            checkBox.setEnabled(false);
            taskView.setPaintFlags(taskView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskView.setTypeface(null, Typeface.ITALIC);
            dateView.setPaintFlags(dateView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dateView.setTypeface(null, Typeface.ITALIC);
            toDoView.setAlpha(0.5f);
        } else {
            checkBox.setEnabled(true);
            taskView.setPaintFlags(taskView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            taskView.setTypeface(null);
            dateView.setPaintFlags(dateView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dateView.setTypeface(null);
            toDoView.setAlpha(1);
        }
        dateView.setText(dateString);
        taskView.setText(taskString);

        return toDoView;

    }

    public void setCheckBoxListener(CheckBoxListener checkBoxListener){
        this.checkBoxListener = checkBoxListener;
    }
    public interface CheckBoxListener{
        public void checkBoxChanged(ToDoItem toDoItem, CheckBox checkBox);
    }

}
