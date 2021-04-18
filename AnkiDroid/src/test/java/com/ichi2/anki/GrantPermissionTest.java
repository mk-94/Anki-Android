package com.ichi2.anki;

import android.content.Intent;
import android.content.pm.PackageManager;

import com.ichi2.anki.dialogs.DatabaseErrorDialog;
import com.ichi2.testutils.BackendEmulatingOpenConflict;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;

import static org.hamcrest.MatcherAssert.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import timber.log.Timber;

@RunWith(AndroidJUnit4.class)
public class GrantPermissionTest extends RobolectricTest {


    @Test
    public void deckPickerShouldHavePermissionAfterFirstGrant() {
        InitialActivityTest.setupForDefault();
        BackendEmulatingOpenConflict.enable();

        GrantPermissionTest.DeckPickerEx d = super.startActivityNormallyOpenCollectionWithIntent(GrantPermissionTest.DeckPickerEx.class, new Intent());
        //assertThat("Permission granted Database Error Log should not be shown", d.mDatabaseErrorDialog, not(DatabaseErrorDialog.DIALOG_LOAD_FAILED));
        InitialActivityTest.setupForDatabaseConflict();
        d.onStoragePermissionGranted();
        Timber.i("Test %s", d.mDatabaseErrorDialog);
        assertThat("Permission granted Database Error Log should not be shown", d.mDatabaseErrorDialog, not(DatabaseErrorDialog.DIALOG_LOAD_FAILED));

    }


    private static class DeckPickerEx extends DeckPicker {
        private int mDatabaseErrorDialog;

        @Override
        public void showDatabaseErrorDialog(int id) {
            this.mDatabaseErrorDialog = id;
        }

        public void onStoragePermissionGranted() {
            onRequestPermissionsResult(DeckPicker.REQUEST_STORAGE_PERMISSION, new String[] { "" }, new int[] { PackageManager.PERMISSION_GRANTED });
        }
    }

}
