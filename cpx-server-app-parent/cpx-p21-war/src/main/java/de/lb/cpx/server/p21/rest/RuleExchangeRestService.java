/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.p21.rest;

import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rulesExchange.RulesExchangeLocal;
import de.lb.cpx.shared.rules.util.RulesImportStatus;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author gerschmann
 */
@Path("RuleExchange")
public class RuleExchangeRestService {

    private static final Logger LOG = Logger.getLogger(RuleExchangeRestService.class.getName());

    @EJB(name = "RulesExchange")
    private RulesExchangeLocal rulesExchangeEjb;

    @GET
    @Path("/createRulePoolInDb/{poolname}/{year}")
    @Produces("text/html")
    public Response createRulePoolInDb(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year
    ) throws ParseException {
        String result = rulesExchangeEjb.createRulePoolInDb(poolName, year);
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/saveRuleTypesInDB")
    @Produces("text/html")
    public Response saveRuleTypesInDb() throws ParseException {
        String result = rulesExchangeEjb.saveRuleTypesInDB();
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/saveRules4PoolInDB/{poolname}/{year}")
    @Produces("text/html")
    public Response saveRules4PoolInDB(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year
    ) throws ParseException {
        String result = rulesExchangeEjb.saveRules4PoolInDB(poolName, year);
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/saveRuleTables4PoolInDB/{poolname}/{year}")
    @Produces("text/html")
    public Response saveRuleTables4PoolInDB(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year
    ) throws ParseException {
        String result = rulesExchangeEjb.saveRuleTables4PoolInDB(poolName, year);
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/importExternalRulesInDB/{poolname}/{year}/{filename}/{mode}/{role_ids_list}/{what}/{userName}")
    @Produces("text/html")
    public Response importExternalRulesInDB(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year,
            @PathParam("filename") String filename,
            @PathParam("mode") String mode,
            @PathParam("role_ids_list") String role_ids_list,
            @PathParam("what") String what,
            @PathParam("userName") String userName
    ) throws ParseException {
        long[] roleIds = null;
        if (role_ids_list != null && role_ids_list.trim().length() > 0) {
            role_ids_list = role_ids_list.trim();
            if (role_ids_list.startsWith("[")) {
                role_ids_list = role_ids_list.substring(1);
            }
            if (role_ids_list.endsWith("]")) {
                role_ids_list = role_ids_list.substring(0, role_ids_list.length() - 1);
            }
            if (role_ids_list.trim().length() > 0) {
                String[] parts = role_ids_list.split(",");
                if (parts.length > 0) {
                    roleIds = new long[parts.length];
                    int i = 0;
                    for (String part : parts) {
                        try {
                            roleIds[i] = Long.parseLong(part.trim());
                            i++;
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "error on evaluation of role ids " + role_ids_list, ex);
                            return Response.status(200).entity("error on evaluation of role ids " + role_ids_list).build();
                        }
                    }
                }
            }
        }

        String result = rulesExchangeEjb.importExternalRulesInDB(poolName, year, filename, mode, roleIds, what.equalsIgnoreCase("dev") ? PoolTypeEn.DEV : PoolTypeEn.PROD, userName);
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/getRulesFromDBAsXmlString/{poolname}/{year}/{rule_ids_list}")
    @Produces("text/html")
    public Response getRulesFromDBAsXmlString(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year,
            @PathParam("rule_ids_list") String rule_ids_list
    ) throws ParseException {
        List<String> rids = null;
        if (rule_ids_list != null && rule_ids_list.trim().length() > 0) {
            rule_ids_list = rule_ids_list.trim();
            if (rule_ids_list.startsWith("[")) {
                rule_ids_list = rule_ids_list.substring(1);
            }
            if (rule_ids_list.endsWith("]")) {
                rule_ids_list = rule_ids_list.substring(0, rule_ids_list.length() - 1);
            }
            if (rule_ids_list.trim().length() > 0) {
                String[] parts = rule_ids_list.split(",");
                rids = Arrays.asList(parts);
            }
        }
        String result = rulesExchangeEjb.exportRulesFromDB(poolName, year, null, rids);
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/saveRulesFromDB/{poolname}/{year}")
    @Produces("text/html")
    public Response saveRulesFromDB(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year
    ) throws ParseException {
        String result = rulesExchangeEjb.saveRulesFromDB(poolName, year);
        return Response.status(200).entity(result).build();

    }

    @GET
    @Path("/exportRulesFromDB/{poolname}/{year}/{filename}/{rule_ids_list}")
    @Produces("text/html")
    public Response exportRulesFromDB(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year,
            @PathParam("filename") String filename,
            @PathParam("rule_ids_list") String rule_ids_list
    ) throws ParseException {
        List<String> rids = null;
        if (rule_ids_list != null && rule_ids_list.trim().length() > 0) {
            rule_ids_list = rule_ids_list.trim();
            if (rule_ids_list.startsWith("[")) {
                rule_ids_list = rule_ids_list.substring(1);
            }
            if (rule_ids_list.endsWith("]")) {
                rule_ids_list = rule_ids_list.substring(0, rule_ids_list.length() - 1);
            }
            if (rule_ids_list.trim().length() > 0) {
                String[] parts = rule_ids_list.split(",");
                rids = Arrays.asList(parts);
            }
        }
        String result = rulesExchangeEjb.exportRulesFromDB(poolName, year, filename, rids);
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/importRulesInDBFromXmlString/{poolname}/{year}/{filename}/{mode}/{role_ids_list}")
    @Produces("text/html")
    public Response importRulesInDBFromXmlString(
            @PathParam("poolname") String poolName,
            @PathParam("year") int year,
            @PathParam("filename") String filename,
            @PathParam("mode") String mode,
            @PathParam("role_ids_list") String role_ids_list
    ) throws ParseException {
        long[] roleIds = null;
        if (role_ids_list != null && role_ids_list.trim().length() > 0) {
            role_ids_list = role_ids_list.trim();
            if (role_ids_list.startsWith("[")) {
                role_ids_list = role_ids_list.substring(1);
            }
            if (role_ids_list.endsWith("]")) {
                role_ids_list = role_ids_list.substring(0, role_ids_list.length() - 1);
            }
            if (role_ids_list.trim().length() > 0) {
                String[] parts = role_ids_list.split(",");
                if (parts.length > 0) {
                    roleIds = new long[parts.length];
                    int i = 0;
                    for (String part : parts) {
                        try {
                            roleIds[i] = Long.parseLong(part.trim());
                            i++;
                        } catch (NumberFormatException ex) {
                            LOG.log(Level.SEVERE, "error on evaluation of role ids " + role_ids_list, ex);
                            return Response.status(200).entity("error on evaluation of role ids " + role_ids_list).build();
                        }
                    }
                }
            }
        }
        String rulesString = null;
        try {
            rulesString = getXMLString(filename);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "could not find or read file  " + filename, ex);
            return Response.status(200).entity("error by reading file " + filename).build();

        }
        if (rulesString == null) {
            LOG.log(Level.SEVERE, "could not find or read file  " + filename);
            return Response.status(200).entity("error by reading file " + filename).build();
        }
        RulesImportStatus status = rulesExchangeEjb.importRulesInDBFromXmlString(poolName, year, mode, roleIds, rulesString);
        return Response.status(200).entity(status.getJsonString()).build();
    }

    private String getXMLString(String fileName) throws Exception {

        if (fileName == null || fileName.isEmpty()) {
            LOG.log(Level.WARNING, " there is no information to import");
            return null;

        }
        String path = "C:\\rules_import\\import\\" + fileName + ".xml";
        File xmlFile = new File(path);
        if (xmlFile.exists() && xmlFile.isFile() && xmlFile.canRead()) {
            URL url = new URL("file", null, path);
            byte[] all;
            try (InputStream stream = url.openStream()) {
                all = stream.readAllBytes();
            }
            return new String(all);

        } else {
            return null;
        }

    }

}
