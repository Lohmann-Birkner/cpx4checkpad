/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import com.google.common.base.Objects;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
public class MenuCacheUsers extends MenuCacheEntryMenuCacheEntity<UserDTO> {

    private static final Logger LOG = Logger.getLogger(MenuCacheUsers.class.getName());

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, UserDTO> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getUsers();
    }

    @Override
    public MenuCacheUsers getCopy() {
        return (MenuCacheUsers) super.getCopy(new MenuCacheUsers());
    }

    /**
     * @param pUserId id of the user
     * @return user name, UName value in CDBUsers
     */
    public String getUserNameForId(long pUserId) {
        UserDTO user = get(pUserId);
        return user != null ? user.getUserName() : "";
    }

    /**
     * @param pUserId id of the user
     * @return user full name value in CDBUsers
     */
    public String getUserFullNameForId(long pUserId) {
        UserDTO user = get(pUserId);
        return user != null ? user.getFullName() : "";
    }

    /**
     * @return get collection of all user names
     * @throws NamingException thrown when bean could not be found
     */
    public Collection<String> getUserNames() throws NamingException {
//        return getUserMap().values();
        Set<String> set = new HashSet<>();
        for (UserDTO dto : values()) {
            set.add(dto.getUserName());
        }
        return set;
    }

    /**
     * client adapation of method CdbUsersDao.getValidMatchForUser
     *
     * @param pUserName UName
     * @param pDate date
     * @return list of valid UName from Date to Date as string
     */
    public Collection<UserDTO> getValidMatchForUser(final String pUserName, final Date pDate) {
        final List<UserDTO> list = values(pDate, MenuCacheOptionsEn.IGNORE_INACTIVE);
        final Iterator<UserDTO> it = list.iterator();
        final String userName = StringUtils.trimToEmpty(pUserName);
        while (it.hasNext()) {
            UserDTO item = it.next();
            if (!userName.isEmpty() && !StringUtils.containsIgnoreCase(item.getUserName(), userName)) {
                it.remove();
            }
        }
        Collections.sort(list);
        return list;
    }

    /**
     * @return map of entries for all valid user names, convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, UserDTO>> getValidUserMapEntries() {
        Set<Map.Entry<Long, UserDTO>> userSet = entrySet();
        userSet.removeIf(new Predicate<Map.Entry<Long, UserDTO>>() {
            @Override
            public boolean test(Map.Entry<Long, UserDTO> t) {
                if (t.getValue().isDeleted()) {
                    return true;
                }
                if (!t.getValue().isValid()) {
                    return true;
                }
                return false;
            }
        });
        return userSet;
    }

    public UserDTO getUserMapEntryForId(Long pUserId) {
        if (pUserId == null) {
            return null;
        }
        for (Long entry : keySet()) {
            if (Objects.equal(entry, pUserId)) {
                return get(entry);
            }
        }
        LOG.log(Level.WARNING, "User with id {0} does not exist!", pUserId);
        return null;
    }

    /**
     * @return map of entries for all user names, convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, String>> getUserNamesEntries() {
        Set<Map.Entry<Long, String>> set = new HashSet<>();
        //yes yes i know, not so nice, but i do not want to break implementation for other maps 
        for (UserDTO dto : values()) {
            set.add(new Map.Entry<Long, String>() {
                @Override
                public Long getKey() {
                    return dto.getId();
                }

                @Override
                public String getValue() {
                    return dto.getName(); //dto.getUserName() + (dto.isInActive() ? " - Inaktiv" : "");
                }

                @Override
                public String setValue(String value) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String toString() {
                    //don't change, (internal) id is passed to search service through this method!
                    return String.valueOf(getKey());
                }
            });
        }
        return set;
    }

    /**
     * @return map of entries for all user names that are considered as valid,
     * calculated by valid and deleted flag in CDBUsers , convince methode for
     * CheckComboBoxes in Menu creates new Map.Entry Objects by hand to not
     * break implementation
     */
    public Set<Map.Entry<Long, String>> getValidUserNamesEntries() {
        Set<Map.Entry<Long, String>> set = new LinkedHashSet<>(); //use LinkedHashSet to remain sort order
        //yes yes i know, not so nice, but i do not want to break implementation for other maps 
        //and i know its copied and pasted
        for (UserDTO dto : values()) {
            if (dto.isDeleted()) {
                continue;
            }
            if (!dto.isValid()) {
                continue;
            }
            set.add(new Map.Entry<Long, String>() {
                @Override
                public Long getKey() {
                    return dto.getId();
                }

                @Override
                public String getValue() {
                    return dto.getName(); //dto.getUserName();
                }

                @Override
                public String setValue(String value) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String toString() {
                    return String.valueOf(getKey());
                }
            });
        }
        return set;
    }

    /**
     * @param pName name of the user
     * @return user id by user name, null if name is not found in map
     * @throws NamingException naming exception (ejb corrupted?!)
     */
    public Long getUserId(String pName) throws NamingException {
        if (pName == null) {
            return null;
        }
        Iterator<Long> it = keySet().iterator();
        while (it.hasNext()) {
            Long next = it.next();
            if (pName.equals(get(next).getUserName())) {
                return next;
            }
        }
        return null;
    }

    @Override
    public String getName(final Long pKey) {
        UserDTO obj = get(pKey);
        return obj == null ? null : obj.getUserName();
//        if (pKey == null) {
//            return "";
//        }
//        return getUserFullNameForId(pKey);
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.USERS;
    }

}
