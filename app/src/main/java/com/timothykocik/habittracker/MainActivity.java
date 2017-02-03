package com.timothykocik.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.timothykocik.habittracker.HabitContract.HabitEntry;

public class MainActivity extends AppCompatActivity {

    /**
     * Database helper that will help provide access to the database
     */
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        //Create or open a database to read
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Specify which columns from the database to use for this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_DESCRIPTION,
                HabitEntry.COLUMN_HABIT_FREQUENCY};

        //Perform a query on the habit table.
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            //Create a header in the TextView
            displayView.setText("This table contains " + cursor.getCount() + " habits. \n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_DESCRIPTION + " - " +
                    HabitEntry.COLUMN_HABIT_FREQUENCY + "\n");

            //Figure out the index of each column.
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DESCRIPTION);
            int frequencyColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_FREQUENCY);

            //Iterate through all the returned rows in the cursor.
            while (cursor.moveToNext()) {
                //Use the index to extract the Sting or Int value of the word at the current wor the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);
                String currentFrequency = cursor.getString(frequencyColumnIndex);
                //Display the value from each column of the current row
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentDescription + " - " +
                        currentFrequency));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertHabit() {
        //Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Creates a ContentValues object
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, "Meditate");
        values.put(HabitEntry.COLUMN_HABIT_DESCRIPTION, "Meditate 20 minutes a day.");
        values.put(HabitEntry.COLUMN_HABIT_FREQUENCY, HabitEntry.FREQUENCY_DAILY);

        //Insert a new row for Meditation in the database
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertHabit();
                displayDatabaseInfo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
