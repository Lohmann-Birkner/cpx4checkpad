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
package de.lb.cpx.server.p21.rest;

import de.lb.cpx.server.importservice.p21import2.JsonImportBeanLocal;
import de.lb.cpx.shared.dto.LockException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.PessimisticLockException;
import javax.security.auth.login.LoginException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;
import java.util.Map;

/**
 * Prototypical implementation of a REST API.
 *
 * @author Gloski
 */
@Path("/p21import2")
public class RestService2 {

    private static final Logger LOG = Logger.getLogger(RestService2.class.getName());

    //@EJB(name = "CpxP21ImportBean")
    //CpxP21ImportBeanLocal p21Importer;
    @EJB(name = "JsonImportBean")
    private JsonImportBeanLocal importBean;

//    @EJB(name = "LoginServiceEJB")
//    private LoginServiceEJBRemote loginService;
    @GET
    @Path("/batchgrouping/{database}")
    @Produces("application/json")
    public JsonObject startGrouping(@PathParam("database") String database) throws LoginException {
//        BatchGroupParameter parameter = new BatchGroupParameter();
        Long executionId;
        int status = 200;
        String message;
        try {
            executionId = importBean.startGrouping(database);
            if (executionId == null) {
                executionId = -1L;
                status = 900;
                message = "Job could not be started.";
            } else {
                message = "OK.";
            }
        } catch (LockException | PessimisticLockException ex) {
            LOG.log(Level.WARNING, "Database is possibly locked", ex);
            executionId = -1L;
            status = 902;
            message = "DB is locked. Another import or grouping job is running on the same DB.";
        }
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("executionId", executionId)
                .add("status", status)
                .add("message", message)
                .build();

        return jsonObject;
    }

//    @GET
//    @Path("/bill/{database}/{directory}")
//    @Produces("application/json")
//    public JsonObject startBillImport(@PathParam("database") String database, @PathParam("directory") String directory) throws LoginException {
////        BatchGroupParameter parameter = new BatchGroupParameter();
//        Long executionId;
//        int status = 200;
//        String message;
//        try {
//            executionId = importBean.startBillImport(database, directory);
//            if (executionId == null) {
//                executionId = -1L;
//                status = 900;
//                message = "Job could not be started.";
//            } else {
//                message = "OK.";
//            }
//        } catch (LockException | PessimisticLockException ex) {
//            LOG.log(Level.WARNING, "Database is possibly locked", ex);
//            executionId = -1L;
//            status = 902;
//            message = "DB is locked. Another import or grouping job is running on the same DB.";
//        }
//        JsonObject jsonObject = Json.createObjectBuilder()
//                .add("executionId", executionId)
//                .add("status", status)
//                .add("message", message)
//                .build();
//
//        return jsonObject;
//    }

    /**
     *
     * @param directoryName directory name from which is imported. This
     * directory has to be on the server as a child of the C:\p21-import
     * directory.
     * @param numberOfSubJobs number of sub jobs of an import job. The fall.csv
     * is divided in as many files (fall1.csv, fall2.csv,...) as the result of
     * the division of the whole number of cases in fall.csv divided through
     * this parameter.
     * @param checkType if set to 'version' a new version of the case is created
     * in the database if it already exists, otherwise it is overwrited in the
     * database.
     * @param doGroup if set to 'true', grouping is done. If set to 'false' no
     * grouping is performed.
     * @param grouperModel determines which grouper model is used for grouping.
     * The value must be one of the de.checkpoint.drg.GDRGModel enumeration.
     * @param database the name of the database, to which the import will import
     * the data. The name consists of two parts separated by ':'. One part of
     * the name consists of the name of the persistence-unit name (e.g.
     * 'dbsys1'), the other part is the database name (e.g. 'cpx_batch'), e.g.
     * 'dbsys1:cpx_batch'.
     * @param module import module (P21, FDSE)
     * @return Status-Message.
     * @throws javax.security.auth.login.LoginException login exception
     */
    @GET
    @Path("/{directory}/{numberOfSubJobs}/{checkType}/{doGroup}/{grouperModel}/{database}/{module}")
    @Produces("application/json")
    public JsonObject startImport(@PathParam("directory") String directoryName,
            @PathParam("numberOfSubJobs") int numberOfSubJobs,
            @PathParam("checkType") String checkType,
            @PathParam("doGroup") boolean doGroup,
            @PathParam("grouperModel") String grouperModel,
            @PathParam("database") String database,
            @PathParam("module") String module) throws LoginException {
        return importBean.startImport(directoryName, database, checkType, numberOfSubJobs, doGroup, grouperModel, module);
    }

    @GET
    @Path("/p21_export/download/{executionId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadP21Export(@PathParam("executionId") long pExecutionId) {
        return downloadFile("p21_export_" + pExecutionId);
    }

    @GET
    @Path("/list_export/download/{executionId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadListExport(@PathParam("executionId") long pExecutionId) {
        return downloadFile("list_export_" + pExecutionId);
    }

    private Response downloadFile(String pTempPath) {
        final String path = System.getProperty("java.io.tmpdir") + pTempPath + "\\"; //"\\p21_export_" + pExecutionId + "\\";
        File tmp = new File(path);
        final File zipFile = new File(tmp.getAbsolutePath() + "\\" + tmp.getName() + ".zip");
        if (!zipFile.exists() || !zipFile.isFile()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("file was not found on server: " + zipFile.getName())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        final String name = zipFile.getName();
        final long length = zipFile.length();
        //ResponseBuilder response = Response.ok((Object) zipFile);
        //response.header("Content-Disposition", "attachment; filename=" + zipFile.getName());
        //return response.build();
        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream arg0) throws IOException, WebApplicationException {
                // TODO Auto-generated method stub
                try (BufferedOutputStream bus = new BufferedOutputStream(arg0); FileInputStream fizip = new FileInputStream(zipFile)) {
                    //ByteArrayInputStream reader = (ByteArrayInputStream) Thread.currentThread().getContextClassLoader().getResourceAsStream();     
                    //byte[] input = new byte[2048];  
                    //URL uri = Thread.currentThread().getContextClassLoader().getResource("");
                    //File file = new File("D:\\Test1.zip");
                    byte[] buffer2 = IOUtils.toByteArray(fizip);
                    bus.write(buffer2);
                } catch (IOException ex) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    LOG.log(Level.SEVERE, ex.getMessage());
                    LOG.log(Level.FINEST, null, ex);
                } finally {
                    LOG.log(Level.INFO, "Delete file after upload or error: " + zipFile.getAbsolutePath());
                    try {
                        Files.delete(zipFile.toPath());
                        LOG.log(Level.INFO, "deleted file: {0}", zipFile.getAbsolutePath());
                    } catch (IOException ex) {
                        LOG.log(Level.WARNING, "was not able to delete file: " + zipFile.getAbsolutePath() + ", will delete it on exit", ex);
                        zipFile.deleteOnExit();
                    }
//                    if (!zipFile.delete()) {
//                        zipFile.deleteOnExit();
//                    }
                    final File parent = zipFile.getParentFile();
                    LOG.log(Level.INFO, "Delete tmp export folder after upload or error: " + parent.getAbsolutePath());
                    try {
                        Files.delete(parent.toPath());
                        LOG.log(Level.INFO, "deleted folder: {0}", parent.getAbsolutePath());
                    } catch (IOException ex) {
                        LOG.log(Level.WARNING, "was not able to delete folder: " + parent.getAbsolutePath() + ", will delete it on exit", ex);
                        parent.deleteOnExit();
                    }
//                    if (!parent.delete()) {
//                        parent.deleteOnExit();
//                    }
                }
            }
        };
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename=" + name)
                .header("content-length", length)
                .build();
    }

    @GET
    @Path("/clientId")
    @Produces("application/json")
    public JsonObject clientId() {
//        AuthServiceEJBRemote authService = lookup(AuthServiceEJB.class);
        return Json.createObjectBuilder()
                //.add("executionId", executionId)
                .add("status", Response.Status.OK.getStatusCode())
                .add("clientId", importBean.getNewClientId())
                .build();
    }

    @GET
    @Path("/login/{clientId}/{userName}/{hashedPassword}/{database}/{appType}")
    @Produces("application/json")
    public JsonObject login(@PathParam("clientId") String pClientId,
            @PathParam("userName") String pUserName,
            @PathParam("hashedPassword") String pHashedPassword,
            @PathParam("database") String pDatabase,
            //@PathParam("properties") CpxSystemPropertiesInterface pCpxClientSystemProperties,
            @PathParam("appType") String pAppTypeEn) {
//        LoginServiceEJBRemote loginService = lookup(LoginServiceEJBRemote.class);
        try {
            importBean.login(pClientId, pUserName, pHashedPassword, pDatabase, /* pCpxClientSystemProperties, */ pAppTypeEn);
        } catch (LoginException ex) {
            return Json.createObjectBuilder()
                    //.add("executionId", executionId)
                    .add("status", Response.Status.UNAUTHORIZED.getStatusCode())
                    .add("message", ex.getMessage())
                    .build();
        }
        return Json.createObjectBuilder()
                //.add("executionId", executionId)
                .add("status", Response.Status.OK.getStatusCode())
                .add("message", "OK.")
                .build();
    }
    
    @GET
    @Path("/getUserRoleProperties/{clientId}")
    @Produces("application/json")
    public JsonObject getUserRoleProperties(@PathParam("clientId") String pClientId) {
//        LoginServiceEJBRemote loginService = lookup(LoginServiceEJBRemote.class);
        JsonObject returnMessage;
        try {
            String prop = importBean.getUserRoleProperties(pClientId);
            if(prop!=null){
                //.add("executionId", executionId)
                returnMessage = Json.createObjectBuilder()
                .add("status", Response.Status.OK.getStatusCode())
                .add("message", prop)
                .build();
            }else{
                returnMessage = Json.createObjectBuilder()
                    //.add("executionId", executionId)
                    .add("status", Response.Status.NO_CONTENT.getStatusCode())
                    .add("message", "Rolepropterties are null!")
                    .build();
            }
        } catch (Exception ex) {
            returnMessage = Json.createObjectBuilder()
                    //.add("executionId", executionId)
                    .add("status", Response.Status.UNAUTHORIZED.getStatusCode())
                    .add("message", ex.getMessage())
                    .build();
        }
        return returnMessage;
    }
    
    @GET
    @Path("/getUserId/{clientId}")
    @Produces("application/json")
    public JsonObject getUserId(@PathParam("clientId") String pClientId) {
//        LoginServiceEJBRemote loginService = lookup(LoginServiceEJBRemote.class);
        JsonObject returnMessage;
        try {
            long id = importBean.getUserId(pClientId);
            if(id!=0){
                //.add("executionId", executionId)
                returnMessage = Json.createObjectBuilder()
                .add("status", Response.Status.OK.getStatusCode())
                .add("message", id)
                .build();
            }else{
                returnMessage = Json.createObjectBuilder()
                    //.add("executionId", executionId)
                    .add("status", Response.Status.NO_CONTENT.getStatusCode())
                    .add("message", "Userid is 0")
                    .build();
            }
        } catch (Exception ex) {
            returnMessage = Json.createObjectBuilder()
                    //.add("executionId", executionId)
                    .add("status", Response.Status.UNAUTHORIZED.getStatusCode())
                    .add("message", ex.getMessage())
                    .build();
        }
        return returnMessage;
    }
    
    @GET
    @Path("/getPersistenceUnits/{clientId}")
    @Produces("application/json")
    public JsonObject getPersistenceUnits(@PathParam("clientId") String pClientId) {
        JsonObject returnMessage;
        try {
           Map<String,Map<String,String>> persistenceUnitsMap = importBean.getPersistenceUnits(pClientId);
            
            if(persistenceUnitsMap!=null && !persistenceUnitsMap.isEmpty()){
                final JsonObjectBuilder json = Json.createObjectBuilder();
                json.add("status", Response.Status.OK.getStatusCode());
                JsonObjectBuilder jsonMessage = Json.createObjectBuilder();

                for(String key :persistenceUnitsMap.keySet()){
                    JsonObjectBuilder jsonUnit = Json.createObjectBuilder();
                    
                    jsonUnit.add("unit_name", key);
                    Map<String,String> dbInfoMap = persistenceUnitsMap.get(key);
                    
                    jsonUnit.add("connectionUrl", dbInfoMap.get("connectionUrl"));
                    jsonUnit.add("connectionString", dbInfoMap.get("connectionString"));
                    jsonUnit.add("database", dbInfoMap.get("database"));
                    jsonUnit.add("hostname", dbInfoMap.get("hostname"));
                    jsonUnit.add("port", dbInfoMap.get("port"));
                    jsonUnit.add("driverVendor", dbInfoMap.get("driverVendor"));
                    jsonUnit.add("driverName", dbInfoMap.get("driverName"));

                    jsonMessage.add(key, jsonUnit);
                }
                json.add("message",jsonMessage);
                returnMessage = json.build();
            }else{
                returnMessage = Json.createObjectBuilder()
                    .add("status", Response.Status.NO_CONTENT.getStatusCode())
                    .add("message", "Userid is 0")
                    .build();
            }
        } catch (Exception ex) {
            returnMessage = Json.createObjectBuilder()
                    .add("status", Response.Status.UNAUTHORIZED.getStatusCode())
                    .add("message", ex.getMessage())
                    .build();
        }
        return returnMessage;
    }
}
