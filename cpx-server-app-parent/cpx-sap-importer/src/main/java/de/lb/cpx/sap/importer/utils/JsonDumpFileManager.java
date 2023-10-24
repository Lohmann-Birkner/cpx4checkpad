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
package de.lb.cpx.sap.importer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.lb.cpx.sap.kain_inka.PvtResultIf;
import de.lb.cpx.sap.results.SapKainPvtSearchResult;
import static de.lb.cpx.str.utils.StrUtils.toStr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author niemeier
 */
public abstract class JsonDumpFileManager {

    private String mFilename = "";
    private File mFile = null;
    private Gson mGson = null;

    /**
     *
     * @param pFilename file name
     * @param pIsReader is readonly?
     */
    public JsonDumpFileManager(final String pFilename, final boolean pIsReader) {
        mFilename = pFilename;
        mFile = new File(mFilename);
        initGson(pIsReader);
    }

    /**
     *
     * @throws IOException cannot open file
     */
    public abstract void openFile() throws IOException;

    /**
     *
     */
    public abstract void closeFile();

    /**
     *
     * @return file
     */
    public File getFile() {
        return mFile;
    }

    /**
     *
     * @return file name
     */
    public String getFilename() {
        return mFilename;
    }

    private void initGson(final boolean pIsReader) {
        GsonBuilder lBuilder = new GsonBuilder();
        //if (!Iskv21cDownloaderProperties.getInstance().getCompactDump()) {
        lBuilder = lBuilder.setPrettyPrinting();
        //}

        mGson = lBuilder
                .serializeNulls()
                //.registerTypeAdapter(EK_FALL_DAK.class, new EK_FALL_DAK_Serializer())
                //.registerTypeAdapter(Message.class, new Message_Serializer())
                //.registerTypeAdapter(PvvResultIf.class, new SapKainPvvSearchResultCreator())
                //.registerTypeAdapter(PvtResultIf.class, new SapKainPvtSearchResultCreator())
                //.registerTypeAdapter(PvvResultIf.class, new SapKainPvvSearchResultCreator())
                .registerTypeAdapter(PvtResultIf.class, (pIsReader ? new SapKainPvtSearchResultCreatorReader() : new SapKainPvtSearchResultCreatorWriter()))
                .create();
    }

    /**
     *
     * @return gson
     */
    protected Gson getGson() {
        return this.mGson;
    }

    /**
     *
     * @throws IOException cannot compress json file
     */
    public void compressFile() throws IOException {
        //Nur als ZIP komprimieren, falls die Datei größer als 0,5 MB ist
        //if (lFile.length()/1024 > 512) {
        zipFile(getFile().getAbsolutePath());
        //lFile.delete();
        //}
    }

    /**
     *
     * @param pSourceFileName source file name
     * @throws IOException cannot compress file
     */
    protected void zipFile(String pSourceFileName) throws IOException {
        pSourceFileName = toStr(pSourceFileName);
        String pTargetFileName = pSourceFileName + ".zip";
        zipFile(pSourceFileName, pTargetFileName);
    }

    /**
     *
     * @param pSourceFileName source file name
     * @param pTargetFileName target file name
     * @throws IOException cannot compress file
     */
    protected void zipFile(String pSourceFileName, String pTargetFileName) throws IOException {
        pSourceFileName = toStr(pSourceFileName);
        pTargetFileName = toStr(pTargetFileName);

        File lSourceFile = new File(pSourceFileName);

        if (pSourceFileName.isEmpty()) {
            return;
        }

        byte[] lBuffer = new byte[1024];

        FileOutputStream lFos = new FileOutputStream(pTargetFileName);
        try (ZipOutputStream lZos = new ZipOutputStream(lFos)) {
            ZipEntry lZe = new ZipEntry(lSourceFile.getName());
            lZos.putNextEntry(lZe);
            try (FileInputStream lIn = new FileInputStream(pSourceFileName)) {
                int lLength;
                while ((lLength = lIn.read(lBuffer)) > 0) {
                    lZos.write(lBuffer, 0, lLength);
                }
            }
            lZos.closeEntry();
        }
    }

    private static class SapKainPvtSearchResultCreatorWriter implements InstanceCreator<PvtResultIf> {

        @Override
        public SapKainPvtSearchResult createInstance(java.lang.reflect.Type type) {
            return new SapKainPvtSearchResult();
        }
    }

    /**
     *
     */
    public class SapKainPvtSearchResultCreatorReader implements JsonDeserializer<PvtResultIf> {

        /**
         *
         * @param json json
         * @param typeOfT interface type
         * @param context context
         * @return pvt result
         */
        @Override
        public PvtResultIf deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject jsonObj = json.getAsJsonObject();
            SapKainPvtSearchResult obj = new SapKainPvtSearchResult();
//            obj.setPvvrowid(jsonObj.get("PVVROWID").getAsString());
//            obj.setPvtrowid(jsonObj.get("PVTROWID").getAsString());
//            obj.setPvttext(jsonObj.get("PVTTEXT").getAsString());
//            obj.setHdPrim(jsonObj.get("HD_PRIM").getAsString());
//            obj.setHdSec(jsonObj.get("HD_SEC").getAsString());
//            obj.setNdPrim(jsonObj.get("ND_PRIM").getAsString());
//            obj.setNdSec(jsonObj.get("ND_SEC").getAsString());
//            obj.setPvtproc(jsonObj.get("PVTPROC").getAsString());
            obj.setPvvrowid(jsonObj.get("pvvrowid").getAsString());
            obj.setPvtrowid(jsonObj.get("pvtrowid").getAsString());
            obj.setPvttext(jsonObj.get("pvttext").getAsString());
            obj.setHdPrim(jsonObj.get("hdPrim").getAsString());
            obj.setHdSec(jsonObj.get("hdSec").getAsString());
            obj.setNdPrim(jsonObj.get("ndPrim").getAsString());
            obj.setNdSec(jsonObj.get("ndSec").getAsString());
            obj.setPvtproc(jsonObj.get("pvtproc").getAsString());
            return obj;
        }
    }

}
