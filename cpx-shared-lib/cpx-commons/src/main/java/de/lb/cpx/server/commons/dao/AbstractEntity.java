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
package de.lb.cpx.server.commons.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractEntity implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    public long id;
    protected long version = 0L;
    protected Date creationDate;
    protected Long creationUser;
    protected Date modificationDate;
    protected Long modificationUser;
    private static final String[] IGNORE_BASIC_FIELDS = {"id", "version", "creationDate", "creationUser", "modificationDate", "modificationUser", "class", "tableName"};
    private static final Logger LOG = Logger.getLogger(AbstractEntity.class.getName());
    protected String[] ignoredFields = null;

    /**
     * Vergleicht Zwei Entity - Sets. Die Entities werden mit der Methode
     * equals2object verglichen, damit nur vorgegebenen Inhalte verglichen
     * werden
     *
     * @param my erste Set
     * @param other andere Set
     * @return Ergebnis des Vergleichs
     */
    public static boolean compareEntitySets(Set<AbstractEntity> my, Set<AbstractEntity> other) {
        // both null or empty
        if ((my == null || my.isEmpty()) && (other == null || other.isEmpty())) {
            return true;
        }
        // one is null or empty, other not
        if ((my == null || my.isEmpty()) && (other != null && !other.isEmpty())
                || (my != null && !my.isEmpty()) && (other == null || other.isEmpty())) {
            return false;
        }
        // different size
        //2016-08-04 DNi: my and other can be null here!
        if (my == null && other == null) {
            LOG.log(Level.SEVERE, "my and other is null!");
            return true;
        }
        if (my == null) {
            LOG.log(Level.SEVERE, "my is null!");
            return false;
        }
        if (other == null) {
            LOG.log(Level.SEVERE, "other is null!");
            return false;
        }
        if (my.size() != other.size()) {
            return false;
        }

        ArrayList<AbstractEntity> otherCopy = new ArrayList<>(other);

        Iterator<AbstractEntity> itrMy = my.iterator();

        while (itrMy.hasNext()) {
            Object found = null;
            Object myObj = itrMy.next();
            Iterator<AbstractEntity> itrOther = otherCopy.iterator();
            while (itrOther.hasNext() && found == null) {
                Object otherObj = itrOther.next();
                if (myObj instanceof AbstractEntity && otherObj instanceof AbstractEntity) {
                    if (((AbstractEntity) myObj).equals2object((AbstractEntity) otherObj)) {
                        found = otherObj;
                    }
                } else if (myObj.equals(otherObj)) {
                    found = otherObj;
                }
            }
            if (found != null) {
                otherCopy.remove(found);
            }

        }

        return otherCopy.isEmpty();

    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", length = 7)
    public Date getCreationDate() {
        return creationDate == null ? null : new Date(creationDate.getTime());
    }

    @Column(name = "CREATION_USER", precision = 10, scale = 0, nullable = true)
    public Long getCreationUser() {
        return this.creationUser;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFICATION_DATE", length = 7)
    public Date getModificationDate() {
        return modificationDate == null ? null : new Date(modificationDate.getTime());
    }

    @Column(name = "MODIFICATION_USER", precision = 10, scale = 0, nullable = true)
    public Long getModificationUser() {
        return this.modificationUser;
    }

    @Column(name = "VERSION" /*, columnDefinition="integer default 0"*/, precision = 10, scale = 0, nullable = false)
    @ColumnDefault("0")
//    @Version
    public long getVersion() {
        return this.version;
    }

    /**
     * Setzen des aktuellen Anwenders und Datums beim Erstellen eines Objekctes
     * durch Verwendung der @PrePersist Annoation.
     */
    @PrePersist
    protected void prePersist() {
        //creationUser = "aktueller Anwender";
        //Awi-20170705:Disable temporary, creation date is not set in all entities. Causes errors in CaseSimulation
        if(creationDate == null){
            creationDate = new Date();
        }
        modificationDate = new Date();
    }

    /**
     * Setzen des aktuellen Anwenders und Datums beim Aendern eines Objekctes
     * durch Verwendung der @PreUpdate Annoation.
     */
    @PreUpdate
    protected void preUpdate() {
        //modificationUser = "aktueller Anwender";
        modificationDate = new Date();
    }

    /*
    @PrePersist
    public void prePersist() {
        if (version == null) {
          version = 0L;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (version == null) {
          version = 0L;
        }
    }
     */
    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate == null ? null : new Date(creationDate.getTime());
    }

    public void setCreationUser(final Long creationUserId) {
        this.creationUser = (creationUserId != null && creationUserId.equals(0L)) ? null : creationUserId;
    }

    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate == null ? null : new Date(modificationDate.getTime());
    }

    public void setModificationUser(final Long modificationUserId) {
        this.modificationUser = (modificationUserId != null && modificationUserId.equals(0L)) ? null : modificationUserId;
    }

    public void setVersion(final Long version) {
        this.version = (version == null ? 0L : version);
    }

    /**
     *
     * @return List of fields for comparison of the entities content
     */
    @Transient
    protected List<String> getIgnoredFields() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(IGNORE_BASIC_FIELDS));
        if (ignoredFields != null) {
            list.addAll(Arrays.asList(ignoredFields));
        }
        return list;
    }

    @Transient
    public String getTableName() {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) {
            return "NO_TABLE_ANNOTATION_FOUND";
        }
        String tableName = table.name();
        tableName = (tableName == null) ? "" : tableName.trim();
        return tableName;
    }

    @Transient
    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    /**
     * fcomparison of the entities content
     *
     * @param obj Entity to compare
     * @return result
     */
    public boolean equals2object(AbstractEntity obj) {

        try {
            boolean result = true;
            List<String> ignore = getIgnoredFields();
            Map<String, Object> other = PropertyUtils.describe(obj);
            Map<String, Object> my = PropertyUtils.describe(this);
            Set<String> keys = my.keySet();
            Iterator<String> itr = keys.iterator();
            while (itr.hasNext() && result) {
                String key = itr.next();
                if (ignore.contains(key)) {
                    continue;

                }
                Object myObj = my.get(key);
                Object otherObj = other.get(key);
                if (myObj == null && otherObj == null) {
                    continue;
                }
                if ((myObj == null || myObj instanceof Set) && (otherObj == null || otherObj instanceof Set)) {
                    result = result && compareEntitySets((Set<AbstractEntity>) myObj, (Set<AbstractEntity>) otherObj);
                } else if (myObj instanceof AbstractEntity) {
                    result = result && ((AbstractEntity) myObj).equals2object((AbstractEntity) otherObj);
                } else {
                    result = result && ((myObj != null && otherObj != null && myObj.equals(otherObj)
                            || myObj == null && otherObj == null));
                }
                if (!result) {
                    LOG.finer("key = " + key + " for class " + (myObj == null ? (otherObj == null ? "null" : otherObj.getClass().getName()) : myObj.getClass().getName()));
                }
            }

            return result;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = id != 0 ? 3 * hash + (int) (this.id ^ (this.id >>> 32)) : 0;
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
            LOG.log(Level.FINE, "this is of type " + getClass().getName() + ", but passed object is of type " + obj.getClass().getName() + " => equals is false!");
            return false;
        }
        final AbstractEntity other = (AbstractEntity) obj;

        return id != 0 && id == other.id;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" + "id=" + id + ", version=" + version + ", creationDate=" + creationDate + ", creationUser=" + creationUser + ", modificationDate=" + modificationDate + ", modificationUser=" + modificationUser + '}';
    }

    @Transient
    public Date getDate() {
        Date modDate = getModificationDate();
        if (modDate != null) {
            return modDate;
        }
        return getCreationDate();
    }

    public static <T> List<List<T>> createBlocks(final Set<T> pList, final int pBlocksize) {
        return createBlocks(pList == null ? null : new ArrayList<>(pList), pBlocksize);
    }

    public static <T> List<List<T>> createBlocks(final List<T> pList, final int pBlocksize) {
        if (pList == null) {
            return null;
        }
        if (pBlocksize <= 0) {
            throw new IllegalArgumentException("block size has to be > 0!");
        }
        Iterator<T> it = pList.iterator();
        final List<List<T>> result = new ArrayList<>();
        List<T> lst = null;
        while (it.hasNext()) {
            if (lst == null || lst.size() == pBlocksize) {
                //blocksize = getRandomNumber(50, 200);
                lst = new ArrayList<>(pBlocksize);
                result.add(lst);
            }
            lst.add(it.next());
        }
        return result;
    }

}
