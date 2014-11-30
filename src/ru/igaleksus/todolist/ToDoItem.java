package ru.igaleksus.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mac on 03.11.14.
 */
public class ToDoItem {
    String task;
    Date plannedDate;
    int id;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public Date getPlannedDate() {
        return plannedDate;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public ToDoItem(String task, boolean isChecked){
        this(task, new Date(System.currentTimeMillis()), isChecked);

    }



    public ToDoItem(String task, Date plannedDate, boolean isChecked){
        this.task = task;
        this.plannedDate = plannedDate;
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(plannedDate);
        return "(" + dateString + ")" + task;
    }
}

//@startuml
//Alice -> Bob: synchronous call
//        Alice ->> Bob: asynchronous call
//Alice --> Bob: suck dick
// @enduml
