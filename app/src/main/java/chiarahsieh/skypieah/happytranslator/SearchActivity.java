package chiarahsieh.skypieah.happytranslator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SearchActivity extends AppCompatActivity {

    static final Object sDataLock = new Object();

    private InputMethodManager inputMethodManager;
    private EditText etSearch;
    private ListView lvEntry;
    private ArrayList<EntryClause> mEntryArrayList;
    private EntryListAdapter elAdapter;

    private static Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        etSearch = (EditText) findViewById(R.id.etSearch);
        lvEntry = (ListView) findViewById(R.id.lvEntry);

        // Add Text Change Listener to EditText
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                Log.d("INPUT", s.toString());
                elAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = etSearch.getRight()
                            - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        etSearch.setText("");
                        return true;
                    } else {
                        if (inputMethodManager != null){
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        }
                    }
                }
                return false;
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
                .setPositiveButton(R.string.add_ok, new DialogInterface.OnClickListener() {

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
                    }
                })
                .setNegativeButton(R.string.add_cancel, new DialogInterface.OnClickListener() {

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
                .setPositiveButton(R.string.edit_ok, new DialogInterface.OnClickListener() {

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
                    }
                })
                .setNegativeButton(R.string.edit_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.edit_delete, new DialogInterface.OnClickListener() {

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
        mEntryArrayList = HappyDict.loadEntries(this);
        elAdapter = new EntryListAdapter(this,mEntryArrayList);
        lvEntry.setAdapter(elAdapter);
        lvEntry.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editAlertDialog(mEntryArrayList.get(position), position);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        HappyDict.saveEntries(this, mEntryArrayList);
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
        Context ctx = getApplicationContext();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_version) {
            Toast.makeText(ctx, getVersionInfo(ctx),Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getVersionInfo(Context context) {
        String strVersion = "Version:";

        PackageInfo packageInfo;
        try {
            packageInfo = context
                    .getPackageManager()
                    .getPackageInfo(
                            getApplicationContext().getPackageName(),
                            0
                    );
            strVersion += packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            strVersion += "Unknown";
        }

        return strVersion;
    }
}
