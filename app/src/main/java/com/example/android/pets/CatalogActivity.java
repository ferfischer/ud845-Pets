package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        //PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        //Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_WEIGHT
        };

        //Cursor cursor = db.query("pets",projection,null,null,null,null,null);

        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI,projection,null,null,null);


        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("The pets table contains " + cursor.getCount() + " pets.");

            displayView.append("\n" + PetEntry._ID+"  -  " + PetEntry.COLUMN_PET_NAME + "  -  " + PetEntry.COLUMN_PET_BREED + "  -  " + PetEntry.COLUMN_PET_GENDER + "  -  " + PetEntry.COLUMN_PET_WEIGHT);

            int idxId = cursor.getColumnIndex(PetEntry._ID);
            int idxName = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int idxBreed = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int idxGender = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int idxWeight = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            while ( cursor.moveToNext() ) {

                displayView.append("\n" +
                                    cursor.getInt(idxId) + " - " +
                                    cursor.getString(idxName) + " - " +
                                    cursor.getString(idxBreed) + " - " +
                                    cursor.getInt(idxGender) + " - " +
                                    cursor.getInt(idxWeight) );

            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertPet();
                displayDatabaseInfo();

                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet(){
        ContentValues fields = new ContentValues();
        fields.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        fields.put(PetEntry.COLUMN_PET_GENDER,  PetEntry.GENDER_MALE);
        fields.put(PetEntry.COLUMN_PET_NAME, "Toto");
        fields.put(PetEntry.COLUMN_PET_WEIGHT, "7");

        //PetDbHelper mDbHelper = new PetDbHelper(this);
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI, fields);
        //db.insert(PetEntry.TABLE_NAME, null, fields);

        //if (petId >= 0) {
            Toast.makeText( this, "Pet saved with Id: " + uri.getPath(), Toast.LENGTH_SHORT ).show();
        //} else {
            //Toast.makeText( this, "Error saving pet :(", Toast.LENGTH_SHORT).show();
        //}
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
}