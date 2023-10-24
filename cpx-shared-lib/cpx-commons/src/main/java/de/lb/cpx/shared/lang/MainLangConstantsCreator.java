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
 *    2016  Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.lang;

import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Dirk Niemeier
 */
@SuppressWarnings("unchecked")
public class MainLangConstantsCreator {

    private static final Logger LOG = Logger.getLogger(MainLangConstantsCreator.class.getName());

    public static synchronized String createLanguageConstants(final List<File> pLocaleFiles) {
        /*
    if (pLocaleFiles == null) {
      return "";
    }
         */
        List<String> out = new LinkedList<>();

        out.add("/* ");
        out.add(" * Copyright (c) 2016 Lohmann & Birkner.");
        out.add(" * NOTICE:  All information contained herein is, and remains");
        out.add(" * the property of Lohmann & Birkner and its suppliers,");
        out.add(" * if any.  The intellectual and technical concepts contained");
        out.add(" * herein are proprietary to Lohmann & Birkner");
        out.add(" * and its suppliers and are protected by trade secret or copyright law.");
        out.add(" * Dissemination of this information or reproduction of this material");
        out.add(" * is strictly forbidden unless prior written permission is obtained");
        out.add(" * from Lohmann & Birkner.");
        out.add(" * http://www.lohmann-birkner.de/de/index.php");
        out.add(" *");
        out.add(" * Contributors:");
        out.add(" *    2016  Niemeier - initial API and implementation and/or initial documentation");
        out.add(" */");
        out.add("");
        out.add("//WARNING: DON'T EDIT THIS FILE MANUALLY, CHANGES WILL BE DISCARDED");
        out.add("//LOOK FOR " + MainLangConstantsCreator.class.getSimpleName() + " CLASS TO APPLY AUTOMATICALLY CHANGES OR EDIT SUPER CLASS " + AbstractLang.class.getSimpleName());
        out.add("package de.lb.cpx.shared.lang;");
        out.add("");
        out.add("import java.util.HashMap;");
        out.add("");
        /*
    out.add("import de.lb.cpx.model.lang.CpxLanguageInterface;");
    out.add("");
         */
        out.add("/**");
        out.add(" *");
        out.add(" * @author Dirk Niemeier");
        out.add(" */");
        out.add("public class Lang extends AbstractLang {");
        /*
    out.add("  protected static CpxLanguageInterface cpxLanguage = null;");
    out.add("");
    out.add("  public static void setCpxLanguage(CpxLanguageInterface pCpxLanguage) {");
    out.add("    cpxLanguage = pCpxLanguage;");
    out.add("  }");
    out.add("");
         */

        Properties props = new Properties();
        //use UTF-8 to decode non-latin characters correctly!
        List<LangConstant> constantList = new LinkedList<>();
        List<LangConstant> constantListAll = new LinkedList<>();
        for (File lFile : pLocaleFiles) {
            try ( BufferedReader reader
                    = new BufferedReader(new InputStreamReader(new FileInputStream(lFile), StandardCharsets.UTF_8))) {
                props.load(reader);
                Enumeration<?> e = props.propertyNames();
                String locale = lFile.getName().substring(0, lFile.getName().indexOf('.'));

                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = props.getProperty(key);

                    //Remove line breaks
                    value = value.replace("\r", "");
                    value = value.replace("\n", "");

                    /*
          if (!isValidKey(key)) {
            if (!key.isEmpty()) {
              LangConstant constant = new LangConstant();
              constant.section = key;
              constantList.add(constant);
            }
            continue;
          }
                     */
                    String constantKey = getConstantKey(key /*, false*/);
                    //key = transformKey(key, false);

                    //key = key.replace(".", "_");
                    LangConstant constant = null;
                    for (LangConstant constantTmp : constantList) {
                        if (constantTmp.constant.equalsIgnoreCase(constantKey)) {
                            constant = constantTmp;
                        }
                    }

                    LangConstant constantAll = new LangConstant();
                    constantAll.locale = locale;
                    constantAll.constant = constantKey;
                    constantAll.key = Character.toUpperCase(key.charAt(0)) + key.substring(1);
                    constantAll.transformedKey = CpxLanguageInterface.transformKey(constantAll.key);
                    constantAll.value = escapeTranslation(value);
                    constantListAll.add(constantAll);

                    if (constant == null) {
                        constant = constantAll;
                        constantList.add(constant);
                    }

                    //Contains placeholders?
                    if (constant.value.contains("{") && constant.value.contains("}")) {
                        constant.params = true;
                    }

                    String valTmp = "[" + locale + "] " + escapeEntities(value);
                    if (!constant.values.contains(valTmp)) {
                        constant.values.add(valTmp);
                    }
                    //System.out.println("  public static final String " + constantKey + " = \"" + value + "\"");
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        Collections.sort(constantList);

        for (LangConstant constant : constantList) {
            StringBuilder comment = new StringBuilder();

            for (LangConstant constant2 : constantList) {
                if (constant == constant2) {
                    continue;
                }
                if (!constant.section.isEmpty()) {
                    continue;
                }
                if (!constant2.section.isEmpty()) {
                    continue;
                }
                if (constant.key.equalsIgnoreCase(constant2.key) || constant.constant.equalsIgnoreCase(constant2.constant)) {
                    if (comment.length() != 0) {
                        comment.append(" ");
                    }
                    comment.append("key is probably duplicated defined as '" + constant2.constant + "', key should be unique!");
                }

                if (constant.value.equalsIgnoreCase(constant2.value)) {
                    if (comment.length() != 0) {
                        comment.append(" ");
                    }
                    comment.append("this value ('" + constant2.value + "') seems to be also defined under the key " + constant2.constant + "!");
                }
            }

            for (int i = 0; i < constant.constant.length(); i++) {
                Character c = constant.constant.charAt(i);
                c = Character.toLowerCase(c);
                if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_') {
                    //ok!
                } else {
                    if (comment.length() != 0) {
                        comment.append(" ");
                    }
                    comment.append("key seems to contain special characters, be careful!");
                    break;
                }
            }

            constant.comment = comment.toString();
        }

        for (LangConstant constant : constantList) {
            out.add("");
            out.add("    /** <pre>" + constant.constant);
            //out.add("      * SQLite key: " + constant.transformedKey);
            String[] values = constant.getValues2();
            for (int i = 0; i < values.length; i++) {
                String val = values[i];
                out.add("      * " + val + ((i == values.length - 1) ? "</pre>" : ""));
            }
            if (!constant.comment.isEmpty()) {
                //out.add("    * FIXME " + constant.comment);
                out.add("      * Attention! " + constant.comment);
            }
            out.add("      */");
            out.add("    public static final String " + constant.constant + " = \"" + constant.key + "\";");
        }
        out.add("");

        //Static Translation Map
        List<String> localeList = new ArrayList<>();
        for (LangConstant constant : constantList) {
            if (!localeList.contains(constant.locale)) {
                localeList.add(constant.locale);
            }
        }

        out.add("    static {");
        out.add("        initLocales();");
        out.add("    }");
        out.add("");
        out.add("    protected static void initLocales() {");
        for (String locale : localeList) {
            out.add("        initLocale" + StringUtils.capitalize(locale) + "();");
        }
        out.add("    }");
        out.add("");
        for (String locale : localeList) {
            out.add("    private static void initLocale" + StringUtils.capitalize(locale) + "() {");
            out.add("        final HashMap<String, String> " + locale + " = new HashMap<>();");
//        }h
//        for (String locale : localeList) {
//            out.add("    M.put(\"" + locale + "\", " + locale + "); ");
//        }
//        out.add("");
            for (LangConstant constant : constantListAll) {
                if (!constant.locale.equalsIgnoreCase(locale)) {
                    continue;
                }
                //out.add("    /** " + constant.constant + " = "  + constant.getValues() + " */");
                out.add("        " + constant.locale + ".put(" + constant.constant + ", \"" + constant.value + "\");");
            }
//        for (String locale : localeList) {
            out.add("        M.put(\"" + locale + "\", " + locale + "); ");
            out.add("    } ");
            out.add("");
        }

        /*
    out.add("  public static String get(final String pKey) {");
    out.add("    if (cpxLanguage != null) {");
    out.add("      return cpxLanguage.get(pKey);");
    out.add("    }");
    out.add("    return pKey;");
    out.add("  }");
    out.add("");
         */
        for (LangConstant constant : constantList) {
            String methodName = getMethodName(constant.key);

            out.add("    /**");
            out.add("     * <pre>" + constant.constant);
            out.add("     * SQLite key: " + constant.transformedKey);
            String[] values = constant.getValues2();
            for (int i = 0; i < values.length; i++) {
                String val = values[i];
                out.add("     * " + val + ((i == values.length - 1) ? "</pre>" : ""));
            }
            if (!constant.comment.isEmpty()) {
                out.add("     * ATTENTION " + constant.comment);
            }
            //out.add("   * " + (constant.comment.isEmpty()?"":"\r\n   * Please notice: " + constant.comment));
            /*
      out.add("   * " + constant.constant + " = "  + constant.getValues() + 
              (constant.comment.isEmpty()?"":"\r\n   * Please notice: " + constant.comment));
             */
            if (constant.params) {
                out.add("     * @param pParams placeholder replacements");
            }
            out.add("     * @return " + escapeEntities(escapeCharacters(constant.getValues())));
            out.add("     */");
            out.add("    public static String get" + methodName + "(" + (constant.params ? "final Object... pParams" : "") + ") {");
            out.add("        return get(" + constant.constant + (constant.params ? ", pParams" : "") + ").value;");
            out.add("    }");
            out.add("");

            if (constant.hasAbbreviation() || constant.hasDescription()) {
                out.add("    public static Translation get" + methodName + "Obj(" + (constant.params ? "final Object... pParams" : "") + ") {");
                out.add("        return get(" + constant.constant + (constant.params ? ", pParams" : "") + ");");
                out.add("    }");
                out.add("");
            }
        }
        out.add("}");

        StringBuilder sb = new StringBuilder();
        for (String line : out) {
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            sb.append(line);
        }

        //Create a tree view of all keys
        Map<String, Object> map = new LinkedHashMap<>(); //String.CASE_INSENSITIVE_ORDER
        //String output = "";
        for (LangConstant constant : constantList) {
            //String spaces = "";
            String[] tmp = constant.key.split("\\.");
            Map<String, Object> subMap = map;
            for (int i = 0; i < tmp.length; i++) {
                //spaces += "  ";
                String val = tmp[i];
                //val = (val == null)?"":val.trim();
                val = findKey(subMap, val);

                if (subMap.get(val) == null) {
                    subMap.put(val, new LinkedHashMap<String, Object>()); //String.CASE_INSENSITIVE_ORDER
                }
                subMap = (Map<String, Object>) subMap.get(val);

                //output += spaces + val;
                if (i == tmp.length - 1) {
                    subMap.put(val, constant.getValues());
                    //output += " = " + constant.getValues();
                }

                //output += "\r\n";
            }
        }

        StringBuilder sb2 = new StringBuilder();

        final boolean addTranslationsAsTree = true;

        if (addTranslationsAsTree) {
            buildTreeString(map, sb2, 0);

            sb.append("\r\n\r\n");
            sb.append("/* Tree view of language keys: ");
            sb.append(sb2.toString());
            sb.append("\r\n");
            sb.append("*/");
        }

        return sb.toString();
    }

    public static String escapeCharacters(final String pValue) {
        if (pValue == null) {
            return pValue;
        }
        //String value = pValue.replace("@", "{@literal @}");
        String value = pValue.replace("&", "{@literal &}");
        //value = value.replace("<", "&lt;");
        //value = value.replace(">", "&gt;");
        return value;
    }

    public static String findKey(Map<String, Object> pMap, String pKey) {
        String key = (pKey == null) ? "" : pKey.trim();
        String newKey = "";

        for (String keyTmp : pMap.keySet()) {
            if (key.equalsIgnoreCase(keyTmp)) {
                newKey = keyTmp;
                break;
            }
        }

        if (newKey.isEmpty()) {
            return key;
        } else {
            return newKey;
        }
    }

    /*
  public Comparator<Map.Entry<String, Object>> getTreeComparator() {
    return new Comparator<Map.Entry<String, Object>>() {
      
        @Override
        public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
            //return o1.getValue().compareTo(o2.getValue());
            return 0;
        } 
        
    };
  }
     */
    public static String escapeTranslation(final String pValue) {
        String value = (pValue == null) ? "" : pValue;
        value = value.replace("\"", "\\\"");
        //value = value.replace("<", "&lt;");
        //value = value.replace(">", "&gt;");
        return value;
    }

    public static String escapeEntities(final String pValue) {
        String value = (pValue == null) ? "" : pValue;
        value = value.replace("<", "&lt;");
        value = value.replace(">", "&gt;");
        return value;
    }

    public static StringBuilder buildTreeString(final Map<String, Object> pMap, final StringBuilder sb, final int pLevel) {
        String spaces = "";
        for (int i = 0; i < pLevel; i++) {
            spaces += "  ";
        }
        String arrow = "";
        if (pLevel > 0) {
            arrow = " ↪ ";
        }
        for (Map.Entry<String, Object> entry : pMap.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Map) {
                sb.append("\r\n");
                key = StringUtils.capitalize(key);
                sb.append(spaces + arrow + key);
                Map<String, Object> subMap = (Map<String, Object>) val;
                buildTreeString(subMap, sb, (pLevel + 1));
            } else {
                //System.out.println(val);
                sb.append(" = " + val);
            }
        }
        //sb.append("\r\n");
        return sb;
    }

    public static String getMethodName(final String pKey) {
        List<String> wordSeparators = Arrays.asList(new String[]{"."});

        String key = pKey;
        key = key.replace("-", "_");
        key = key.replace("/", "_");
        key = key.replace("+", "");
        String k = key;
        while (k.endsWith(".")) {
            k = k.substring(0, k.length() - 1);
        }
        //key = key.replace(".", "_");
        String methodName = "";
        String s_old = "";
        //Character c_old = ' ';
        for (int i = 0; i < key.length(); i++) {
            Character c = key.charAt(i);
            String s = String.valueOf(c);
            //if (wordSeparators.contains(s)) {
            if (wordSeparators.contains(s)) {
                //Nothing
            } else {
                if (wordSeparators.contains(s_old)) {
                    s = s.toUpperCase();
                }
                methodName += s;
            }
            s_old = s;
            //c_old = c;
        }
        return methodName;
    }

    /*
  public String getConstantKey(final String pKey) {
    return getConstantKey(pKey, true);
  }
     */
    public static String getConstantKey(final String pKey /*, final boolean pWithPrefix*/) {
        /*
    if (!isValidKey(pKey)) {
      return "";
    }
         */
        String key = (pKey == null) ? "" : pKey.trim();

        String tmp = "";
        if (pKey != null && key.equals(pKey.toUpperCase())) {
            tmp = pKey;
        } else {
            key = key.replace(".", "_");
            key = key.replace("-", "_");
            key = key.replace("/", "_");
            key = key.replace("+", "");
            String k = key;
            while (k.endsWith(".")) {
                k = k.substring(0, k.length() - 1);
            }
            key = k;
            tmp = "";
            String s_old = "";
            Character c_old = ' ';
            for (int i = 0; i < key.length(); i++) {
                Character c = key.charAt(i);
                String s = String.valueOf(c);
                if (c >= '0' && c <= '9' && c_old >= '0' && c_old <= '9' || c == '_') {
                    //
                } else if (!s_old.isEmpty()
                        && !s_old.equals("_")
                        && s_old.toLowerCase().equals(s_old)
                        && s.toUpperCase().equals(s)) {
                    tmp += "_";
                }
                tmp += s.toUpperCase();
                if (s_old.equals(".") || s_old.equals("_")) {
                    s_old = s.toUpperCase();
                    c_old = Character.toUpperCase(c);
                } else {
                    s_old = s;
                    c_old = c;
                }
            }
        }

        while (true) {
            String tmp2 = tmp;
            tmp = tmp.replace("__", "_");
            if (tmp2.equals(tmp)) {
                break;
            }
        }

        /*
    if (!tmp.isEmpty()) {
      if (pWithPrefix) {
        tmp = getPrefix() + tmp;
      }
    }
         */
        return tmp;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //CpxLanguage cpxLanguage = new CpxLanguage();
        CpxSystemPropertiesInterface props = CpxSystemProperties.getInstance();
        List<String> locales = new LinkedList<>();
        locales.add("en");
        locales.add("de");
        List<File> localeFiles = new LinkedList<>();
        //String userDir = props.getUserDir();
        String userDir = "";

        if (args.length <= 0 || args[0].trim().isEmpty()) {
            LOG.log(Level.SEVERE, "No arguments passed, needs working directory (e.g. ${project.basedir})");
        } else {
            userDir = args[0].trim() + props.getFileSeparator();
        }

        //userDir = userDir.replace("cpx-service-api", "");
        LOG.log(Level.INFO, "Current Working Directory: " + userDir);
        for (String locale : locales) {
            String fileName = userDir + "../../WD_CPX_Server/locale/" + locale + ".properties";
            LOG.log(Level.INFO, "Try to create CPX Language Constants File with the help of this language resource bundle: " + fileName);
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) {
                LOG.log(Level.SEVERE, "Language resource bundle not found!");
            }
            localeFiles.add(file);
        }
        String langConstantClass = createLanguageConstants(localeFiles);
        //String langFileName = new File(Lang.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath() + props.getFileSeparator() + Lang.class.getSimpleName();
        //String langFileName = MainLangConstantsCreator.class.getResource(Lang.class.getSimpleName() + ".class");
        String langFileName = userDir
                + /* "../../" + */ //"cpx-shared-lib" + props.getFileSeparator() + 
                //"cpx-service-api" + props.getFileSeparator() + 
                "src" + props.getFileSeparator()
                + "main" + props.getFileSeparator()
                + "java" + props.getFileSeparator()
                + "de.lb.cpx.shared.lang".replace(".", props.getFileSeparator()) + props.getFileSeparator()
                + "Lang.java";
        //Lang.class.getSimpleName() + ".java";
        LOG.log(Level.INFO, "Now I will create the Language Constants File '" + langFileName + "'...");
        File langFile = new File(langFileName);
        if (!langFile.exists() || !langFile.isFile()) {
            LOG.log(Level.SEVERE, "Lang Constants file cannot be found!");
        }
        try {
            //System.out.println(langConstantClass);
            writeToFile(langFile, langConstantClass);
        } catch (IOException ex) {
            Logger.getLogger(MainLangConstantsCreator.class.getName()).log(Level.SEVERE, "Cannot read locale files", ex);
        }
    }

    public static void writeToFile(final File pLangFile, final String pContent) throws IOException {
        try ( OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(pLangFile), StandardCharsets.UTF_8); //FileWriter fw = new FileWriter(out);
                  BufferedWriter writer = new BufferedWriter(out)) {
            writer.write(pContent);
        }
    }

    private static class LangConstant implements Comparable<LangConstant> {

        public String locale = "";
        public String constant = "";
        public String key = "";
        public String value = "";
        public List<String> values = new LinkedList<>();
        public String section = "";
        public String comment = "";
        public boolean params = false;
        private String transformedKey = "";

        @Override
        public int compareTo(LangConstant o) {
            return this.constant.toLowerCase().compareTo(o.constant.toLowerCase());
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.constant);
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
            final LangConstant other = (LangConstant) obj;
            if (!Objects.equals(this.constant, other.constant)) {
                return false;
            }
            return true;
        }

        public String getValues() {
            StringBuilder valuesStr = new StringBuilder();
            for (String lValue : values) {
                if (lValue == null || lValue.trim().isEmpty()) {
                    continue;
                }
                String value = CpxLanguageInterface.getValue(lValue);
                String abbr = CpxLanguageInterface.getAbbreviation(lValue);
                if (!abbr.isEmpty()) {
                    value += " (" + abbr + ")";
                }
                if (valuesStr.length() > 0) {
                    valuesStr.append(", ");
                }
                valuesStr.append(value);
            }
            return valuesStr.toString();
        }

        public String[] getValues2() {
            List<String> list = new ArrayList<>();
            for (String lValue : values) {
                if (lValue == null || lValue.trim().isEmpty()) {
                    continue;
                }
                String value = CpxLanguageInterface.getValue(lValue);
                String abbr = CpxLanguageInterface.getAbbreviation(lValue);
                if (!abbr.isEmpty()) {
                    value += " (" + abbr + ")";
                }
                value = value.replace("&", "&amp;");
                value = value.replace(">", "&gt;");
                value = value.replace("<", "&lt;");
                /*
        if (valuesStr.length() > 0) {
        valuesStr.append("\n");
        }
                 */
                //("[" + locale + "] " +
                list.add(value);
            }
            String[] array = new String[list.size()];
            list.toArray(array);
            return array;
        }

        public String getAbbreviation() {
            return CpxLanguageInterface.getAbbreviation(value);
        }

        public String[] getAbbreviations() {
            List<String> list = new ArrayList<>();
            for (String lValue : values) {
                if (lValue == null || lValue.trim().isEmpty()) {
                    continue;
                }
                lValue = CpxLanguageInterface.getAbbreviation(lValue);
                lValue = lValue.trim();
                lValue = lValue.replace("&", "&amp;");
                lValue = lValue.replace(">", "&gt;");
                lValue = lValue.replace("<", "&lt;");

                if (lValue.isEmpty()) {
                    continue;
                }
                /*
        if (valuesStr.length() > 0) {
        valuesStr.append("\n");
        }
                 */
                //("[" + locale + "] " +
                list.add(lValue);
            }
            String[] array = new String[list.size()];
            list.toArray(array);
            return array;
        }

        public boolean hasAbbreviation() {
            return (getAbbreviations().length > 0);
        }

        public String getDescription() {
            return CpxLanguageInterface.getDescription(value);
        }

        public String[] getDescriptions() {
            List<String> list = new ArrayList<>();
            for (String lValue : values) {
                if (lValue == null || lValue.trim().isEmpty()) {
                    continue;
                }
                lValue = CpxLanguageInterface.getDescription(lValue);
                lValue = lValue.trim();
                lValue = lValue.replace("&", "&amp;");
                lValue = lValue.replace(">", "&gt;");
                lValue = lValue.replace("<", "&lt;");

                if (lValue.isEmpty()) {
                    continue;
                }
                /*
        if (valuesStr.length() > 0) {
        valuesStr.append("\n");
        }
                 */
                //("[" + locale + "] " +
                list.add(lValue);
            }
            String[] array = new String[list.size()];
            list.toArray(array);
            return array;
        }

        public boolean hasDescription() {
            return (getDescriptions().length > 0);
        }

        public String getValue() {
            return CpxLanguageInterface.getValue(value);
        }

        /*
    public boolean hasAbbreviation() {
    for(String lValue: values) {
    if (lValue == null) {
    continue;
    }
    String[] arr = lValue.split(delimiter);
    if (arr.length >= 2) {
    if (!arr[1].trim().isEmpty()) {
    return true;
    }
    }
    }
    return false;
    }
         */
    }

}
