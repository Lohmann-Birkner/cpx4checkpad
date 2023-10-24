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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.results;

import com.sap.conn.jco.JCoTable;

public class SapReturnResult {

    private String type = "";
    private String id = "";
    private String number = "";
    private String message = "";
    private String logNo = "";
    private String logMsgNo = "";
    private String messageV1 = "";
    private String messageV2 = "";
    private String messageV3 = "";
    private String messageV4 = "";
    private String parameter = "";
    private String row = "";
    private String field = "";
    private String system = "";

    /**
     *
     */
    public SapReturnResult() {
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the logNo
     */
    public String getLogNo() {
        return logNo;
    }

    /**
     * @param logNo the logNo to set
     */
    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    /**
     * @return the logMsgNo
     */
    public String getLogMsgNo() {
        return logMsgNo;
    }

    /**
     * @param logMsgNo the logMsgNo to set
     */
    public void setLogMsgNo(String logMsgNo) {
        this.logMsgNo = logMsgNo;
    }

    /**
     * @return the messageV1
     */
    public String getMessageV1() {
        return messageV1;
    }

    /**
     * @param messageV1 the messageV1 to set
     */
    public void setMessageV1(String messageV1) {
        this.messageV1 = messageV1;
    }

    /**
     * @return the messageV2
     */
    public String getMessageV2() {
        return messageV2;
    }

    /**
     * @param messageV2 the messageV2 to set
     */
    public void setMessageV2(String messageV2) {
        this.messageV2 = messageV2;
    }

    /**
     * @return the messageV3
     */
    public String getMessageV3() {
        return messageV3;
    }

    /**
     * @param messageV3 the messageV3 to set
     */
    public void setMessageV3(String messageV3) {
        this.messageV3 = messageV3;
    }

    /**
     * @return the messageV4
     */
    public String getMessageV4() {
        return messageV4;
    }

    /**
     * @param messageV4 the messageV4 to set
     */
    public void setMessageV4(String messageV4) {
        this.messageV4 = messageV4;
    }

    /**
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the row
     */
    public String getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(String row) {
        this.row = row;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the system
     */
    public String getSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(String system) {
        this.system = system;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(final JCoTable aTable) {
        if (aTable.getNumColumns() >= 1) {
            setType(aTable.getString(0));
        }
        if (aTable.getNumColumns() >= 2) {
            setId(aTable.getString(1));
        }
        if (aTable.getNumColumns() >= 3) {
            setNumber(aTable.getString(2));
        }
        if (aTable.getNumColumns() >= 4) {
            setMessage(aTable.getString(3));
        }
        if (aTable.getNumColumns() >= 5) {
            setLogNo(aTable.getString(4));
        }
        if (aTable.getNumColumns() >= 6) {
            setLogMsgNo(aTable.getString(5));
        }
        if (aTable.getNumColumns() >= 7) {
            setMessageV1(aTable.getString(6));
        }
        if (aTable.getNumColumns() >= 8) {
            setMessageV2(aTable.getString(7));
        }
        if (aTable.getNumColumns() >= 9) {
            setMessageV3(aTable.getString(8));
        }
        if (aTable.getNumColumns() >= 10) {
            setMessageV4(aTable.getString(9));
        }
        if (aTable.getNumColumns() >= 11) {
            setParameter(aTable.getString(10));
        }
        if (aTable.getNumColumns() >= 12) {
            setRow(aTable.getString(11));
        }
        if (aTable.getNumColumns() >= 13) {
            setField(aTable.getString(12));
        }
        if (aTable.getNumColumns() >= 14) {
            setSystem(aTable.getString(13));
        }
    }

    @Override
    public String toString() {
        return "TYPE >" + getType() + "<\n"
                + "ID >" + getId() + "<\n"
                + "NUMBER >" + getNumber() + "<\n"
                + "MESSAGE >" + getMessage() + "<\n"
                + "LOG_NO >" + getLogNo() + "<\n"
                + "LOG_MSG_NO >" + getLogMsgNo() + "<\n"
                + "MESSAGE_V1 >" + getMessageV1() + "<\n"
                + "MESSAGE_V2 >" + getMessageV2() + "<\n"
                + "MESSAGE_V3 >" + getMessageV3() + "<\n"
                + "MESSAGE_V4 >" + getMessageV4() + "<\n"
                + "PARAMETER >" + getParameter() + "<\n"
                + "ROW >" + getRow() + "<\n"
                + "FIELD >" + getField() + "<\n"
                + "SYSTEM >" + getSystem() + "<\n";
    }
}
