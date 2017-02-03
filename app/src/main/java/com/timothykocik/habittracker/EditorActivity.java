package com.timothykocik.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.timothykocik.habittracker.HabitContract.HabitEntry;

/**
 * Created by jenniferkocik on 1/30/17.
 */

public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the habit name */
    private EditText mNameEditText;

    /** EditText field to enter the habit description */
    private EditText mDescriptionEditText;

    /** EditText field to enter the frequency */
    private Spinner mFrequencySpinner;

    /**
     * Frequency of the habit
     */
    private  int mFrequency = HabitEntry.FREQUENCY_UNKNOWN;

    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //Find all relevant views that we will need to read user input form
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_habit_description);
        mFrequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to choose frequency of the habit.
     */
    private void setupSpinner() {
        //Create adapter for spinner
        ArrayAdapter frequencySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_frequency_options, android.R.layout.simple_spinner_item);

        //Specify dropdown layout style
        frequencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //Apply the adapter to the spinner
        mFrequencySpinner.setAdapter(frequencySpinnerAdapter);

        //Set the integer
        mFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_frequency))) {
                        mFrequency = HabitEntry.FREQUENCY_DAILY;
                    } else if (selection.equals(getString(R.string.frequency_weekly))) {
                        mFrequency = HabitEntry.FREQUENCY_WEEKLY;
                    } else if (selection.equals(getString(R.string.frequency_biweekly))) {
                        mFrequency = HabitEntry.FREQUENCY_BIWEEKLY;
                    } else {
                        mFrequency = HabitEntry.FREQUENCY_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFrequency = HabitEntry.FREQUENCY_UNKNOWN;
            }
        });

        }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_DESCRIPTION, descriptionString);
        values.put(HabitEntry.COLUMN_HABIT_FREQUENCY, mFrequency);


        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (MainActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}