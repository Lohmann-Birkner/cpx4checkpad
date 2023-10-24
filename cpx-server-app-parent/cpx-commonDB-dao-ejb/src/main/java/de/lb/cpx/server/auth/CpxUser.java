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
 *    2016  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.auth;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.security.auth.login.LoginException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CpxUser extends CpxAbstractClient {

    private CdbUsers mCdbUser = null;
    private List<CdbUserRoles> mCdbUserRoles = null;
    private CdbUserRoles mActualRole = null;
    private Map<Long, RoleProperties> mProperties = null;
    private CpxSystemPropertiesInterface cpxSystemProperties = null;
    private AppTypeEn mAppType = null;

    public CpxUser() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(final Class<T> clazz) {
        BeanManager beanManager = CDI.current().getBeanManager();
        final Iterator<Bean<?>> iter = beanManager.getBeans(clazz).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type " + clazz.getName());
        }
        final Bean<T> bean = (Bean<T>) iter.next();
        final CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, clazz, ctx);
    }

    protected static Document stringToDocument(String pDocument) throws ParserConfigurationException, SAXException, IOException {
        Document lDocument = null;
        if (pDocument != null && !pDocument.trim().isEmpty()) {
            DocumentBuilderFactory lFactory = DocumentBuilderFactory.newInstance();
            lFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            lFactory.setNamespaceAware(true);
            DocumentBuilder lDocumentBuilder = lFactory.newDocumentBuilder();

            lDocument = lDocumentBuilder.parse(new ByteArrayInputStream(pDocument.getBytes(CpxSystemProperties.DEFAULT_ENCODING)));
        }
        return lDocument;
    }

    public CpxUser init(final CdbUsers pCdbUser, final List<CdbUserRoles> pCdbUserRoles, final String pClientId, final AppTypeEn pAppTypeEn) throws LoginException {
        mLoginDate = new GregorianCalendar();
        mCdbUser = pCdbUser;
        mClientId = pClientId;
        mAppType = pAppTypeEn;
        this.setUserRoles(pCdbUserRoles);
        loadRolePropertyMap();
        return this;
    }

    private void setUserRoles(List<CdbUserRoles> pCdbUserRoles) {
        Collections.sort(pCdbUserRoles, new Comparator<CdbUserRoles>() {
            @Override
            public int compare(CdbUserRoles o1, CdbUserRoles o2) {
                String name1 = (o1 == null || o1.getCdburName() == null) ? "" : o1.getCdburName().toLowerCase();
                String name2 = (o2 == null || o2.getCdburName() == null) ? "" : o2.getCdburName().toLowerCase();
                return name1.compareTo(name2);
            }
        });

        mCdbUserRoles = new ArrayList<>(pCdbUserRoles);
    }

    public CdbUsers getUser() {
        if (mCdbUser == null) {
            mCdbUser = new CdbUsers();
        }
        return mCdbUser;
    }

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getUserName() {
        String lName = getUser().getUName();
        lName = (lName == null) ? "" : lName;
        return lName;
    }

    @Override
    public long getUserId() {
        long lId = getUser().getId();
        return lId;
    }

    public List<CdbUserRoles> getRoles() {
        if (mCdbUserRoles == null) {
            mCdbUserRoles = new ArrayList<>();
        }
        return new ArrayList<>(mCdbUserRoles);
    }

    /*
  public List<String> getRolesName() {
    List<String> lRoleNames = new LinkedList<>();
    for(CdbUserRoles lRole: getRoles()) {
      lRoleNames.add(lRole.getCdburName());
    }
    Collections.sort(lRoleNames);
    return lRoleNames;
  }
     */
    public Map<Long, String> getRoleNames() {
        Map<Long, String> lMap = new LinkedHashMap<>();
        for (CdbUserRoles lCdbUserRoles : getRoles()) {
            String lName = lCdbUserRoles.getCdburName();
            lName = (lName == null) ? "" : lName.trim();
            lMap.put(lCdbUserRoles.getId(), lName);
        }
        return lMap;
    }

    public CdbUserRoles getActualRole() {
        if (mActualRole == null) {
            CdbUsersDao cdbUsersDao = lookup(CdbUsersDao.class);
            Long roleId = cdbUsersDao.getActualRole(getUserId());
            setActualRole(roleId);
            /*
      if (roleId != null && mCdbUserRoles != null) {
        for(CdbUserRoles cdbUserRoles: mCdbUserRoles) {
          if (cdbUserRoles.getId() == roleId) {
            mActualRole = cdbUserRoles;
            break;
          }
        }
      }
             */
        }

        //Return first role if no selected role was found
        if (mActualRole == null) {
            List<CdbUserRoles> roles = getRoles();
            if (!roles.isEmpty()) {
                mActualRole = roles.iterator().next();
                setActualRole(mActualRole.getId());
            }
//            for (CdbUserRoles lCdbUserRoles : getRoles()) {
//                mActualRole = lCdbUserRoles;
//                setActualRole(mActualRole.getId());
//                break;
//            }
        }
        return mActualRole;
    }

    @Override
    public long getActualRoleId() {
        CdbUserRoles cdbUserRoles = getActualRole();
        if (cdbUserRoles == null) {
            return 0L;
        }
        return cdbUserRoles.getId();
    }

    @Override
    public String getActualRoleName() {
        CdbUserRoles cdbUserRoles = getActualRole();
        if (cdbUserRoles != null) {
            return cdbUserRoles.getCdburName();
        }
        return "";
    }

    public boolean setActualRole(final Long pRoleId) {
        long roleId = (pRoleId == null) ? 0L : pRoleId;
        if (roleId == 0L) {
            return false;
        }
        for (CdbUserRoles lCdbUserRoles : getRoles()) {
            if (lCdbUserRoles.getId() == roleId) {
                mActualRole = lCdbUserRoles;
                CdbUsersDao cdbUsersDao = lookup(CdbUsersDao.class);
                cdbUsersDao.setActualRole(getUserId(), roleId);
                return true;
            }
        }
        return false;
    }

    public boolean setActualRole(final String pName) {
        String lName = (pName == null) ? "" : pName.trim();
        Long roleId = null;
        for (CdbUserRoles lCdbUserRoles : getRoles()) {
            String lNameTmp = (lCdbUserRoles.getCdburName() == null) ? "" : lCdbUserRoles.getCdburName().trim();
            if (lNameTmp.equalsIgnoreCase(lName)) {
                roleId = lCdbUserRoles.getId();
                break;
            }
        }
        return setActualRole(roleId);
    }

    public CdbUserRoles getRole(final String pName) {
        String lName = (pName == null) ? "" : pName.trim();
        for (CdbUserRoles lCdbUserRoles : getRoles()) {
            String lNameTmp = (lCdbUserRoles.getCdburName() == null) ? "" : lCdbUserRoles.getCdburName().trim();
            if (lNameTmp.equalsIgnoreCase(lName)) {
                return lCdbUserRoles;
            }
        }
        return null;
    }

    @Override
    public synchronized String getActualDatabase() {
        if ((mDatabase == null || mDatabase.trim().isEmpty()) && (mAppType == null || mAppType.getNeedsDatabase())) {
            CdbUsersDao cdbUsersDao = lookup(CdbUsersDao.class);
            mDatabase = cdbUsersDao.getActualDatabase(getUserId());
        }
        return mDatabase;
    }

    public synchronized boolean setActualDatabase(String pDatabase) {
        CdbUsersDao cdbUsersDao = lookup(CdbUsersDao.class);
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        cdbUsersDao.setActualDatabase(getUserId(), database);
        mDatabase = database;
        //DaoSession.setDatabase(getCurrentCpxUser().getActualDatabase());
        //TODO: Better solution required! Is not session-safe!
        //EntityManagerProducer.database = pDatabase;

        return true;
    }

    public Map<Long, RoleProperties> getPropertyMap() {
        if (mProperties == null) {
            mProperties = new HashMap<>();
        }
        return mProperties;
    }

    public RoleProperties getProperties() {
        CdbUserRoles lCdbUserRoles = getActualRole();
        if (lCdbUserRoles == null) {
            return null;
        }
        return getPropertyMap().get(lCdbUserRoles.getId());
    }

    private void loadRolePropertyMap() throws LoginException {
        CdbUserRolesDao cdbUsersRolesDao = ClientManager.lookup(CdbUserRolesDao.class);
        for (CdbUserRoles lCdbUserRoles : getRoles()) {
            RoleProperties roleProperties = cdbUsersRolesDao.getUserRoleProperties(lCdbUserRoles.getId());
            getPropertyMap().put(lCdbUserRoles.getId(), roleProperties);
        }
    }

    public synchronized RoleProperties getActualRoleProperties() {
        RoleProperties roleProperties = null;
        if (getActualRole() != null) {
            roleProperties = getRoleProperties(getActualRole().getId());
        }
        /*
    if (roleProperties == null) {
      roleProperties = new RoleProperties();
    }
         */
        return roleProperties;
    }

    public synchronized RoleProperties getRoleProperties(final Long pRoleId) {
        long roleId = (pRoleId == null) ? 0L : pRoleId;
        RoleProperties roleProperties;
        if (roleId == 0L) {
            return null;
        }
        CdbUserRolesDao cdbUsersRolesDao = ClientManager.lookup(CdbUserRolesDao.class);
        roleProperties = cdbUsersRolesDao.getUserRoleProperties(pRoleId);
        if (roleProperties == null) {
            roleProperties = new RoleProperties();
        }
        return roleProperties;
    }

    @Override
    public Calendar getLoginDate() {
        if (mLoginDate == null) {
            mLoginDate = new GregorianCalendar();
        }
        return mLoginDate;
    }

    public boolean isUserInRole(String pRoleName) {
        return getRole(pRoleName) != null;
    }

    public void setCpxSystemProperties(final CpxSystemPropertiesInterface pCpxClientSystemProperties) {
        cpxSystemProperties = pCpxClientSystemProperties;
    }

    @Override
    public CpxSystemPropertiesInterface getCpxSystemProperties() {
        return cpxSystemProperties;
    }

    public void setAppType(final AppTypeEn pType) {
        mAppType = pType;
    }

    @Override
    public AppTypeEn getAppType() {
        return mAppType;
    }

    @Override
    public String toString() {
        return String.valueOf(mCdbUser);
    }

    @Override
    public boolean isJob() {
        return false;
    }

    public boolean isValid() {
        return mCdbUser != null && mCdbUser.isValid();
    }

}
