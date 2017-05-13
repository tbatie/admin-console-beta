/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 **/
package org.codice.ddf.admin.graphql.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codice.ddf.admin.api.DataType;
import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.common.fields.base.BaseFunctionField;
import org.codice.ddf.admin.common.fields.base.BaseObjectField;
import org.codice.ddf.admin.common.fields.base.function.BaseFieldProvider;
import org.codice.ddf.admin.common.fields.base.scalar.BooleanField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;

import com.google.common.collect.ImmutableList;

public class TestClasses {

    public static class TestProvider extends BaseFieldProvider {

        private TestRecursion testRecursion;
        private TestArgWithFunc testArgWithFunc;
        public StringField recurseValue;

        public TestProvider() {
            super("testing", "Test", "Testing purposes only.");
            testRecursion = new TestRecursion(this);
            recurseValue = new StringField("recurseValue");
            testArgWithFunc = new TestArgWithFunc();
            updateInnerFieldPaths();
        }

        @Override
        public List<Field> getDiscoveryFields() {
            return ImmutableList.of(testRecursion, testArgWithFunc, recurseValue);
        }

        @Override
        public List<FunctionField> getMutationFunctions() {
            return new ArrayList<>();
        }
    }

    public static class TestRecursion extends BaseFunctionField<TestProvider> {

        private TestProvider provider;
        private StringField recurseValue;

        public TestRecursion(TestProvider provider) {
            super("testRecursion", "Attempts to return back the query provider.", provider);
            this.provider = provider;
            recurseValue = new StringField();
            updateArgumentPaths();
        }

        @Override
        public TestProvider performFunction() {
            provider.recurseValue.setValue(recurseValue.getValue());
            return provider;
        }

        @Override
        public List<DataType> getArguments() {
            return Arrays.asList(recurseValue);
        }
    }

    public static class TestArgWithFunc extends BaseFunctionField<ObjectWithFunction> {

        private ObjectWithFunction objectWithFunction;

        public TestArgWithFunc() {
            super("testArgWithFunc", "Demonstrates that an input object's funcs are filted out while the same output object contains the funcs.", new ObjectWithFunction());
            objectWithFunction = new ObjectWithFunction();
            updateArgumentPaths();
        }

        @Override
        public List<DataType> getArguments() {
            return ImmutableList.of(objectWithFunction);
        }

        @Override
        public ObjectWithFunction performFunction() {
            return new ObjectWithFunction();
        }
    }

    public static class ObjectWithFunction extends BaseObjectField {

        private BooleanField testPassed;

        public ObjectWithFunction() {
            super("argWithFunc", "ArgWithFunc", "Demonstrates that an input object's funcs are filted out while the same output object contains the funcs.");
            testPassed = new BooleanField();
            testPassed.setValue(true);
            updateInnerFieldPaths();
        }

        @Override
        public List<Field> getFields() {
            return ImmutableList.of(testPassed);
        }
    }

    //Test function in input (arguments)

    //Test recursion in input types

    //Test service references to sources
}
