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
 *    2019  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.properties;

import de.lb.cpx.service.properties.PropertyDoc.Language;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public abstract class CpxProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CpxProperties.class.getName());

    //@XmlTransient
    private String name;
    //@XmlTransient
    private long id;

    public void setName(final String pName) {
        name = pName == null ? "" : pName.trim();
    }

    public void setId(final Long pId) {
        id = pId == null ? 0L : pId;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Map<String, PropertyEntry<?>> getAsMap() {
        return getEntries().getAsMap();
    }

    private static PropertyEntry<?> getProperties(final Object instance, final PropertyDoc.Language pLanguage) {
        return getProperties(instance, null, instance.getClass(), pLanguage);
    }

    private static PropertyEntry<?> getProperties(final Object instance, final PropertyEntry<?> pEntry, Class<?> pClazz, final PropertyDoc.Language pLanguage) {
        PropertyEntry<?> root = pEntry;
        if (root == null) {
//            Property a = pClazz.getAnnotation(Property.class);
//            final String name;
//            final String desc;
//            if (a != null) {
//                name = a.name();
//                desc = a.desc();
//                //path += a.name();
//            } else {
//                name = pClazz.getSimpleName();
//                desc = "";
//            }
            root = new PropertyEntry<>(instance, pClazz, null, null, true, pLanguage, null);
        }

        //final String delimiter = " -> ";
        final Class<?>[] innerClasses = pClazz.getDeclaredClasses();
//        for (Class<?> clazz : clazzes) {
//            Property a = clazz.getAnnotation(Property.class);
//            //String path = pPath;
//            final String name;
//            final String desc;
//            if (a != null) {
//                name = a.name();
//                desc = a.desc();
//                //path += a.name();
//            } else {
//                name = clazz.getSimpleName();
//                desc = "";
//                //path += clazz.getSimpleName();
//            }
//            final RoleEntry entry = new RoleEntry(clazz, name, desc);
//            if (pEntry == null) {
//                pSet.add(entry);
//            } else {
//                pEntry.addEntry(entry);
//            }
//            Object newInstance = clazz.cast(pProps);
//            getProperties(pProps, entry, pSet, clazz);
//        }
        final Field[] fields = pClazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            boolean isClass = false;
            for (Class<?> clazz : innerClasses) {
                if (field.getType() == clazz) {
                    isClass = true;
                    break;
                }
            }

            //field.setAccessible(true);
//            Property a = getAnnotation(field, isClass);
            field.setAccessible(true);
//            final String name;
//            final String desc;

            final PropertyEntry<?> entry;
            if (!isClass) {
                final Object value;
                Object v = null;
                try {
                    v = field.get(instance);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOG.log(Level.SEVERE, "Cannot read value from field '" + field.getName() + "'", ex);
                }
                value = v;
                //final Class<?> type = field.getType();
                entry = new PropertyEntry<>(instance, pClazz, field, (Serializable) value, isClass, pLanguage, root);
//                if (type == String[].class) {
//                    entry = new RoleEntry<>(pClazz, field, (String[]) value, isClass, pLanguage);
//                } else if (type == Integer[].class) {
//                    entry = new RoleEntry<>(pClazz, field, (Integer[]) value, isClass, pLanguage);
//                } else if (type == Boolean.class) {
//                    entry = new RoleEntry<>(pClazz, field, (Boolean) value, isClass, pLanguage);
//                } else if (type == String.class) {
//                    entry = new RoleEntry<>(pClazz, field, (String) value, isClass, pLanguage);
//                } else if (type == Integer.class) {
//                    entry = new RoleEntry<>(pClazz, field, (Integer) value, isClass, pLanguage);
//                } else if (type == Long.class) {
//                    entry = new RoleEntry<>(pClazz, field, (Long) value, isClass, pLanguage);
//                } else {
//                    LOG.log(Level.SEVERE, "Unknown data type for field '" + field.getName() + "' with value '" + String.valueOf(value) + "'");
//                    entry = new RoleEntry<>(pClazz, field, (Serializable) value, isClass, pLanguage);
//                }
            } else {
                entry = new PropertyEntry<>(instance, pClazz, field, null, isClass, pLanguage, root);
            }
//            if (a != null) {
//                name = a.name();
//                desc = a.desc();
//            } else {
//                name = field.getName();
//                desc = "";
//            }

            //root.addEntry(entry);
            if (isClass) {
                Object newInstance = null;
                try {
                    newInstance = field.get(instance);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOG.log(Level.SEVERE, "Cannot get instance of subclass", ex);
                }
                getProperties(newInstance, entry, field.getType(), pLanguage);
            }
//            if (!Modifier.isPrivate(field.getModifiers())) {
//                continue;
//            }
        }
        return root;
    }

    public PropertyEntry<?> getEntries(Language pLanguage) {
        //RoleEntry entry = new RoleEntry();
        PropertyEntry<?> root = getProperties(this, pLanguage);
        return root;
    }

    public PropertyEntry<?> getEntries() {
        //RoleEntry entry = new RoleEntry();
        PropertyEntry<?> root = getEntries(PropertyDoc.Language.de);
        return root;
    }

    public Set<PropertyEntry<?>> getLeafEntries() {
        return getEntries().getLeafEntries();
    }

    public Set<PropertyEntry<?>> getLeafEntries(Language pLanguage) {
        return getEntries(pLanguage).getLeafEntries();
    }

    @Override
    public String toString() {
        Set<PropertyEntry<?>> entries = getEntries().getLeafEntries();
        StringBuilder sb = new StringBuilder("Properties with name '" + name + "' and id " + id + ":");
        for (PropertyEntry<?> entry : entries) {
            sb.append("\n" + entry.getPath() + ": " + entry.getValueAsString());
        }
        return sb.toString();
    }

}
