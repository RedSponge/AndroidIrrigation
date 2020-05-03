package com.redsponge.irrigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ProgramListActivity extends Activity {

    private ListView programList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);
        programList = findViewById(R.id.programList);
        programList.setAdapter(new ProgramAdapter(this));
        showList();
    }

    private void showList() {
        ((ArrayAdapter<Program>)programList.getAdapter()).clear();
        ((ArrayAdapter<Program>)programList.getAdapter()).addAll(((IrrigationApplication)getApplication()).getPrograms());
    }

    public void addProgram(View view) {
        Toast.makeText(this, "Adding a program :D", Toast.LENGTH_SHORT).show();
    }

    public void sendUpdatesToServer(View view) {
        Toast.makeText(this, "Sending data so cool", Toast.LENGTH_SHORT).show();
    }

    public void getDataFromServer(View view) {
        Toast.makeText(this, "Getting data so cool", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }
}
