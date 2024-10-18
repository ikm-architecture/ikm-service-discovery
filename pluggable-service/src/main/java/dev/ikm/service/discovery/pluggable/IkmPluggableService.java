/*
 * Copyright © 2015 Integrated Knowledge Management (support@ikm.dev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.ikm.service.discovery.pluggable;

import dev.ikm.commons.service.discovery.PluggableService;
import dev.ikm.commons.service.discovery.PluginServiceLoader;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * PluggablePluggableServiceLoader is a utility class used to load service providers using the {@link ServiceLoader} mechanism.
 */
public class IkmPluggableService implements PluggableService {
    private final AtomicReference<PluginServiceLoader> serviceLoader = new AtomicReference<>();
    private final ConcurrentHashMap<Class, Object> cachedServices = new ConcurrentHashMap<>();

    @Override
    public <S> ServiceLoader<S> load(Class<S> service) {
        if (serviceLoader.get() == null) {
            if (!IkmPluggableService.class.getModule().canUse(service)) {
                IkmPluggableService.class.getModule().addUses(service);
            }
            return ServiceLoader.load(service);
        }
        try {
            return serviceLoader.get().loader(service);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <S> S first(Class<S> service) {
        return (S) cachedServices.computeIfAbsent(service, serviceClass -> {
            ServiceLoader<?> loader = this.load(service);
            if (loader.stream().count() > 1) {
                throw new IllegalStateException("More than one pluggable service loaded for: " + service);
            }
            return loader.findFirst().get();
        });
     }

    @Override
     public Class<?> forName(String className) throws ClassNotFoundException {
         if (serviceLoader.get() == null) {
             return Class.forName(className);
         }
         return serviceLoader.get().forName(className);
     }

    @Override
    public void setPluginServiceLoader(PluginServiceLoader pluginServiceLoader) {
        serviceLoader.set(pluginServiceLoader);
    }
}
