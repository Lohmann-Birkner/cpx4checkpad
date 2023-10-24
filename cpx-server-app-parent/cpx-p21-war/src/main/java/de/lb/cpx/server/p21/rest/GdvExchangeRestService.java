/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.p21.rest;

import de.lb.cpx.server.Gdv.GdvResponseLocal;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author gerschmann
 */
@Path("GdvExchange")
public class GdvExchangeRestService {

    private static final Logger LOG = Logger.getLogger(GdvExchangeRestService.class.getName());
    private static final String PATH_TO_ATTACHMENT_DIRECTORY = "c:\\temp\\";
    private static final String PATH_TO_DEST_DIRECTORY = "c:\\temp\\";
    private static final String[] SUFFIX = {"pdf", "jpeg", "tif", "jpg" };
    
    @EJB(name = "GdvResponseEjb")
    private GdvResponseLocal gdvResponse;
    
    @GET
//    @Path("/getGdvAnswer/{database}/{patientNumber}/{attachment}")
    @Path("/getGdvAnswer/{database}/{patientNumber}/{attachment}/{dest}")
    @Produces("text/html")
    public Response getGdvAnswer(
    @PathParam("database") String database,
    @PathParam("patientNumber") String patientNumber,
     @PathParam("attachment") String attachment,
   @PathParam("dest" ) String dest
    )throws ParseException {
        try{
            String result = gdvResponse.createGdvAnswer(database, patientNumber, getAttachments(attachment), getDestPath(dest) );
            return Response.status(200).entity(result).build();        
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "generating error", ex);
            throw new ParseException(ex.getMessage(), 0);
        }
    }

    @GET
    @Path("/getGdvBill/{database}/{patientNumber}/{attachment}/{dest}")
    @Produces("text/html")    
    public Response getGdvBil(
     @PathParam("database") String database,
    @PathParam("patientNumber") String patientNumber,
    @PathParam("attachment") String attachment,
    @PathParam("dest") String dest)throws ParseException { 
        try{
            String result = gdvResponse.createGdvBill(database, patientNumber,getAttachments( attachment), getDestPath(dest));
             return Response.status(200).entity(result).build();
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "generating error", ex);
            throw new ParseException(ex.getMessage(), 0);
        }
    }
    
    @GET
    @Path("/ping") 
    public Response ping()
    {
        return  Response.ok().entity("OK").build();
    }
    
    private List<String> getAttachments(String attachment){
        String attachmentDir = PATH_TO_ATTACHMENT_DIRECTORY + attachment;

        List<String> paths = new ArrayList<>();
        Collection<File> files = FileUtils.listFiles(new File(attachmentDir), SUFFIX, false);
       if(files != null){
           files.forEach((file) -> {
               paths.add(file.getAbsolutePath());
            });
       }
       return paths;
        
    }
    
    private String getDestPath(String pDest){
        return PATH_TO_DEST_DIRECTORY + pDest;
    }
    
    
}
