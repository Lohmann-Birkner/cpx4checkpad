/*
 * Copyright (c) 2020 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wilde
 * @param <T> error class to read
 */
public class JsonErrorMessageReader<T extends JsonErrorMessage> {

    private final Class<T[]> clazz;

    public JsonErrorMessageReader(Class<T[]> pClazz) {
        clazz = pClazz;
    }
    public T readSingleResultOrNull(String pJsonObject) throws IOException{
        List<T> result = read(pJsonObject);
        if(result.isEmpty()|| result.size()>1){
            return null;
        }
        return result.get(0);
    }
    public T readSingleResultOrNull(byte[] pJsonObject,String pCharset) throws IOException{
        if(pJsonObject == null){
            return null;
        }
        pCharset = Objects.requireNonNullElse(pCharset, "UTF-8");
        return readSingleResultOrNull(new String(pJsonObject, pCharset));
    }
    public T readUtf8SingleResultOrNull(byte[] pJsonObject) throws IOException{
        return readSingleResultOrNull(pJsonObject, "UTF-8");
    }
    
    public List<T> read(String pJsonObject) throws IOException{
        return Arrays.asList(new ObjectMapper().readValue(pJsonObject, clazz));
    }
    
    public List<T> read(byte[] pContent, String pCharset) throws UnsupportedEncodingException, IOException{
        if(pContent == null){
            return null;
        }
        pCharset = Objects.requireNonNullElse(pCharset, "UTF-8");
        return read(new String(pContent, pCharset));
    }
    
    public List<T> readUtf8(byte[] pContent) throws UnsupportedEncodingException, IOException{
        return read(pContent, "UTF-8");
    }
    
}
