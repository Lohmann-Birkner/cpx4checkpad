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
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * CdbUsers initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CDB_USERS : Tabelle der CPX
 * Benutzer </p>
 */
@NamedQueries({
    @NamedQuery(
            name = "findUserByName",
            //query = "from CdbUsers u where u.UName = :name AND u.UPassword = :password"
            query = "from CdbUsers u where lower(u.UName) = lower(:name)" //  AND u.UPassword = :password
    )
})
@Entity
@Table(name = "CDB_USERS")
@SuppressWarnings("serial")
//@SecurityDomain("other")
@PermitAll
public class CdbUsers extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private String UName;
    private String UFirstName;
    private String ULastName;
    private String UPassword;
    private String UPwSalt;
    private String UDatabase;
    private Long URoleId;
    private String UTitle;
    private Date UValidFrom;
    private Date UValidTo;
    private boolean UIsAdmin;
    private boolean UIsValid;
    private boolean UIsDeleted;
    private String UEmailAddresse;
    private String UEmailServer;
    private String UEmailUser;
    private String UPhoneNumber;
    private String UFaxNumber;
    private Set<CdbUser2Role> cdbUser2Roles = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CDB_USERS_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return UName: Benutzername
     */
    @Column(name = "U_NAME", length = 50)
    public String getUName() {
        return this.UName;
    }

    /**
     *
     * @param UName Column U_NAME :Benutzername
     */
    public void setUName(String UName) {
        this.UName = UName;
    }

    /**
     *
     * @return UFirstName:Firstname des Benutzer.
     */
    @Column(name = "U_FIRST_NAME", length = 50)
    public String getUFirstName() {
        return UFirstName;
    }

    /**
     * @param id User ID
     * @return First name of this user
     */
    public String getUserFirstNameById(long id) {
        this.id = id;
        return getUFirstName();
    }

    public void setUFirstName(String UFirstName) {
        this.UFirstName = UFirstName;
    }

    /**
     *
     * @return ULastName:Nachname des Benutzer.
     */
    @Column(name = "U_LAST_NAME", length = 50)
    public String getULastName() {
        return ULastName;
    }

    //2017-04-19 DNi: Syntactic sugar :-)
    /**
     * Returns user name as "family name, first name"
     *
     * @return user name
     */
    @Transient
    public String getUFullName() {
        String firstName = getUFirstName() == null ? "" : getUFirstName().trim();
        String lastName = getULastName() == null ? "" : getULastName().trim();
        //first and lastname are optinal values
        //if no optinal field is set return userName
        if(firstName.isEmpty() && lastName.isEmpty()){
            return getUName();
        }
        if (firstName.isEmpty()) {
            return lastName;
        }
        if (lastName.isEmpty()) {
            return firstName;
        }

        return lastName + ", " + firstName;
    }

    /**
     *
     * @param ULastName Column U_LAST_NAME: Nachname des Benutzer.
     */
    public void setULastName(String ULastName) {
        this.ULastName = ULastName;
    }

    /**
     *
     * @return UPassword : Passwort des Benutzer
     */
    @Column(name = "U_PASSWORD", length = 100)
    public String getUPassword() {
        return this.UPassword;
    }

    /**
     *
     * @param UPassword Column:U_PASSWORD Passwort des Benutzer
     */
    public void setUPassword(String UPassword) {
        this.UPassword = UPassword;
    }

    /**
     *
     * @return UPwSalt: Feld dient zu vergessen des Passwortes.
     */
    @Column(name = "U_PW_SALT", length = 10)
    public String getUPwSalt() {
        return this.UPwSalt;
    }

    /**
     *
     * @param UPwSalt Column U_PW_SALT : Feld dient zu vergessen des Passwortes.
     */
    public void setUPwSalt(String UPwSalt) {
        this.UPwSalt = UPwSalt;
    }

    /**
     *
     * @return UDatabase:Datenbank des Benutzer, die beim Einloggen verwendet
     * wurde.
     */
    @Column(name = "U_DATABASE", length = 50)
    public String getUDatabase() {
        return this.UDatabase;
    }

    /**
     *
     * @param UDatabase Column U_DATABASE :Datenbank des Benutzer, die beim
     * Einloggen verwendet wurde.
     */
    public void setUDatabase(String UDatabase) {
        this.UDatabase = UDatabase;
    }

    /**
     *
     * @return URoleId:Referenz aud die Tabelle CDB_USER_ROLES .
     */
    @Column(name = "U_ROLE_ID", nullable = true)
    public Long getURoleId() {
        return this.URoleId;
    }

    /**
     *
     * @param URoleId Column U_ROLE_ID : Referenz aud die Tabelle CDB_USER_ROLES
     * .
     */
    public void setURoleId(final Long URoleId) {
        this.URoleId = URoleId;
    }

    /**
     *
     * @return UTitle: Titel des Benutzer.
     */
    @Column(name = "U_TITLE", length = 50)
    public String getUTitle() {
        return this.UTitle;
    }

    /**
     *
     * @param UTitle Column U_TITLE : Titel des Benutzer.
     */
    public void setUTitle(String UTitle) {
        this.UTitle = UTitle;
    }

    /**
     *
     * @return UValidFrom : Anfang der Gültigkeit des Benutzer.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "U_VALID_FROM", length = 7)
    public Date getUValidFrom() {
        return UValidFrom == null ? null : new Date(UValidFrom.getTime());
    }

    /**
     *
     * @param UValidFrom Column U_VALID_FROM : Anfang der Gültigkeit des
     * Benutzer.
     */
    public void setUValidFrom(Date UValidFrom) {
        this.UValidFrom = UValidFrom == null ? null : new Date(UValidFrom.getTime());
    }

    /**
     *
     * @return UValidTo : Ende der Gültigkeit des Benutzer.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "U_VALID_TO", length = 7)
    public Date getUValidTo() {
        return UValidTo == null ? null : new Date(UValidTo.getTime());
    }

    /**
     *
     * @param UValidTo Column U_VALID_TO: Ende der Gültigkeit des Benutzer.
     */
    public void setUValidTo(Date UValidTo) {
        this.UValidTo = UValidTo == null ? null : new Date(UValidTo.getTime());
    }

    /**
     *
     * @return UIsAdmin : Flag 1/0, ob der Benutzer Administartor ist.
     */
    @Column(name = "U_IS_ADMIN", nullable = false, precision = 1, scale = 0)
    public boolean isUIsAdmin() {
        return this.UIsAdmin;
    }

    /**
     *
     * @param UIsAdmin Column U_IS_ADMIN : Flag 1/0, ob der Benutzer
     * Administartor ist.
     */
    public void setUIsAdmin(boolean UIsAdmin) {
        this.UIsAdmin = UIsAdmin;
    }

    /**
     *
     * @return UIsValid : Flag 1/0, ob der Benutzer gültig ist.
     */
    @Column(name = "U_IS_VALID", nullable = false, precision = 1, scale = 0)
    public boolean isUIsValid() {
        return UIsValid;
    }

    /**
     *
     * @param UIsValid Column U_IS_VALID : Flag 1/0, ob der Benutzer gültig ist.
     */
    public void setUIsValid(boolean UIsValid) {
        this.UIsValid = UIsValid;
    }

    /**
     *
     * @return UIsDeleted: Flag 1/0, ob der Benutzer gelöscht ist.
     */
    @Column(name = "U_IS_DELETED", nullable = false, precision = 1, scale = 0)
    public boolean isUIsDeleted() {
        return UIsDeleted;
    }

    /**
     *
     * @param UIsDeleted Column U_IS_DELETED: Flag 1/0, ob der Benutzer gelöscht
     * ist.
     */
    public void setUIsDeleted(boolean UIsDeleted) {
        this.UIsDeleted = UIsDeleted;
    }

    /**
     *
     * @return UEmailAddresse: Email-Adresse
     */
    @Column(name = "U_EMAIL_ADDRESSE", length = 50)
    public String getUEmailAddresse() {
        return this.UEmailAddresse;
    }

    /**
     *
     * @param UEmailAddresse Column U_EMAIL_ADDRESSE :Email-Adresse
     */
    public void setUEmailAddresse(String UEmailAddresse) {
        this.UEmailAddresse = UEmailAddresse;
    }

    /**
     *
     * @return UEmailServer: Email-Server
     */
    @Column(name = "U_EMAIL_SERVER", length = 50)
    public String getUEmailServer() {
        return this.UEmailServer;
    }

    /**
     *
     * @param UEmailServer Column U_EMAIL_SERVER : Email-Server
     */
    public void setUEmailServer(String UEmailServer) {
        this.UEmailServer = UEmailServer;
    }

    /**
     *
     * @return UEmailUser: E-Mail von Benutzer
     */
    @Column(name = "U_EMAIL_USER", length = 50)
    public String getUEmailUser() {
        return this.UEmailUser;
    }

    /**
     *
     * @param UEmailUser Column U_EMAIL_USER : E-Mail von Benutzer
     */
    public void setUEmailUser(String UEmailUser) {
        this.UEmailUser = UEmailUser;
    }

    /**
     * Gibt Telefon-Nummer des Anwenders zurück.
     *
     * @return UPhoneNumber
     */
    @Column(name = "U_PHONE_NUMBER", length = 20, nullable = true)
    public String getUPhoneNumber() {
        return UPhoneNumber;
    }

    /**
     *
     * @param UPhoneNumber Column U_PHONE_NUMBER: Telefon-Nummer des Anwenders.
     */
    public void setUPhoneNumber(String UPhoneNumber) {
        this.UPhoneNumber = UPhoneNumber;
    }

    /**
     * Gibt Fax-Nummer des Anwenders zurück.
     *
     * @return UFaxNumber
     */
    @Column(name = "U_FAX_NUMBER", length = 20, nullable = true)
    public String getUFaxNumber() {
        return UFaxNumber;
    }

    /**
     *
     * @param UFaxNumber Column U_FAX_NUMBER: Fax-Nummer des Anwenders.
     */
    public void setUFaxNumber(String UFaxNumber) {
        this.UFaxNumber = UFaxNumber;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cdbUsers")
    public Set<CdbUser2Role> getCdbUser2Roles() {
        return this.cdbUser2Roles;
    }

    public void setCdbUser2Roles(Set<CdbUser2Role> cdbUser2Roles) {
        this.cdbUser2Roles = cdbUser2Roles;
    }

//    @Transient
//    public boolean isValid() {
//        if (!UIsValid) {
//            return false;
//        }
//        final Date lNow = new Date();
//        if (UValidFrom != null && UValidFrom.after(lNow)) {
//            //Rolle noch nicht gültig
//            return false;
//        }
//        if (UValidTo != null && UValidTo.before(lNow)) {
//            //Rolle nicht mehr gültig
//            return false;
//        }
//        return true;
//    }
//    @Override
//    public int compareTo(final CdbUsers o) {
//        return StringUtils.trimToEmpty(this.getUName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getUName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.UName);
        hash = 41 * hash + Objects.hashCode(this.UValidFrom);
        hash = 41 * hash + Objects.hashCode(this.UValidTo);
        hash = 41 * hash + (this.UIsValid ? 1 : 0);
        hash = 41 * hash + (this.UIsDeleted ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CdbUsers other = (CdbUsers) obj;
        if (this.UIsValid != other.UIsValid) {
            return false;
        }
        if (this.UIsDeleted != other.UIsDeleted) {
            return false;
        }
        if (!Objects.equals(this.UName, other.UName)) {
            return false;
        }
        if (!Objects.equals(this.UValidFrom, other.UValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.UValidTo, other.UValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String userId = String.valueOf(this.id);
        final String hash = Integer.toHexString(this.hashCode());
        return "user_" + UName + ";id_" + userId + "@" + hash;
    }

    @Transient
    @Override
    public String getName() {
        return UName;
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return id;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return UValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return UValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return UIsValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return UIsDeleted;
    }

    @Transient
    @Override
    public boolean isInActive() {
        return isInActive(new Date());
    }


    @Override
    public boolean isInActive(final Date pDate) {
        return isDeleted() || !isValid(pDate);
    }

    @Transient
    @Override
    public int getSort() {
        return 0; //does not exist for user!
    }

}
