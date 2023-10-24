/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.interceptor;

import java.io.Serializable;
import java.sql.SQLException;
import javax.annotation.Priority;
import javax.ejb.EJBException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;
import org.hibernate.HibernateException;

@Throws
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class CpxExceptionInterceptor implements Serializable {

    @AroundInvoke
    public Object check(InvocationContext invocationContext) throws Exception, Throwable {
        try {
            return invocationContext.proceed();

        } catch (javax.ejb.EJBException ejbEx) {
            Throwable sqlEx = findSqlException(ejbEx);
            if (sqlEx instanceof SQLException) {
                Exception ex = new Exception(sqlEx.getMessage());
                ex.setStackTrace(sqlEx.getStackTrace());
                throw new EJBException(sqlEx.getMessage(),ex);
            }
            throw ejbEx;
//            throw new Exception(e1.getMessage()).initCause(e1);
        }
    }
    private Throwable findSqlException(Throwable pException) {
        if(pException instanceof EJBException){
            return findSqlException(pException.getCause()); 
        }
        if(pException instanceof PersistenceException){
            return findSqlException(pException.getCause()); 
        }
        if(pException instanceof HibernateException){
            return findSqlException(pException.getCause());
        }
        if(pException instanceof SQLException){
            return pException;
        }
        return null;
    }
}
