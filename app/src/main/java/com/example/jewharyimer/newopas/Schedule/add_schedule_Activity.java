package com.example.jewharyimer.newopas.Schedule;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
//implementation "com.nbsp:library:1.8"
import com.example.jewharyimer.newopas.R;
import com.example.jewharyimer.newopas.data.TaskContract;
import com.example.jewharyimer.newopas.data.TaskUpdateService;
import com.example.jewharyimer.newopas.views.DatePickerFragment;


public class add_schedule_Activity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    //Selected due date, stored as a timestamp
    private long mDueDate = Long.MAX_VALUE;

    private TextInputEditText mDescriptionView;
    private SwitchCompat mPrioritySelect;
    private TextView mDueDateView;

    private String mDate;

    private static final String KEY_DATE = "date_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_);

        mDescriptionView =  findViewById(R.id.text_input_description);
        mPrioritySelect = findViewById(R.id.switch_priority);
        mDueDateView = findViewById(R.id.text_date);
        View mSelectDate = findViewById(R.id.select_date);
        android.support.v7.widget.Toolbar toolbar =  findViewById(R.id.add_sch_toolbar);
        setSupportActionBar(toolbar);


        mSelectDate.setOnClickListener(this);
        updateDateDisplay();

        if (savedInstanceState != null) {
            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDueDateView.setText(savedDate);
            mDate = savedDate;
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_DATE, mDueDateView.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_add_task) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu_add_task);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_save) {
            saveItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /* Manage the selected date value */
    public void setDateSelection(long selectedTimestamp) {
        mDueDate = selectedTimestamp;
        updateDateDisplay();
    }

    public long getDateSelection() {
        return mDueDate;
    }

    /* Click events on Due Date */
    @Override
    public void onClick(View v) {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /* Date set events from dialog */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Set to noon on the selected day
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        setDateSelection(c.getTimeInMillis());
    }

    private void updateDateDisplay() {
        if (getDateSelection() == Long.MAX_VALUE) {
            mDueDateView.setText(R.string.date_empty);
        } else {
            CharSequence formatted = DateUtils.getRelativeTimeSpanString(this, mDueDate);
            mDueDateView.setText(formatted);
        }
    }

    private void saveItem() {
        //Insert a new item
        final ContentValues values = new ContentValues(4);
        values.put(TaskContract.TaskColumns.DESCRIPTION, mDescriptionView.getText().toString());
        values.put(TaskContract.TaskColumns.IS_PRIORITY, mPrioritySelect.isChecked() ? 1 : 0);
        values.put(TaskContract.TaskColumns.IS_COMPLETE, 0);
        values.put(TaskContract.TaskColumns.DUE_DATE, getDateSelection());
        TaskUpdateService.insertNewTask(this, values);

        finish();
    }

}
