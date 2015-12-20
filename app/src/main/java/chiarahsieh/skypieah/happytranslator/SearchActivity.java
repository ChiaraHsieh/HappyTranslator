package chiarahsieh.skypieah.happytranslator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private static String fileName = "happydict.json";

    private EditText etSearch;
    private ListView lvEntry;
    private ArrayList<EntryClause> mEntryArrayList;
    private EntryListAdapter elAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etSearch = (EditText) findViewById(R.id.etSearch);
        lvEntry = (ListView) findViewById(R.id.lvEntry);

        // Add Text Change Listener to EditText
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                elAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void showAlertDialog() {

        View addView = View.inflate(this, R.layout.add_dialog, null);
        final EditText etEng = (EditText) addView.findViewById(R.id.etEng);
        final EditText etChi = (EditText) addView.findViewById(R.id.etChi);
        final EditText etTwn = (EditText) addView.findViewById(R.id.etTwn);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.add_entry)
                .setView(addView)
                .setPositiveButton(R.string.add_ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EntryClause newEntry = new EntryClause(
                                etEng.getText().toString(),
                                etChi.getText().toString(),
                                etTwn.getText().toString()
                        );
                        mEntryArrayList.add(0, newEntry);
                        elAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }})
                .setNegativeButton(R.string.add_cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(SearchActivity.this,
//                                getResources().getText(R.string.add_cancel),
//                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    private void editAlertDialog(EntryClause entry, final int posInList) {

        View addView = View.inflate(this, R.layout.add_dialog, null);
        final EditText etEng = (EditText) addView.findViewById(R.id.etEng);
        final EditText etChi = (EditText) addView.findViewById(R.id.etChi);
        final EditText etTwn = (EditText) addView.findViewById(R.id.etTwn);

        etEng.setText(entry.getEng());
        etChi.setText(entry.getChi());
        etTwn.setText(entry.getTwn());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.edit_entry)
                .setView(addView)
                .setPositiveButton(R.string.edit_ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EntryClause newEntry = new EntryClause(
                                etEng.getText().toString(),
                                etChi.getText().toString(),
                                etTwn.getText().toString()
                        );
                        mEntryArrayList.set(posInList, newEntry);
                        elAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }})
                .setNegativeButton(R.string.edit_cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.edit_delete, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEntryArrayList.remove(posInList);
                        elAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Read in json file
        mEntryArrayList = HappyDict.loadEntries(this,fileName);
        elAdapter = new EntryListAdapter(this,mEntryArrayList);
        lvEntry.setAdapter(elAdapter);
        lvEntry.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editAlertDialog(mEntryArrayList.get(position),position);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        HappyDict.saveEntries(this, fileName, mEntryArrayList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
    }
}
