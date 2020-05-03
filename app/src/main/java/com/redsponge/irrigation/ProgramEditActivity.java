package com.redsponge.irrigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Arrays;

import androidx.annotation.Nullable;

public class ProgramEditActivity extends Activity {

    private int progIndex;
    private Program prog;

    private Spinner spDayChooser;
    private TimePicker startTimePicker;
    private EditText etDuration;
    private Spinner spValveChooser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_program);
        progIndex = getIntent().getIntExtra("program", -1);
        if(progIndex == -1) {
            throw new RuntimeException("Didn't pass a program!");
        }
        prog = ((IrrigationApplication)getApplication()).getPrograms().get(progIndex);
        spDayChooser = findViewById(R.id.spDayChooser);
        startTimePicker = findViewById(R.id.timePicker);
        etDuration = findViewById(R.id.etMinuteInput);
        spValveChooser = findViewById(R.id.spValveChooser);

        initializeComponents();

        fetchInfo();
    }

    private void initializeComponents() {
        spDayChooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")));
        spValveChooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(1, 2, 3, 4)));
        startTimePicker.setIs24HourView(true);
    }

    private void fetchInfo() {
        Program prog = ((IrrigationApplication)getApplication()).getPrograms().get(progIndex);
        startTimePicker.setHour(prog.getStartHour());
        startTimePicker.setMinute(prog.getStartMin());
        spDayChooser.setSelection(prog.getDayOfWeek() - 1);
        spValveChooser.setSelection(prog.getValve() - 1);
        etDuration.setText("" + (prog.getDuration() / 60));
    }

    public void saveProgram(View view) {
        prog.setDayOfWeek(spDayChooser.getSelectedItemPosition() + 1);
        prog.setDuration(Integer.parseInt(etDuration.getText().toString()) * 60);
        prog.setStartHour(startTimePicker.getHour());
        prog.setStartMin(startTimePicker.getMinute());
        prog.setValve(spValveChooser.getSelectedItemPosition() + 1);
        finish();
    }

    public void cancelEdit(View view) {
        finish();
    }
}
