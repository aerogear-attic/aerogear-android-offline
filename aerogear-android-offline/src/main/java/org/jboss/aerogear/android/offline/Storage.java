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
package org.jboss.aerogear.android.offline;

import java.io.File;
import java.net.URL;
import org.jboss.aerogear.android.core.Callback;

/**
 * A Storage instance provides async crud operations.
 *
 * @author summers
 */
public interface Storage {

    /**
     *
     * This should check and see if a file related to the URL is available. If
     * the file is locally stored, the file should be passed to the callback. If
     * the file is not it should be fetched, saved, and returned to the
     * callback.
     *
     * @param url the url to save
     * @param saveCallback the callback to call after the save is completed
     */
    public void get(URL url, Callback<File> saveCallback);
}
