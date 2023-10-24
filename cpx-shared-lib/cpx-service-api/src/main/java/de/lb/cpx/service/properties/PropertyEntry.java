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
package de.lb.cpx.service.properties;

import de.lb.cpx.service.properties.PropertyDoc.Language;
import java.io.Serializable;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author niemeier
 * @param <T> type
 */
public class PropertyEntry<T extends Serializable> implements Serializable, Comparable<PropertyEntry<?>> {

    private static final long serialVersionUID = 1L;
    private static final String MODULE_DELIMITER = ".";
    private static final String PATH_DELIMITER = " > ";

    private final Set<PropertyEntry<?>> entries = new TreeSet<>();
    private PropertyEntry<?> parent;

    private final Class<?> clazz;
    private final transient Field field;
//    private final Property[] annos;
    private final boolean isClass;
    private final String name;
    private final String desc;
    private final T value;
    //public static final Property.Language LANGUAGE = Property.Language.de;
    //private final String valueAsString;
    private final Class<?> type;
    private final String key;
    private final String moduleKey;
    private final String modulePath;
    private final transient Object instance;

//    public RoleEntry(Class<?> pClazz, String pName, String pDesc) {
//        this.clazz = pClazz;
//        this.field = null;
//        this.name = pName == null ? "" : pName.trim();
//        this.desc = pDesc == null ? "" : pDesc.trim();
//        this.value = null;
//        this.valueAsString = "";
//    }
//
//    public RoleEntry(Class<?> pClazz, Field pField, String pName, String pDesc, Object pValue) {
//        this.clazz = pClazz;
//        this.field = pField;
//        this.name = pName == null ? "" : pName.trim();
//        this.desc = pDesc == null ? "" : pDesc.trim();
//        this.value = pValue;
//        this.valueAsString = getValueAsString(pValue);
//    }
    public PropertyEntry(final Object pInstance, final Class<?> pClazz, final Field pField,
            final T pValue, final boolean pIsClass, final Language pLanguage, final PropertyEntry<?> pParent) {
        this.clazz = pClazz;
        this.field = pField;
        this.type = pField == null ? null : pField.getType();
        //final Property[] annos = getAnnotations(pField, pIsClass);
        this.name = findName(pClazz, pField, pIsClass, pLanguage);
        this.desc = findDesc(pClazz, pField, pIsClass, pLanguage);
        //final String language = Language.de;
        //this.name = pField. == null ? "" : pName.trim();
        //this.desc = pDesc == null ? "" : pDesc.trim();
        this.value = pValue;
        this.isClass = pIsClass;
        this.instance = pInstance;
        this.parent = pParent;
        String[] tmp = buildModuleKey(this);
        moduleKey = tmp[0];
        modulePath = tmp[1];
        if (moduleKey.isEmpty()) {
            key = "";
        } else {
            key = moduleKey + (this.field == null ? "" : (MODULE_DELIMITER + this.field.getName()));
        }
        if (this.parent != null) {
            parent.addEntry(this);
        }
        //this.valueAsString = getValueAsString(pValue);
    }

    private static String findName(final Class<?> pClazz, final Field pField, final boolean pIsClass, final Language pLanguage) {
        final PropertyDoc[] annos = getAnnotations(pField, pIsClass);
        PropertyDoc a = getAnnotation(annos, pLanguage);
        if (a != null) {
            return a.name();
        }
        if (pIsClass) {
            return pClazz == null ? "" : pClazz.getSimpleName();
        } else {
            return pField == null ? "" : pField.getName();
        }
//        return name;
    }

    private static String findDesc(final Class<?> pClazz, final Field pField, final boolean pIsClass, final Language pLanguage) {
        final PropertyDoc[] annos = getAnnotations(pField, pIsClass);
        PropertyDoc a = getAnnotation(annos, pLanguage);
        if (a != null) {
            return a.desc();
        }
        return "";
    }

    private static void findLeafEntries(final Set<PropertyEntry<?>> pSet, final PropertyEntry<?> entry) {
        for (PropertyEntry<?> e : entry.getEntries()) {
            if (e.isLeaf()) {
                pSet.add(e);
            } else {
                findLeafEntries(pSet, e);
            }
        }
    }

    private static PropertyDoc getAnnotation(PropertyDoc[] pAnnotations, final Language pLanguage) {
        if (pAnnotations == null || pAnnotations.length == 0) {
            return null;
        }
        for (PropertyDoc a : pAnnotations) {
            if (pLanguage == null || a.lang().equals(pLanguage)) {
                return a;
            }
        }
        return null;
    }

    private static PropertyDoc[] getAnnotations(final Field pField, final boolean pIsClass) {
        if (pField == null) {
            return new PropertyDoc[0];
        }
        PropertyDoc[] a = pField.getAnnotationsByType(PropertyDoc.class);
        if ((a == null || a.length == 0) && pIsClass) {
            a = pField.getType().getAnnotationsByType(PropertyDoc.class);
            if (a == null || a.length == 0) {
                for (AnnotatedType ifc : pField.getType().getAnnotatedInterfaces()) {
                    a = ifc.getAnnotationsByType(PropertyDoc.class);
                    if (a != null && a.length > 0) {
                        break;
                    }
                    if (ifc.getType() instanceof Class<?>) {
                        a = ((Class<?>) ifc.getType()).getAnnotationsByType(PropertyDoc.class);
                        if (a != null && a.length > 0) {
                            break;
                        }
                    }
                }
            }
        }
        return a;
    }

    public Object getInstance() {
        return instance;
    }

    public String getKey() {
        return key;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public String getModulePath() {
        return modulePath;
    }

    public String getPath() {
        return modulePath + PATH_DELIMITER + name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isClass() {
        return isClass;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

//    public boolean isField() {
//        return field != null;
//    }
    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return entries.isEmpty();
    }

    public List<PropertyEntry<?>> getEntries() {
        return new ArrayList<>(entries);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Field getField() {
        return field;
    }

    public T getValue() {
        return value;
    }

    public String getValueAsString() {
        final T val = value;
        if (val == null) {
            return "---";
        }
        if (isBoolean()) {
            return String.valueOf(val);
        }
        if (isStringArray()) {
            return String.join(", ", (CharSequence[]) val);
        }
        if (isMap()) {
            Map<?, ?> map = (Map<?, ?>) val;
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(entry.getKey());
            }
            return sb.toString();
        }
        if (isPrimitiveLongArray()) {
            StringBuilder sb = new StringBuilder();
            for (long value : (long[]) val) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(value);
            }
            return sb.toString();
        }
        if (isLongArray()) {
            StringBuilder sb = new StringBuilder();
            for (Long value : (Long[]) val) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(value);
            }
            return sb.toString();
        }
        if (isPrimitiveIntegerArray()) {
            StringBuilder sb = new StringBuilder();
            for (int value : (int[]) val) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(value);
            }
            return sb.toString();
        }
        if (isIntegerArray()) {
            StringBuilder sb = new StringBuilder();
            for (Integer value : (Integer[]) val) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(value);
            }
            return sb.toString();
        }
        return String.valueOf(val);
//        if (isIntegerArray()) {
//            return String.join(", ", (Integer[]) val);
//        }
    }

//    public String getValueAsString() {
//        return valueAsString;
//    }
    public PropertyEntry<?> getParent() {
        return parent;
    }

    private void addEntry(final PropertyEntry<?> pEntry) {
        if (pEntry == null) {
            return;
        }
        entries.add(pEntry);
        pEntry.parent = this;
    }

    private static String[] buildModuleKey(final PropertyEntry<?> pEntry) {
        List<String> path = new ArrayList<>();
        List<String> name = new ArrayList<>();
        PropertyEntry<?> p = pEntry;
        do {
            if (p.field != null && p.isClass) {
                path.add(p.field.getName());
                name.add(p.name);
            }
            p = p.parent;
        } while (p != null && p != p.parent);

        String[] saPath = new String[path.size()];
        for (int i = path.size() - 1; i >= 0; i--) {
            saPath[path.size() - (i + 1)] = path.get(i);
        }
        final String resultPath = String.join(MODULE_DELIMITER, saPath);

        String[] saName = new String[name.size()];
        for (int i = name.size() - 1; i >= 0; i--) {
            saName[name.size() - (i + 1)] = name.get(i);
        }
        final String resultName = String.join(PATH_DELIMITER, saName);

        return new String[]{resultPath, resultName};
    }

    public PropertyEntry<?> getRoot() {
        PropertyEntry<?> p = this;
        while (p.parent != null) {
            p = p.parent;
        }
        return p;
    }

    public int size() {
        return entries.size();
    }

//    public static String getValueAsString(Object obj) {
//        if (obj == null) {
//            return "";
//        }
//        if (obj instanceof Boolean) {
//            Boolean val = (Boolean) obj;
//            return val ? "ja" : "nein";
//        }
//        if (obj instanceof String[]) {
//            String[] val = (String[]) obj;
//            return String.join(", ", val);
//        }
//        return String.valueOf(obj);
//    }
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(PropertyEntry<?> o) {
        int c = this.moduleKey.compareTo(o.moduleKey);
        if (c != 0) {
            return c;
        }
        return this.name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.key);
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
        final PropertyEntry<?> other = (PropertyEntry<?>) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    public Set<PropertyEntry<?>> getLeafEntries() {
        Set<PropertyEntry<?>> set = new TreeSet<>();
        findLeafEntries(set, this);
        return set;
    }

    public Map<String, PropertyEntry<?>> getAsMap() {
        Map<String, PropertyEntry<?>> map = new LinkedHashMap<>();
        for (PropertyEntry<?> e : getLeafEntries()) {
            map.put(e.getKey(), e);
        }
        return map;
    }

    public void setValue(final Object value) throws IllegalAccessException {
        field.set(instance, value);
    }

    public boolean isStringArray() {
        return type == String[].class;
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(type);
    }

    public boolean isIntegerArray() {
        return type == Integer[].class;
    }

    public boolean isPrimitiveIntegerArray() {
        return type == int[].class;
    }

    public boolean isLongArray() {
        return type == Long[].class || type == long[].class;
    }

    public boolean isPrimitiveLongArray() {
        return type == long[].class;
    }

    public boolean isBoolean() {
        return type == Boolean.class;
    }

    public boolean isPrimitiveBoolean() {
        return type == boolean.class;
    }

    public boolean isString() {
        return type == String.class;
    }

    public boolean isInteger() {
        return type == Integer.class;
    }

    public boolean isPrimitiveInteger() {
        return type == int.class;
    }

    public boolean isLong() {
        return type == Long.class;
    }

    public boolean isPrimitiveLong() {
        return type == long.class;
    }

}
