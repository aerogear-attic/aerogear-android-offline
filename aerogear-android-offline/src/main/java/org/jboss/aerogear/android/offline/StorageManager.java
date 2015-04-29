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
package org.jboss.aerogear.android.offline;

import org.jboss.aerogear.android.core.ConfigurationProvider;
import org.jboss.aerogear.android.offline.internal.InternalStorageConfiguration;
import org.jboss.aerogear.android.offline.internal.InternalStorageConfigurationProvider;

import java.util.HashMap;
import java.util.Map;

public class StorageManager {

    private static Map<String, Storage> storages = new HashMap<String, Storage>();

    private static Map<Class<? extends StorageConfiguration<?>>, ConfigurationProvider<?>> configurationProviderMap =
            new HashMap<Class<? extends StorageConfiguration<?>>, ConfigurationProvider<?>>();

    private static OnStorageCreatedListener onStorageCreatedListener = new OnStorageCreatedListener() {
        @Override
        public void onStorageCreated(StorageConfiguration<?> configuration, Storage storage) {
            storages.put(configuration.getName(), storage);
        }
    };

    static {
        StorageManager.registerConfigurationProvider(InternalStorageConfiguration.class,
                new InternalStorageConfigurationProvider());
    }

    private StorageManager() {
    }

    public static <CONFIGURATION extends StorageConfiguration<CONFIGURATION>> void registerConfigurationProvider
            (Class<CONFIGURATION> configurationClass, ConfigurationProvider<CONFIGURATION> provider) {
        configurationProviderMap.put(configurationClass, provider);
    }

    public static <CFG extends StorageConfiguration<CFG>> CFG config(String name, Class<CFG> storageImplementationClass) {

        @SuppressWarnings("unchecked")
        ConfigurationProvider<? extends StorageConfiguration<CFG>> provider =
                (ConfigurationProvider<? extends StorageConfiguration<CFG>>)
                configurationProviderMap.get(storageImplementationClass);

        if (provider == null) {
            throw new IllegalArgumentException("Configuration not registered!");
        }

        return provider.newConfiguration()
                .setName(name)
                .addOnStorageCreatedListener(onStorageCreatedListener);

    }

    public static Storage getStorage(String name) {
        return storages.get(name);
    }

}
