/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Sequence Entity to access and manipulate last entries for the sequences
 *
 * @author wilde
 */
@Entity
@Table(name = "CDB_SEQUENCE")
@SuppressWarnings("serial")
public class CdbSequence extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String workflowNumberSeq;
//    private String sendingInkaNumberSeq;

    //  should have only one SequenceGenerator for the Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
//    @GenericGenerator(name = "default_gen", strategy = GenerationType.AUTO, parameters = {
//        @Parameter(name = "CDB_SEQUENCE_SQ", value = "CDB_SEQUENCE_SQ"),
//        @Parameter(name = "CURRENT_NO_4_SENDING_INKA_SQ", value = "CURRENT_NO_4_SENDING_INKA_SQ")
//      })
    @SequenceGenerator(name = "default_gen", sequenceName = "CDB_SEQUENCE_SQ", allocationSize = 1)
//    @SequenceGenerator(name = "default_gen", sequenceName = "CURRENT_NO_4_SENDING_INKA_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * get the db id of the sequence
     *
     * @param id db id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * get the last stored workflow number, needed for calculation
     *
     * @return workflowNumberSeq, last value of the Workflow Number Sequence
     */
    @Column(name = "WORKFLOW_NUMBER_SEQ")
    public String getWorkflowNumberSeq() {
        return this.workflowNumberSeq;
    }

    /**
     * sts new workflow sequence number
     *
     * @param WorkflowNumberSeq sets the new value in the sequence
     */
    public void setWorkflowNumberSeq(String WorkflowNumberSeq) {
        this.workflowNumberSeq = WorkflowNumberSeq;
    }

    /**
     * get the last stored Sending Inka sequence Number
     *
     * @return sendingInkaNumberSeq, last value of the Sending Inka sequence
     * number
     */
//    @Column(name = "CURRENT_NO_4_SENDING_INKA_SEQ")
//    public String getSendingInkaNumberSeq() {
//        return this.sendingInkaNumberSeq;
//    }
    /**
     * sets new Sending Inka Number sequence number
     *
     * @param sendingInkaNumberSeq sets the new value in the sequence
     */
//    public void setSendingInkaNumberSeq(String sendingInkaNumberSeq) {
//        this.sendingInkaNumberSeq = sendingInkaNumberSeq;
//    }
}
