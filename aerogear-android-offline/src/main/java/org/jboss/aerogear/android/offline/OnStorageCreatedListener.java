package org.jboss.aerogear.android.offline;

public interface OnStorageCreatedListener {

    void onStorageCreated(StorageConfiguration<?> configuration, Storage store);

}
