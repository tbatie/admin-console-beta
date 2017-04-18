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
package org.codice.ddf.admin.ldap.actions.commons;

import org.codice.ddf.admin.common.message.ErrorMessage;
import org.codice.ddf.admin.common.message.WarningMessage;

public class LdapMessages {

    // TODO: tbatie - 4/17/17 - Fix all these messages once paths are properly implemented
    public static ErrorMessage cannotConnectError() {
        return new ErrorMessage("CANNOT_CONNECT");
    }

    public static ErrorMessage cannotBindError() {
        return new ErrorMessage("CANNOT_BIND");
    }

    public static ErrorMessage internalError() {
        return new ErrorMessage("INTERNAL_ERROR");
    }

    public static ErrorMessage invalidDnFormatError(String pathOrigin) {
        return  new ErrorMessage("INVALID_DN", pathOrigin);
    }

    public static ErrorMessage invalidQueryError(String pathOrigin) {
        return new ErrorMessage("INVALID_QUERY", pathOrigin);
    }

    public static ErrorMessage serviceDoesNotExistError(String fieldPath) {
        return new ErrorMessage("SERVICE_DOES_NOT_EXIST", fieldPath);
    }

    public static WarningMessage serviceAlreadyExistsWarning(String pathOrigin) {
        return new WarningMessage("IDENTICAL_SERVICE_EXISTS", pathOrigin);
    }

    public static ErrorMessage dnDoesNotExistError(String pathOrigin) {
        return new ErrorMessage("DN_DOES_NOT_EXIST", pathOrigin);
    }

    public static ErrorMessage noUsersInBaseUserDnError(String pathOrigin) {
        return new ErrorMessage("NO_USERS_IN_BASE_USER_DN", pathOrigin);
    }

    public static ErrorMessage noGroupsInBaseGroupDnError(String pathOrigin) {
        return new ErrorMessage("NO_GROUPS_IN_BASE_GROUP_DN", pathOrigin);
    }

    public static WarningMessage noGroupsWithMembersWarning(String pathOrigin) {
        return new WarningMessage("NO_GROUPS_WITH_MEMBERS", pathOrigin);
    }

    public static WarningMessage noReferencedMemberWarning(String pathOrigin) {
        return new WarningMessage("NO_REFERENCED_MEMBER", pathOrigin);
    }

    public static WarningMessage userNameAttributeNotFoundWarning(String pathOrigin) {
        return new WarningMessage("USER_NAME_ATTRIBUTE_NOT_FOUND", pathOrigin);
    }
}
