package com.google.developer.taskmaker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.taskmaker.data.DatabaseContract;
import com.google.developer.taskmaker.views.TaskTitleView;

import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID_MESSAGES = 0;

    TaskTitleView nameView;
    TextView dateView;
    ImageView priorityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        nameView = (TaskTitleView) findViewById(R.id.text_description);
        dateView = (TextView) findViewById(R.id.text_date);
        priorityView = (ImageView) findViewById(R.id.priority);

        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //TODO: Handle date selection from a DatePickerFragment
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e("uri", getIntent().getData().toString());
        return new CursorLoader(this, getIntent().getData(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            int id = DatabaseContract.getColumnInt(data, DatabaseContract.TaskColumns._ID);
            String description = DatabaseContract.getColumnString(data, DatabaseContract.TaskColumns.DESCRIPTION);
            int complete = DatabaseContract.getColumnInt(data, DatabaseContract.TaskColumns.IS_COMPLETE);
            int priority = DatabaseContract.getColumnInt(data, DatabaseContract.TaskColumns.IS_PRIORITY);
            long dueDate = DatabaseContract.getColumnLong(data, DatabaseContract.TaskColumns.IS_COMPLETE);

            nameView.setText(description);
            if(complete == 1){
                nameView.setState(TaskTitleView.DONE);
            }else if(dueDate != 0 && dueDate > (new Date()).getTime()){
                nameView.setState(TaskTitleView.OVERDUE);
            }else{
                nameView.setState(TaskTitleView.NORMAL);
            }
            if(dueDate != 0){
                dateView.append(" "+DateUtils.getRelativeTimeSpanString(dueDate));
            }else{
                dateView.append(" "+getString(R.string.date_empty));
            }
            if(priority == 1){
                priorityView.setImageResource(R.drawable.ic_priority);
            }else {
                priorityView.setImageResource(R.drawable.ic_not_priority);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
