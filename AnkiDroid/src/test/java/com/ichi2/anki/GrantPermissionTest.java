package com.ichi2.anki;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.ichi2.anki.dialogs.DatabaseErrorDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import timber.log.Timber;

@RunWith(AndroidJUnit4.class)
public class GrantPermissionTest extends RobolectricTest {


    @Test
    public void deckPickerShouldHavePermissionAfterFirstGrant() {

        //Start the App for the first time
        //Push OK-Button for the welcome message
        //Give initial permissions (Press allow)
        //Check if DIALOG_LOAD_FAILED will not be shown

        AnkiActivity mock = mock(AnkiActivity.class);
        when(mock.colIsOpen()).thenReturn(false);
        GrantPermissionTest.DeckPickerEx d = super.startActivityNormallyOpenCollectionWithIntent(GrantPermissionTest.DeckPickerEx.class, new Intent());
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);;
        d.onStoragePermissionGranted();
        assertThat("Permission granted and no dialog should be shown", d.mDatabaseErrorDialog, nullValue());

    }


    private static class DeckPickerEx extends DeckPicker {
        private Integer mDatabaseErrorDialog;

        @Override
        public void showDatabaseErrorDialog(int id) {
            this.mDatabaseErrorDialog = id;
        }

        public void onStoragePermissionGranted() {
            onRequestPermissionsResult(DeckPicker.REQUEST_STORAGE_PERMISSION, new String[] { "" }, new int[] { PackageManager.PERMISSION_GRANTED });
        }
    }

}
