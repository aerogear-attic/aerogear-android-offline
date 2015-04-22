/*
 * Copyright 2015 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.offline.storage;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.offline.Storage;
import org.jboss.aerogear.android.pipe.rest.RestAdapter;

/**
 * This stores files to the  internal directory for an app on Android.
 */
public class DefaultLocalStorage implements Storage {
    
    private final Context appContext;
    private final File internalDirectory;
    private final SharedPreferences sharedPreferences;
    
    /**
     * Creates an instance using the appContext and creates any necessary 
     * directories if they are missing.
     * 
     * @param appContext the app's application context
     */
    public DefaultLocalStorage(Context appContext) {
        this.appContext = appContext;
        internalDirectory = appContext.getFilesDir();
        sharedPreferences = appContext.getSharedPreferences(internalDirectory.getName(), Context.MODE_PRIVATE);
    }

    @Override
    public void get(final URL url,final Callback<File> saveCallback) {
        
        RestAdapter.THREAD_POOL_EXECUTOR.execute(new Runnable() {

            @Override
            public void run() {
                String urlKey = url.toString();
                if (sharedPreferences.contains(urlKey)) {
                    String fileUri = sharedPreferences.getString(urlKey, "");
                    if (fileUri.isEmpty()) {
                        saveCallback.onFailure(new IllegalStateException(urlKey + " has an empty file."));
                    }
                    saveCallback.onSuccess(new File(fileUri));
                } else {
                    try {
                        
                        File outfile = new File(internalDirectory.getAbsolutePath(), UUID.randomUUID().toString());
                        outfile.createNewFile();
                        FileOutputStream stream = new FileOutputStream(outfile);
                        InputStream inputStream = url.openConnection().getInputStream();
                        int read;
                        while ((read = inputStream.read()) != -1) {
                            stream.write(read);
                        }
                        
                        stream.close();
                        sharedPreferences.edit().putString(urlKey, outfile.getAbsolutePath()).commit();
                        saveCallback.onSuccess(outfile);
                    } catch (Exception e) {
                        saveCallback.onFailure(e);
                    }
                }
            }
        });
    }
    
    
    
}
