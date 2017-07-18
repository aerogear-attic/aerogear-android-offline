/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.offline;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.offline.internal.InternalStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * This class test Internal Storage.
 */
@RunWith(AndroidJUnit4.class)
public class InternalStorageTest {

    private String TAG = InternalStorageTest.class.getSimpleName();
    private URL filename;

    @Before
    public void setUp() throws Exception {
        Context context = getContext();
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

    @After
    public void tearDown() throws Exception {
        getContext().deleteFile("test");
    }

    @Test
    public void testGetFileFromUrl() throws MalformedURLException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<File> fileRef = new AtomicReference<File>();
        new InternalStorage(getContext()).get(filename, new Callback<File>() {

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
        Assert.assertNotNull(fileRef.get());

    }

}
