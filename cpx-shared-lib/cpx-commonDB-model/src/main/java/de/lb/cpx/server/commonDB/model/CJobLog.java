/**
 * Copyright (c) 2016 Lohmann & Birkner.
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
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CAtc initially generated at 03.02.2016 10:32:45 by Hibernate Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_ATC: Tabelle der ATC
 * (Anatomisch-Therapeutisch-Chemisches Klassifikationssystem).</p>
 *
 *
 */
@Entity
@Table(name = "C_JOB_LOG")
@SuppressWarnings("serial")
public class CJobLog extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private String database;
//    private long jobId;
    private String importName;
    private Date startDate;
    private Date endDate;
    private String status = "STARTED";
    private Integer numOfPatients;
    private Integer numOfCases;
    private String message;
    private String inputSource;
    private Integer numOfImportedCases;
    private Integer numOfIgnoredCases;
    private String username;
    private long jobId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_JOB_LOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return database
     */
    @Column(name = "DB", nullable = false, length = 255)
    public String getDatabase() {
        return this.database;
    }

    /**
     * @param database Column DATABASE
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return database
     */
    @Column(name = "IMPORT_NAME", nullable = false, length = 80)
    public String getImportName() {
        return this.importName;
    }

    /**
     * @param importName Column IMPORT_NAME
     */
    public void setImportName(String importName) {
        this.importName = importName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE", nullable = true, length = 7)
    public Date getStartDate() {
        return startDate == null ? null : new Date(startDate.getTime());
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE", nullable = true, length = 7)
    public Date getEndDate() {
        return endDate == null ? null : new Date(endDate.getTime());
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
    }

//    /**
//     * @return jobId
//     */
//    @Column(name = "JOB_ID", nullable = false)
//    public long getJobId() {
//        return jobId;
//    }
//
//    /**
//     * @param jobId ib id
//     */
//    public void setJobId(long jobId) {
//        this.jobId = jobId;
//    }
    /**
     * @return numOfPatients
     */
    @Column(name = "NUM_OF_PATIENTS", nullable = true)
    public Integer getNumOfPatients() {
        return numOfPatients;
    }

    /**
     * @param numOfPatients number of patients
     */
    public void setNumOfPatients(Integer numOfPatients) {
        this.numOfPatients = numOfPatients;
    }

    /**
     * @return numOfCases
     */
    @Column(name = "NUM_OF_CASES", nullable = true)
    public Integer getNumOfCases() {
        return numOfCases;
    }

    /**
     * @param numOfCases number of hospital cases
     */
    public void setNumOfCases(Integer numOfCases) {
        this.numOfCases = numOfCases;
    }

    /**
     * @return status
     */
    @Column(name = "STATUS", nullable = false, length = 20)
    public String getStatus() {
        return status;
    }

    /**
     * @param status last status of import
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    @Lob
    @Column(name = "MESSAGE", length = 6000, nullable = false)
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
     *
     * @return inputSource
     */
    @Column(name = "INPUT_SOURCE", nullable = true, length = 255)
    public String getInputSource() {
        return inputSource;
    }

    /**
     *
     * @return numOfImportedCases
     */
    @Column(name = "NUM_OF_IMPORTED_CASES", nullable = true)
    public Integer getNumOfImportedCases() {
        return numOfImportedCases;
    }

    /**
     *
     * @return numOfIgnoredCases
     */
    @Column(name = "NUM_OF_IGNORED_CASES", nullable = true)
    public Integer getNumOfIgnoredCases() {
        return numOfIgnoredCases;
    }

    /**
     *
     * @return username
     */
    @Column(name = "USER_NAME", nullable = true, length = 100)
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return jobId
     */
    @Column(name = "JOB_ID", precision = 10, scale = 0, nullable = false)
    public long getJobId() {
        return jobId;
    }

    /**
     *
     * @param inputSource the inputSource to set
     */
    public void setInputSource(String inputSource) {
        this.inputSource = inputSource;
    }

    /**
     *
     * @param numOfImportedCases the numOfImportedCases to set
     */
    public void setNumOfImportedCases(Integer numOfImportedCases) {
        this.numOfImportedCases = numOfImportedCases;
    }

    /**
     *
     * @param numOfIgnoredCases the numOfIgnoredCases to set
     */
    public void setNumOfIgnoredCases(Integer numOfIgnoredCases) {
        this.numOfIgnoredCases = numOfIgnoredCases;
    }

    /**
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @param jobId the jobId to set
     */
    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

}
