/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.reader.exception;

import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public enum ReaderExceptionTypeEn {
 
    
    NOT_SET(""),
    EXCEL_NOT_FOUND(Lang.getMsExcelNotFound()),
    EXCEL_TOO_OLD(Lang.getMsExcelVersion(), Lang.getIsNotSupported()),
    WORD_NOT_FOUND(Lang.getMsWordNotFound()),
    WORD_TOO_OLD(Lang.getMsWordVersion(), Lang.getIsNotSupported()),
    OUTLOOK_NOT_FOUND(Lang.getMsOutlookNotFound()),
    OUTLOOK_TOO_OLD(Lang.getMsOutlookVersion(), Lang.getIsNotSupported()),
    OFFICE_NOT_FOUND(Lang.getMsOfficeNotFound()),
    OFFICE_TOO_OLD(Lang.getMsOfficeVersion(),  Lang.getIsNotSupported()),
    OFFICE_DISABLED(Lang.getMsOfficeDisabled());

    private static final Logger LOG = Logger.getLogger(ReaderExceptionTypeEn.class.getName());

    public static ReaderExceptionTypeEn check(String message) {
        if(message == null || message.trim().isEmpty()){
            return NOT_SET;
        }
        LOG.log(Level.INFO, "message =" + message);
        ReaderExceptionTypeEn[] vals = values();
        ReaderExceptionTypeEn ret = NOT_SET;
        for(ReaderExceptionTypeEn val: vals){
//            LOG.log(Level.INFO, "val: " + val.toString() + "  mContentFirst:" + val.mContentFirst + " mContentNext:" + val.mContentNext==null?"null":val.mContentNext);
            if(val.mContentNext == null){
                if(message.equals(val.mContentFirst)){
//                    LOG.log(Level.INFO, "return val: " + val.toString() + "  mContentFirst:" + val.mContentFirst + " mContentNext:" + val.mContentNext==null?"null":val.mContentNext);
                   ret = val;
                   break;
                }
            }else{
                if(message.startsWith(val.mContentFirst) && message.contains(val.mContentNext)){
//                    LOG.log(Level.INFO, "return val: " + val.toString() + "  mContentFirst:" + val.mContentFirst + " mContentNext:" + val.mContentNext==null?"null":val.mContentNext);
                    ret = val;
                    break;
                }
            }
        }
        LOG.log(Level.INFO, "ret={0}" , ret.toString());
        return ret;
    }
    
    
    private final String mContentFirst;
    private final String mContentNext;

    
    private ReaderExceptionTypeEn(String pContent){
        mContentFirst = pContent;
        mContentNext = null;
    }
    
    private ReaderExceptionTypeEn(String pContentStart, String pContentNext){
        mContentFirst = pContentStart;
        mContentNext = pContentNext;
    }
    

}
