/*
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
 * Contributors:
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <p style="font-size:1em; color:green;"> . </p> @author Husser
 */
@Entity
@Table(name = "T_WM_PROCESS_T_CASE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"T_WM_PROCESS_ID", "T_CASE_ID"}, name = "Uni_ProcessId_CaseId") //Each case can be assigned only once to a process!
//@UniqueConstraint(columnNames = {"TWMPROCESS_ID", "MAIN_CASE"}), //Each process can only have one main case!
},
        indexes = {
            @Index(name = "IDX_PROCESS_T_CASE4PROCESS_ID", columnList = "T_WM_PROCESS_ID", unique = false),
            @Index(name = "IDX_PROCESS_T_CASE4CASE_ID", columnList = "T_CASE_ID", unique = false),
            @Index(name = "IDX_PROCESS_T_CASE4PROCID_MAIN", columnList = "T_WM_PROCESS_ID, MAIN_CASE_FL", unique = false)
        })
//@Check(constraints = "MAIN_CASE IN (0, 1)")
public class TWmProcessCase extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TWmProcess process;

    private TCase hosCase;
    
    private TCaseDetails kisCaseDetails;

    private boolean mainCase;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_PROCESS_T_CASE_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf ID der Tablle T_WM_PROCESS zurück.
     *
     * @return process
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_PROCESS_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_PROCESS_CASE4PROCESS_ID"))
    public TWmProcess getProcess() {
        return process;
    }

    /**
     * @param process Column T_WM_PROCESS_ID: Verweis auf ID der Tablle
     * T_WF_PROCESS.
     */
    public void setProcess(TWmProcess process) {
        this.process = process;
    }

    /**
     * Gibt Verweis auf ID der Tablle T_CASE zurück.
     *
     * @return hosCase
     */
    @ManyToOne
    @JoinColumn(name = "T_CASE_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_PROCESS_CASE4CASE_ID"))
    public TCase getHosCase() {
        return hosCase;
    }

    /**
     * @param hosCase Column T_CASE_ID :Verweis auf ID der Tablle T_CASE.
     */
    public void setHosCase(TCase hosCase) {
        this.hosCase = hosCase;
//         setKisDetails(hosCase.getCurrentExtern());
    }

    /**
     * Gibt Verweis auf ID der Tablle T_CASE_DETAILS zurück.
     *
     * @return kisCaseDetails
     */
    @ManyToOne
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_PROCESS_CASE4CASEDET_ID"))
    public TCaseDetails getKisDetails() {
        return kisCaseDetails;
    }

    /**
     * @param kisCaseDetails Column T_CASE_DETAILS_ID :Verweis auf ID der Tabelle T_CASE_DETAILS, LOCAL_FL = 0.
     */
    public void setKisDetails(TCaseDetails kisCaseDetails) {
        this.kisCaseDetails = kisCaseDetails;
    }

    /**
     * Gibt MainProcessCase true=1,False=0 zurück.
     *
     * @return mainCase
     */
    @Column(name = "MAIN_CASE_FL", nullable = false)
    public boolean getMainCase() {
        return mainCase;
    }

    /**
     * @param mainCase Column MAIN_CASE_FL: MainProcessCase true=1,False=0.
     */
    public void setMainCase(boolean mainCase) {
        this.mainCase = mainCase;
    }

}
