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
package org.jboss.aerogear.android.offline.provider;

import java.io.File;
import org.jboss.aerogear.android.offline.PathProvider;

/**
 *
 * @author summers
 */
public class StaticPathProvider implements PathProvider {
    private final File file;

    public StaticPathProvider(String filePath) {
        file = new File(filePath);
    }
    
    @Override
    public File get(Object... in) {
        return file;
    }
    
}
