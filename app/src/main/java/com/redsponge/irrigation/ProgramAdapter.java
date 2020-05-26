package com.redsponge.irrigation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProgramAdapter extends ArrayAdapter<Program> {


    public ProgramAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View workOn;
        if(convertView == null) {
            workOn = LayoutInflater.from(getContext()).inflate(R.layout.item_program, parent, false);
        } else {
            workOn = convertView;
        }

        final Program prog = getItem(position);
        assert prog != null;

        TextView dayDisplay = workOn.findViewById(R.id.tvDayDisplay);
        TextView timeDisplay = workOn.findViewById(R.id.tvStartTimeDisplay);
        TextView durationDisplay = workOn.findViewById(R.id.tvDurationDisplay);
        TextView valveDisplay = workOn.findViewById(R.id.tvValveDisplay);
        CheckBox isActiveCheckbox = workOn.findViewById(R.id.cbIsActive);
        Button btDelProg = workOn.findViewById(R.id.btDelProg);

        LinearLayout infoDisplay = workOn.findViewById(R.id.llInfoDisplay);
        infoDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProgramEditActivity.class);
                i.putExtra("program", position);
                getContext().startActivity(i);
            }
        });

        dayDisplay.setText(DateFormatSymbols.getInstance().getWeekdays()[prog.getDayOfWeek()]);
        timeDisplay.setText(String.format(Locale.UK, "%02d:%02d", prog.getStartHour(), prog.getStartMin()));
        int hours = prog.getDuration() / 3600;
        int mins = (prog.getDuration() - (hours * 3600)) / 60;
        durationDisplay.setText(String.format(Locale.UK, "Duration: %02d:%02d", hours, mins));
        valveDisplay.setText(String.format(Locale.UK, "Valve: %d", prog.getValve()));
        isActiveCheckbox.setChecked(prog.isActive());

        isActiveCheckbox.setOnClickListener((v) -> prog.setActive(isActiveCheckbox.isChecked()));

        if(getContext() instanceof IApplicationRetriever) {
            btDelProg.setOnClickListener((v) -> {
                ((IApplicationRetriever) getContext()).getApp().getPrograms().remove(prog);
                remove(prog);
            });
        } else {
            btDelProg.setActivated(false);
        }

        return workOn;
    }
}
