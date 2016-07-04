package chiarahsieh.skypieah.happytranslator;

import android.app.backup.BackupAgent;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ChiaraHsieh on 2016/7/1.
 */
public class HappyBackupAgent extends BackupAgentHelper {
    static final private String KEY_PREFIX="dictBackup";

    public void requestBackup() {
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }

    @Override
    public void onCreate() {
        Log.i("BackupAgent","oncreate() backing up "+HappyDict.name);
        FileBackupHelper helper = new FileBackupHelper(this, HappyDict.name);
        addHelper(KEY_PREFIX,helper);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        Log.i("BackupAgent","onBackUp");
        synchronized(SearchActivity.sDataLock) {
            super.onBackup(oldState, data, newState);
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        Log.i("BackupAgent","onRestore");
        synchronized(SearchActivity.sDataLock) {
            super.onRestore(data, appVersionCode, newState);
        }
    }
}
