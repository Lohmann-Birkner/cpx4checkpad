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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wilde
 * @param <T> message type
 */
public class JsonErrorMessageBuilder<T extends JsonErrorMessage> {


    protected T message;
    private final List<T> messages = new ArrayList<>();
    
    public JsonErrorMessageBuilder(T pMessage){
        add(pMessage);
    }
        
    public JsonErrorMessageBuilder<T> setType(String pType){
        message.setType(pType);
        return this;
    }
    
    public JsonErrorMessageBuilder<T> setSrcYear(int pYear){
        message.setSrcYear(pYear);
        return this;
    }
    
    public JsonErrorMessageBuilder<T> setDestYear(int pYear){
        message.setDestYear(pYear);
        return this;
    }
    
    public JsonErrorMessageBuilder<T> setDescription(String pDescription){
        message.setDescription(pDescription);
        return this;
    }
    
    public JsonErrorMessageBuilder<T> setSeverity(String pSeverity){
        message.setSeverity(pSeverity);
        return this;
    }
    
    public JsonErrorMessageBuilder<T> setReason(MessageReasonEn pReason){
        message.setReason(pReason);
        return this;
    } 
    
    public JsonErrorMessageBuilder<T> add(T pMessage){
        message = pMessage;
        messages.add(message);
        return this;
    }
    
    public JsonErrorMessageBuilder<T> setAll(T... pMessages){
        messages.clear();
        messages.addAll(Arrays.asList(pMessages));
        message = Iterables.getLast(messages, message);
        return this;
    }
    
    public String build() throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(messages);
    }
    
    public byte[] build(String pCharset) throws JsonProcessingException, UnsupportedEncodingException{
        return build().getBytes(pCharset);
    }
    
    public byte[] buildUtf8() throws JsonProcessingException, UnsupportedEncodingException{
        return build().getBytes("UTF-8");
    }
}
