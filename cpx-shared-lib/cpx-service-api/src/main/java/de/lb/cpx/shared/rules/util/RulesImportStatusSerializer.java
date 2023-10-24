/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  sklarow - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.rules.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author sklarow
 */
final class RulesImportStatusSerializer implements JsonSerializer<RulesImportStatus> {

    @Override
    public JsonElement serialize(RulesImportStatus rulesImportStatus, Type interfaceType, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("endImportStatus", context.serialize(rulesImportStatus.getEndImportStatus(), rulesImportStatus.getEndImportStatus().getClass()));
        jsonObject.add("importStatus", context.serialize(rulesImportStatus.getImportStatus(), rulesImportStatus.getImportStatus().getClass()));
        return jsonObject;
    }
}
