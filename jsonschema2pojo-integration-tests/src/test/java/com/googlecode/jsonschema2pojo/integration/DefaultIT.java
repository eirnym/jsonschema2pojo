/**
 * Copyright © 2010-2011 Nokia
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

package com.googlecode.jsonschema2pojo.integration;

import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultIT {

    private static Class<?> classWithDefaults;

    @BeforeClass
    public static void generateAndCompileClass() throws ClassNotFoundException {

        ClassLoader resultsClassLoader = generateAndCompile("/schema/default/default.json", "com.example", true, false, false);

        classWithDefaults = resultsClassLoader.loadClass("com.example.Default");

    }

    @Test
    public void stringPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getStringWithDefault");

        assertThat((String) getter.invoke(instance), is(equalTo("abc")));

    }

    @Test
    public void integerPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getIntegerWithDefault");

        assertThat((Integer) getter.invoke(instance), is(equalTo(1337)));

    }

    @Test
    public void numberPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getNumberWithDefault");

        assertThat((Double) getter.invoke(instance), is(equalTo(Double.valueOf("1.337"))));

    }

    @Test
    public void booleanPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getBooleanWithDefault");

        assertThat((Boolean) getter.invoke(instance), is(equalTo(true)));

    }

    @Test
    public void dateAsMillisecPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getDateWithDefault");

        assertThat((Date) getter.invoke(instance), is(equalTo(new Date(123456789))));

    }

    @Test
    public void dateAsStringPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getDateAsStringWithDefault");

        assertThat((Date) getter.invoke(instance), is(equalTo(new Date(1298539523112L))));

    }

    @Test
    public void utcmillisecPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getUtcmillisecWithDefault");

        assertThat((Long) getter.invoke(instance), is(equalTo(123456789L)));

    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void enumPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {

        Object instance = classWithDefaults.newInstance();

        Class<Enum> enumClass = (Class<Enum>) classWithDefaults.getClassLoader().loadClass("com.example.Default$EnumWithDefault");

        Method getter = classWithDefaults.getMethod("getEnumWithDefault");

        assertThat((Enum) getter.invoke(instance), is(equalTo(enumClass.getEnumConstants()[1])));

    }

    @Test
    public void complexPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getComplexPropertyWithDefault");

        assertThat(getter.invoke(instance), is(nullValue()));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void arrayPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getArrayWithDefault");

        assertThat(getter.invoke(instance), is(instanceOf(List.class)));

        List<String> defaultList = (List<String>) getter.invoke(instance);

        assertThat(defaultList.size(), is(3));
        assertThat(defaultList.get(0), is(equalTo("one")));
        assertThat(defaultList.get(1), is(equalTo("two")));
        assertThat(defaultList.get(2), is(equalTo("three")));

        // list should be mutable
        assertThat(defaultList.add("anotherString"), is(true));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void uniqueArrayPropertyHasCorrectDefaultValue() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getUniqueArrayWithDefault");

        assertThat(getter.invoke(instance), is(instanceOf(Set.class)));

        Set<Integer> defaultSet = (Set<Integer>) getter.invoke(instance);
        Iterator<Integer> defaultSetIterator = defaultSet.iterator();

        assertThat(defaultSet.size(), is(3));
        assertThat(defaultSetIterator.next(), is(equalTo(100)));
        assertThat(defaultSetIterator.next(), is(equalTo(200)));
        assertThat(defaultSetIterator.next(), is(equalTo(300)));

        // set should be mutable
        assertThat(defaultSet.add(400), is(true));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void arrayPropertyWithoutDefaultIsEmptyList() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getArrayWithoutDefault");

        assertThat(getter.invoke(instance), is(instanceOf(List.class)));

        List<String> defaultList = (List<String>) getter.invoke(instance);

        assertThat(defaultList.size(), is(0));

        // list should be mutable
        assertThat(defaultList.add("anotherString"), is(true));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void uniqueArrayPropertyWithoutDefaultIsEmptySet() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object instance = classWithDefaults.newInstance();

        Method getter = classWithDefaults.getMethod("getUniqueArrayWithoutDefault");

        assertThat(getter.invoke(instance), is(instanceOf(Set.class)));

        Set<Boolean> defaultSet = (Set<Boolean>) getter.invoke(instance);

        assertThat(defaultSet.size(), is(0));

        // set should be mutable
        assertThat(defaultSet.add(true), is(true));

    }

}
