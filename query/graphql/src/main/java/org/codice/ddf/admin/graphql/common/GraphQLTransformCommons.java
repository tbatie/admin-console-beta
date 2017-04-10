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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codice.ddf.admin.api.action.Action;
import org.codice.ddf.admin.api.action.ActionCreator;
import org.codice.ddf.admin.api.action.ActionReport;
import org.codice.ddf.admin.api.action.Message;
import org.codice.ddf.admin.api.fields.EnumField;
import org.codice.ddf.admin.api.fields.Field;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;

public class GraphQLTransformCommons {

    public static GraphQLObjectType actionCreatorToGraphQLObjectType(ActionCreator creator,
            List<Action> actions) {
        return GraphQLObjectType.newObject()
                .name(creator.typeName())
                .description(creator.description())
                .fields(actionsToGraphQLFieldDef(creator, actions))
                .build();
    }

    public static List<GraphQLFieldDefinition> actionsToGraphQLFieldDef(ActionCreator creator,
            List<Action> actions) {
        List<GraphQLFieldDefinition> fields = new ArrayList<>();

        for (Action action : actions) {
            List<GraphQLArgument> graphQLArgs = new ArrayList<>();

            if (action.getArguments() != null) {
                action.getArguments()
                        .forEach(f -> graphQLArgs.add(fieldToGraphQLArgument((Field) f)));
            }

            fields.add(GraphQLFieldDefinition.newFieldDefinition()
                    .name(action.name())
                    .type(GraphQLTransformOutput.fieldToGraphQLOutputType(action.returnType()))
                    .description(action.description())
                    .argument(graphQLArgs)
                    .dataFetcher(env -> actionFieldDataFetch(env, action.name(), creator))
                    .build());
        }
        return fields;
    }

    public static Object actionFieldDataFetch(DataFetchingEnvironment env, String actionId,
            ActionCreator actionCreator) {
        Map<String, Object> args = new HashMap<>();
        if (env.getArguments() != null) {
            args.putAll(env.getArguments());
        }

        Action action = actionCreator.createAction(actionId);
        action.setArguments(env.getArguments());
        ActionReport report = action.process();
        report.messages().forEach(msg -> ((Message)msg).addSubpath(actionCreator.name()));
        return report;
    }

    public static GraphQLArgument fieldToGraphQLArgument(Field field) {
        return GraphQLArgument.newArgument()
                .name(field.fieldName())
                .description(field.description())
                .type(GraphQLTransformInput.fieldTypeToGraphQLInputType(field))
                .build();
    }

    public static GraphQLEnumType enumFieldToGraphQLEnumType(EnumField field) {
        GraphQLEnumType.Builder builder = GraphQLEnumType.newEnum()
                .name(capitalize(field.fieldTypeName()))
                .description(field.description());

        field.getEnumValues()
                .forEach(val -> builder.value(((Field) val).fieldName(),
                        ((Field) val).getValue(),
                        ((Field) val).description()));
        return builder.build();
    }


    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }
}
