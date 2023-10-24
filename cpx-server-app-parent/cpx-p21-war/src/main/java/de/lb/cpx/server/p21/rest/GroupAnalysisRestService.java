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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.p21.rest;

import de.lb.cpx.server.grouperAnalyse.GrouperAnalysisLocal;
import de.lb.cpx.server.grouperEvaluation.GrouperEvaluationLocal;
import java.text.ParseException;
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
@Path("/groupAnalysis")
public class GroupAnalysisRestService {

    @EJB(name = "GrouperAnalysis")
    private GrouperAnalysisLocal groupAnalysis;

    @EJB(name = "GrouperEvaluation")
    private GrouperEvaluationLocal grouperEvaluator;

    @GET
    @Path("/{grouperModel}/{database}")
    @Produces("text/html")
    public Response printGroupAnalysisMessage(@PathParam("grouperModel") String grouperModel,
            @PathParam("database") String database) {

        String analyse = groupAnalysis.performGroupAnalysis(grouperModel, database);
        String result = analyse;

        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/evaluateGrouping/{fileName}/{grouperModel}/{local}/{database}")
    @Produces("text/html")
    public Response evaluateGrouping(@PathParam("fileName") String fileName,
            @PathParam("grouperModel") String grouperModel,
            @PathParam("local") String local,
            @PathParam("database") String database
    ) throws ParseException {

        String res = grouperEvaluator.evaluate4Model(fileName, grouperModel, local.equalsIgnoreCase("local"), database);
        String result = res;

        return Response.status(200).entity(result).build();
    }

}
