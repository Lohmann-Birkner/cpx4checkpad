/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.server.commonDB.model.CSearchList;
import de.lb.cpx.service.properties.SearchListProperties;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

/**
 * Data access object for domain model class CDoctor. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class CSearchListDao extends AbstractCommonDao<CSearchList> {

    private static final Logger LOG = Logger.getLogger(CSearchListDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CSearchListDao() {
        super(CSearchList.class);
    }

    public SearchListProperties getSearchListProperties(final Long pSearchListId) {
        if (pSearchListId == null || pSearchListId.equals(0L)) {
            return null;
        }

        String query;
        if (isOracle()) {
            query = "SELECT ID, SL_NAME, SL_TYPE, NVL2(SL_PROPERTIES, (SL_PROPERTIES).getClobVal(), NULL) SL_PROPERTIES FROM C_SEARCHLIST WHERE C_SEARCHLIST.ID = ? ";
        } else {
            //Microsoft SQL Server
            query = "SELECT ID, SL_NAME, SL_TYPE, CAST(SL_PROPERTIES AS VARCHAR(MAX)) SL_PROPERTIES FROM C_SEARCHLIST WHERE C_SEARCHLIST.ID = ? ";
        }

        Query qry = getEntityManager().createNativeQuery(query);
        qry.setParameter(1, pSearchListId);
        final Object o = qry.getSingleResult();
        if (o == null) {
            return null;
        }
        final Object[] obj = (Object[]) o;
//        if (obj.length < 3 || obj[2] == null) {
//            return null;
//        }
        String text = "";
        final Long id = ((Number) obj[0]).longValue();
        final String name = ((String) obj[1]);
        final String listType = ((String) obj[2]);
        if (obj[3] != null) {
            if (obj[3] instanceof String) {
                text = (String) obj[3];
            } else {
                try {
                    InputStream in = ((java.sql.Clob) obj[3]).getAsciiStream();
                    try ( Reader read = new InputStreamReader(in, CpxSystemProperties.DEFAULT_ENCODING)) {
                        StringWriter write = new StringWriter();
                        int c;
                        while ((c = read.read()) != -1) {
                            write.write(c);
                        }
                        write.flush();
                        text = write.toString();
                    }
                } catch (SQLException | IOException ex) {
                    LOG.log(Level.SEVERE, "Cannot read search list properties '" + name + "' with id " + id, ex);
                }
            }
        }
        SearchListProperties props;
        if (text == null || text.trim().isEmpty()) {
            props = new SearchListProperties();
        } else {
            props = SearchListProperties.deserialize(text);
        }
        if (props != null) {
            props.setId(id);
            props.setName(name);
            props.setList(listType);
        }
        return props;
    }

    public boolean setSearchListProperties(final Long pSearchListId, final SearchListProperties pSearchListProperties) {
        final String props = (pSearchListProperties == null) ? "" : pSearchListProperties.serialize();
        final String connUrl = getConnectionUrl();

        ReturningWork<Boolean> work = new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query = "";
                if (isOracle(connUrl)) {
                    query = "UPDATE C_SEARCHLIST SET SL_PROPERTIES = XMLTYPE(?) WHERE C_SEARCHLIST.ID = ? ";
                } else {
                    //Microsoft SQL Server
                    query = "UPDATE C_SEARCHLIST SET SL_PROPERTIES = ? WHERE C_SEARCHLIST.ID = ? ";
                }
                try ( PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setClob(1, new StringReader(props));
                    stmt.setLong(2, pSearchListId);
                    stmt.executeUpdate();
                    return true;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

//    public List<CSearchList> findAll(SearchListTypeEn pList) {
//        return findAll(pList, null);
//    }
    public List<CSearchList> findAll(SearchListTypeEn pList, final Long[] pIds) {
//        final List<CSearchList> list;
//        if (pList == null) {
//            list = findAll();
//        } else {
        final StringBuilder sb = new StringBuilder("from " + getEntityName());
        if (pList != null || (pIds != null && pIds.length > 0)) {
            sb.append(" where");
            if (pList != null) {
                sb.append(" slType = :type");
            }
            if (pIds != null && pIds.length > 0) {
                sb.append(" id in (:ids)");
            }
        }
        Query query = getEntityManager().createQuery(sb.toString());
        if (pList != null) {
            query.setParameter("type", pList);
        }
        if (pIds != null && pIds.length > 0) {
            query.setParameter("ids", Arrays.asList(pIds));
        }
        return query.getResultList();
//        }
//        return list;
    }

}
