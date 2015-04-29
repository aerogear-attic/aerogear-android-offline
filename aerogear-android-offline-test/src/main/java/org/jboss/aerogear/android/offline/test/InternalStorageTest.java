/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.offline.test;

import android.content.Context;
import android.util.Log;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.offline.internal.InternalStorage;
import org.jboss.aerogear.android.offline.test.util.PatchedActivityInstrumentationTestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class test Internal Storage.
 */
public class InternalStorageTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    private String TAG = InternalStorageTest.class.getSimpleName();
    private URL filename;

    public InternalStorageTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        Context context = getActivity();
        FileOutputStream fileStream = context.openFileOutput("test", Context.MODE_PRIVATE);
        InputStream sampleInputStream = context.getResources().openRawResource(R.raw.sample_document);
        int read;
        while ((read = sampleInputStream.read()) != -1) {
            fileStream.write(read);
        }
        sampleInputStream.close();
        fileStream.close();
        filename = context.getFileStreamPath("test").toURI().toURL();
    }

    @Override
    protected void tearDown() throws Exception {
        getActivity().deleteFile("test");
    }

    public void testGetFileFromUrl() throws MalformedURLException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<File> fileRef = new AtomicReference<File>();
        new InternalStorage(getActivity()).get(filename, new Callback<File>() {

            @Override
            public void onSuccess(File f) {
                fileRef.set(f);
                latch.countDown();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, exception.getMessage(), exception);
                latch.countDown();
            }
        });

        latch.await(60, TimeUnit.SECONDS);
        assertNotNull(fileRef.get());

    }

}
