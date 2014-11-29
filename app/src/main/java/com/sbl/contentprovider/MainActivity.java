package com.sbl.contentprovider;

import android.support.v4.app.FragmentActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {

    private SimpleCursorAdapter adapter;
    // Defines the id of the loader for later reference
    public static final int CONTACT_LOADER_ID = 1;


    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader =
            // anonymous inner class
            new LoaderManager.LoaderCallbacks<Cursor>() {
                // Create and return the actual cursor loader for the contacts data
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // Define the columns to retrieve
                    String[] projectionFields = new String[]{ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME};
                        /* this cursor loader asynchronously queries the contact content provider

                           onLoadFinished callback loads resulting Cursor(like resultSet in jdbc) into
                           the CursorAdapter 'adapter'.

                           CursorLoader is a loader that queries the contentresolver to get cursor
                        */
                    CursorLoader cursorLoader = new CursorLoader(MainActivity.this,
                            ContactsContract.Contacts.CONTENT_URI, // URI
                            projectionFields,  // projection fields
                            null, // the selection criteria
                            null, // the selection args
                            null // the sort order
                    );
                    // Return the loader for use
                    return cursorLoader;
                }

                // When the system finishes retrieving the Cursor,
                // a call to the onLoadFinished() method takes place.
                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    // The swapCursor() method assigns the new Cursor to the adapter
                    adapter.swapCursor(cursor);
                }

                // This method is triggered when the loader is being reset
                // and the loader data is no longer available.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    adapter.swapCursor(null);
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializes the adapter
        setupCursorAdapter();

        ListView lvContacts = (ListView) findViewById(R.id.lvContacts);
        lvContacts.setAdapter(adapter);

        getSupportLoaderManager().initLoader(CONTACT_LOADER_ID,
                new Bundle(), contactsLoader);
    }


    private void setupCursorAdapter() {
        // ContactsContract is a convenience class to access Android's contacts content provider
        String[] uiBindFrom = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        // textfield reference that is part of the android framework
        int[] uiBindTo = { android.R.id.text1 };
        /* simple_list_item_1 is a layout that is part of the android framework.
           Again, prefix with Android since it is part of the android framework, not specifically
           this project
         */
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
