package com.timothykocik.habittracker;

import android.provider.BaseColumns;

/**
 * Created by jenniferkocik on 1/31/17.
 */

public class HabitContract {

        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        private HabitContract() {}

        /**
         * Inner class that defines constant values for the habits database table.
         * Each entry in the table represents a single habit.
         */
        public static final class HabitEntry implements BaseColumns {

            /** Name of database table for habits */
            public final static String TABLE_NAME = "habits";

            /**
             * Unique ID number for the habit (only for use in the database table).
             *
             * Type: INTEGER
             */
            public final static String _ID = BaseColumns._ID;

            /**
             * Name of the habit.
             *
             * Type: TEXT
             */
            public final static String COLUMN_HABIT_NAME ="name";

            /**
             * Description of the habit..
             *
             * Type: TEXT
             */
            public final static String COLUMN_HABIT_DESCRIPTION = "description";

            /**
             * Frequency of the habit.
             *
             * The only possible values are {@link #FREQUENCY_DAILY}, {@link #FREQUENCY_WEEKLY},
             * or {@link #FREQUENCY_BIWEEKLY}.
             *
             * Type: INTEGER
             */
            public final static String COLUMN_HABIT_FREQUENCY = "frequency";


            /**
             * Possible values for the frequency of the habit.
             */
            public static final int FREQUENCY_UNKNOWN = 0;
            public static final int FREQUENCY_DAILY = 1;
            public static final int FREQUENCY_WEEKLY = 2;
            public static final int FREQUENCY_BIWEEKLY = 3;
        }

    }

