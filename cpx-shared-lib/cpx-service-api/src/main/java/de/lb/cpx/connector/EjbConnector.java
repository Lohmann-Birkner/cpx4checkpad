/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.connector;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.hash.generator.HashGenerator;
import de.lb.cpx.server.catalog.service.ejb.RuleServiceBeanRemote;
import de.lb.cpx.server.rule.analyser.RuleCheckServiceBeanRemote;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.server.rule.services.RuleLockBeanRemote;
import de.lb.cpx.service.ejb.AcgServiceEJBRemote;
import de.lb.cpx.service.ejb.AddCaseEJBRemote;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.ejb.BatchJobBeanRemote;
import de.lb.cpx.service.ejb.BatchResultEJBRemote;
import de.lb.cpx.service.ejb.CaseMergingServiceBeanRemote;
import de.lb.cpx.service.ejb.CaseServiceBeanRemote;
import de.lb.cpx.service.ejb.CatalogImportServiceEJBRemote;
import de.lb.cpx.service.ejb.ConfigurationServiceEJBRemote;
import de.lb.cpx.service.ejb.CpxP21ImportBeanRemote;
import de.lb.cpx.service.ejb.JmsTestBeanRemote;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.ejb.LoginServiceEJBRemote;
import de.lb.cpx.service.ejb.MasterDataBeanServiceRemote;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.QuotaListStatelessEJBRemote;
import de.lb.cpx.service.ejb.ReadmissionServiceEJBRemote;
import de.lb.cpx.service.ejb.ReadonlyDocumentEJBRemote;
import de.lb.cpx.service.ejb.ResourceBundleEJBRemote;
import de.lb.cpx.service.ejb.RiskServiceBeanRemote;
import de.lb.cpx.service.ejb.RuleListStatelessEJBRemote;
import de.lb.cpx.service.ejb.SearchListServiceEJBRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.service.ejb.TemplateServiceBeanRemote;
import de.lb.cpx.service.ejb.TextTemplateServiceBeanRemote;
import de.lb.cpx.service.ejb.TransferCatalogBeanRemote;
import de.lb.cpx.service.ejb.WorkflowListStatelessEJBRemote;
import de.lb.cpx.service.ejb.WorkingListStatelessEJBRemote;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Wilde
 *
 * Programmstartpunkt, wird gebraucht um den Kontext zu initialisieren, bei
 * "reiner" FX-Implementierung kann der Kontext nicht korrekt initialisiert
 * werden
 *
 */
public class EjbConnector {

    private static final Logger LOG = Logger.getLogger(EjbConnector.class.getName());

    private String clientId = null;
    private final ObjectProperty<Context> jndiContext = new SimpleObjectProperty<>();
    private final ObjectProperty<Context> jndiUserContext = new SimpleObjectProperty<>();

    private String serverHost = "";
    private int serverPort = 0;

    public void closeContexts() {
        closeJndiContext();
        closeJndiUserContext();
    }

    public void initContexts(final String pServerHost, final int pServerPort, final String pServerUser, final String pServerPassword) throws NamingException {
        serverHost = pServerHost;
        serverPort = pServerPort;
        createJndiContextInstance(serverHost, serverPort, pServerUser, pServerPassword);
        EjbProxy<AuthServiceEJBRemote> authServiceBean = connectAuthServiceBean();
        if (authServiceBean.get() != null) {
            setClientId(String.valueOf(authServiceBean.get().getNewClientId()));
            LOG.log(Level.INFO, "Client-ID is " + getClientId());
        }
        createJndiUserContextInstance(serverHost, serverPort, "", "");
    }

    public boolean doCpxLogin(final String pCpxUser, final String pPassword, final String pDatabase, final AppTypeEn pAppTypeEn, boolean changeRole) throws LoginException {
        if (getClientId() == null || getClientId().isEmpty()) {
            throw new IllegalArgumentException("EJB Connector has no Client ID!");
        }
        EjbProxy<LoginServiceEJBRemote> loginServiceBean = connectLoginServiceBean();
        final String hashedPassword = HashGenerator.getInstance().hash(pPassword);
        return loginServiceBean.get().login(getClientId(), pCpxUser, hashedPassword, pDatabase, CpxSystemProperties.getInstance(), pAppTypeEn, changeRole);
    }

    public boolean doCpxLogout() throws NamingException {
        EjbProxy<LoginServiceEJBRemote> loginServiceBean = connectLoginServiceBean();
        return loginServiceBean.get().doLogout();
    }

    public boolean doChangeDatabase(final String pDatabase) throws LoginException, NamingException {
        if (getClientId().isEmpty()) {
            throw new LoginException("EJB Connector has no Client ID!");
        }
        if (pDatabase == null || pDatabase.isEmpty()) {
            throw new NamingException("No Database specified!");
        }
        EjbProxy<LoginServiceEJBRemote> loginServiceBean = connectLoginServiceBean();
        return loginServiceBean.get().changeDatabase(getClientId(), pDatabase);
    }

    public boolean isJndiContextInitialized() {
        return jndiContext.get() != null;
    }

    public boolean isJndiUserContextInitialized() {
        return jndiUserContext.get() != null;
    }

    protected synchronized boolean closeJndiContext() {
        if (jndiContext == null || jndiContext.get() == null) {
            return true;
        }
        try {
            jndiContext.get().close();
            jndiContext.set(null);
            return true;
        } catch (NamingException ex) {
            Logger.getLogger(EjbConnector.class.getName()).log(Level.SEVERE, "Cannot close JDNI context", ex);
            return false;
        }
    }

    protected synchronized boolean closeJndiUserContext() {
        if (jndiUserContext == null || jndiUserContext.get() == null) {
            return true;
        }
        try {
            jndiUserContext.get().close();
            jndiUserContext.set(null);
            return true;
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, "Cannot close JNDI user context", ex);
            return false;
        }
    }

    protected void setClientId(final String pClientId) {
        if (pClientId == null || pClientId.trim().isEmpty()) {
            clientId = null;
        } else {
            clientId = pClientId;
        }
    }

    public String getClientId() {
        return clientId;
    }

    protected Properties getClientProperties(final String pServerHost, final int pServerPort, final String pUser, final String pPassword) {
        Properties clientProperties = new Properties();
        clientProperties.put("remote.connections", "default");
        clientProperties.put("remote.connection.default.port", String.valueOf(pServerPort));
        clientProperties.put("remote.connection.default.host", pServerHost);
        clientProperties.put("remote.connection.default.username", pUser);
        clientProperties.put("remote.connection.default.password", pPassword);
        clientProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        clientProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        clientProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        return clientProperties;
    }

    protected synchronized Context createJndiUserContextInstance(final String pServerHost, final int pServerPort, final String pUser, final String pPassword) throws NamingException {
        closeJndiUserContext();
        //Just a hack to pass all fields to the server correctly!!
        //String weiredUserParamField = pUser + delimiter + HashGenerator.hash(pPassword) + delimiter + clientId;
        if (clientId == null || clientId.trim().isEmpty()) {
            Logger.getLogger(EjbConnector.class.getName()).log(Level.SEVERE, "Cannot create JNDI user context, Client ID is not set. Do EjbConnector.setClient(...) first!");
            return null;
        }
        //String clientId = String.valueOf(clientId);
        //String user = pUser.toLowerCase().trim();
        Properties clientProperties = getClientProperties(pServerHost, pServerPort, clientId, pPassword);
        //clientProperties.put("jboss.naming.client.ejb.context", true);
        //clientProperties.put("remote.connection.default.username", weiredUserParamField);
        //clientProperties.put("remote.connection.default.password", pPassword);
        //clientProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        //clientProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");

        //EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(clientProperties);
        //ContextSelector<EJBClientContext> contextSelector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
        //EJBClientContext.setSelector(contextSelector);
        //EJBClientContext.getContextManager().
        Properties properties = clientProperties;
        //properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        //properties.put(Context.PROVIDER_URL,"http-remoting://" + CpxClientConfig.instance().getServerHost() + ":" + CpxClientConfig.instance().getServerPort());
        //properties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        properties.put(Context.SECURITY_PRINCIPAL, clientId);
        properties.put(Context.SECURITY_CREDENTIALS, pPassword);
        //properties.put("jboss.naming.client.ejb.context", true);
        //properties.put(Context.SECURITY_AUTHENTICATION, "none");
        //properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        //properties.put("remote.connection.default.callback.handler.class", "de.lb.cpx.service.auth.DatabaseCallback");
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");

        //properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        //properties.put(Context.PROVIDER_URL, "http-remoting://localhost:8085");
        //properties.put("jboss.naming.client.ejb.context", true);
        jndiUserContext.set(new InitialContext(properties));
        return jndiUserContext.get();
    }

    protected Context getJndiUserContextInstance() {
        return jndiUserContext.get();
    }

    protected synchronized Context createJndiContextInstance(final String pServerHost, final int pServerPort, final String pUser, final String pPassword) throws NamingException {
        closeJndiContext();
        Properties clientProperties = getClientProperties(pServerHost, pServerPort, pUser, pPassword);

        //EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(clientProperties);
        //ContextSelector<EJBClientContext> contextSelector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
        //EJBClientContext.setSelector(contextSelector);
        Properties properties = clientProperties;
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiContext.set(new InitialContext(properties));
        return jndiContext.get();
    }

    protected Context getJndiContextInstance() {
        return jndiContext.get();
    }

    /**
     * Connects to EJB with jndi-Name
     *
     * @param <T> Target generic Remote Interfaceclass
     * @param jndi Full JNDI-Name of Target EJB
     * @return Remote Interfaceclass of target EJB
     */
    protected <T> T doJndiLookup(final String jndi) {
        try {
            return doJndiLookupWithException(jndi);
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, "Cannot establish connection to this bean: " + jndi, ex);
        }
        return null;
    }

    /**
     * Connects to EJB with jndi-Name
     *
     * @param <T> Target generic Remote Interfaceclass
     * @param jndi Full JNDI-Name of Target EJB
     * @return Remote Interfaceclass of target EJB
     * @throws javax.naming.NamingException Mostly occurs when server is
     * unavailable
     */
    @SuppressWarnings("unchecked")
    protected <T> T doJndiLookupWithException(final String jndi) throws NamingException {
        return (T) getJndiContextInstance().lookup(jndi);
    }

    /**
     * Connects to EJB with jndi-Name
     *
     * @param <T> Target generic Remote Interfaceclass
     * @param jndi Full JNDI-Name of Target EJB
     * @return Remote Interfaceclass of target EJB
     */
    protected <T> T doJndiUserLookup(final String jndi) {
        try {
            return doJndiUserLookupWithException(jndi);
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, "Cannot establish connection to this bean: " + jndi, ex);
        }
        return null;
    }

    /**
     * Connects to EJB with jndi-Name
     *
     * @param <T> Target generic Remote Interfaceclass
     * @param jndi Full JNDI-Name of Target EJB
     * @return Remote Interfaceclass of target EJB
     * @throws javax.naming.NamingException Mostly occures when server is
     * unavailable
     */
    @SuppressWarnings("unchecked")
    protected <T> T doJndiUserLookupWithException(final String jndi) throws NamingException {
        return (T) getJndiUserContextInstance().lookup(jndi);
    }

    protected String buildJndiName(final Class<?> pRemoteInterfaceClass) {
        return buildJndiName(pRemoteInterfaceClass, false);
    }

    protected String buildJndiName(final Class<?> pRemoteInterfaceClass, final boolean pStateful) {
        if (pRemoteInterfaceClass == null) {
            return null;
        }
        String remoteInterface = pRemoteInterfaceClass.getSimpleName();
        String className = remoteInterface;
        String remoteSuffix = "Remote";
        if (className.toLowerCase().endsWith(remoteSuffix.toLowerCase())) {
            className = className.substring(0, className.length() - remoteSuffix.length());
        }
        String beanJndiName = "ejb:cpx-ear/cpx-service-ejb/" + className + "!de.lb.cpx.service.ejb." + remoteInterface;
        if (pStateful) {
            beanJndiName += "?stateful";
        }
        return beanJndiName;
    }

    protected String buildJndiNameForCatalogService(final Class<?> pRemoteInterfaceClass) {
        return buildJndiNameForCatalogService(pRemoteInterfaceClass, false);
    }

    protected String buildJndiNameForCatalogService(final Class<?> pRemoteInterfaceClass, final boolean pStateful) {
        if (pRemoteInterfaceClass == null) {
            return null;
        }
        String remoteInterface = pRemoteInterfaceClass.getSimpleName();
        String className = remoteInterface;
        String remoteSuffix = "Remote";
        if (className.toLowerCase().endsWith(remoteSuffix.toLowerCase())) {
            className = className.substring(0, className.length() - remoteSuffix.length());
        }
        String beanJndiName = "ejb:cpx-ear/cpx-catalog-service-ejb/" + className + "!de.lb.cpx.server.catalog.service.ejb." + remoteInterface;
        if (pStateful) {
            beanJndiName += "?stateful";
        }
        return beanJndiName;
    }

    protected String buildJndiNameForRuleService(final Class<?> pRemoteInterfaceClass, final boolean pStateful) {
        if (pRemoteInterfaceClass == null) {
            return null;
        }
        String remoteInterface = pRemoteInterfaceClass.getSimpleName();
        String className = remoteInterface;
        String remoteSuffix = "Remote";
        if (className.toLowerCase().endsWith(remoteSuffix.toLowerCase())) {
            className = className.substring(0, className.length() - remoteSuffix.length());
        }
        String beanJndiName = "ejb:cpx-ear/cpx-rule-services/" + className + "!de.lb.cpx.server.rule.services." + remoteInterface;
        if (pStateful) {
            beanJndiName += "?stateful";
        }
        return beanJndiName;
    }

    protected String buildJndiNameForRuleCheckService(final Class<?> pRemoteInterfaceClass, final boolean pStateful) {
        if (pRemoteInterfaceClass == null) {
            return null;
        }
        String remoteInterface = pRemoteInterfaceClass.getSimpleName();
        String className = remoteInterface;
        String remoteSuffix = "Remote";
        if (className.toLowerCase().endsWith(remoteSuffix.toLowerCase())) {
            className = className.substring(0, className.length() - remoteSuffix.length());
        }
        String beanJndiName = "ejb:cpx-ear/cpx-rule-analyser/" + className + "!de.lb.cpx.server.rule.analyser." + remoteInterface;
        if (pStateful) {
            beanJndiName += "?stateful";
        }
        return beanJndiName;
    }

    public EjbProxy<LoginServiceEJBRemote> connectLoginServiceBean() {
        final String beanJndiName = buildJndiName(LoginServiceEJBRemote.class, true);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<ReadonlyDocumentEJBRemote> connectReadonlyDocumentBean() {
        final String beanJndiName = buildJndiName(ReadonlyDocumentEJBRemote.class, true);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<AuthServiceEJBRemote> connectAuthServiceBean() {
        final String beanJndiName = buildJndiName(AuthServiceEJBRemote.class, true);
        return new EjbProxy<>(jndiContext, beanJndiName);
    }

    public EjbProxy<MasterDataBeanServiceRemote> connectMasterDataBeanServiceBean() {
        final String beanJndiName = buildJndiName(MasterDataBeanServiceRemote.class);
        return new EjbProxy<>(jndiContext, beanJndiName);
    }

    public EjbProxy<WorkingListStatelessEJBRemote> connectWorkingListBean() {
        final String beanJndiName = buildJndiName(WorkingListStatelessEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<WorkflowListStatelessEJBRemote> connectWorkflowListBean() {
        final String beanJndiName = buildJndiName(WorkflowListStatelessEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<AcgServiceEJBRemote> connectAcgServiceBean() {
        final String beanJndiName = buildJndiName(AcgServiceEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<RuleListStatelessEJBRemote> connectRuleListBean() {
        final String beanJndiName = buildJndiName(RuleListStatelessEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<QuotaListStatelessEJBRemote> connectQuotaListBean() {
        final String beanJndiName = buildJndiName(QuotaListStatelessEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<SingleCaseEJBRemote> connectSingleCaseBean() {
        final String beanJndiName = buildJndiName(SingleCaseEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<AddCaseEJBRemote> connectAddCaseBean() {
        final String beanJndiName = buildJndiName(AddCaseEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<JmsTestBeanRemote> connectJmsTestBean() {
        final String beanJndiName = buildJndiName(JmsTestBeanRemote.class);
        return new EjbProxy<>(jndiContext, beanJndiName);
    }

    public EjbProxy<SingleCaseGroupingEJBRemote> connectSingleCaseGroupingBean() {
        final String beanJndiName = buildJndiName(SingleCaseGroupingEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<CatalogImportServiceEJBRemote> connectCatalogImportServiceBean() {
        final String beanJndiName = buildJndiName(CatalogImportServiceEJBRemote.class, true);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<ConfigurationServiceEJBRemote> connectConfigurationServiceBean() {
        final String beanJndiName = buildJndiName(ConfigurationServiceEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<ResourceBundleEJBRemote> connectResourceBundleBean() {
        final String beanJndiName = buildJndiName(ResourceBundleEJBRemote.class);
        return new EjbProxy<>(jndiContext, beanJndiName);
    }

    public EjbProxy<RuleServiceBeanRemote> connectToRuleServiceBean() {
        final String beanJndiName = buildJndiNameForCatalogService(RuleServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<CpxP21ImportBeanRemote> connectCpxP21ImportBean() {
        //ejb:cpx-ear/cpx-service-ejb/CpxP21ImportBean!de.lb.cpx.service.ejb.CpxP21ImportBeanRemote
        final String beanJndiName = "ejb:cpx-ear/cpx-service-ejb/CpxP21ImportBean!de.lb.cpx.service.ejb.CpxP21ImportBeanRemote?stateful";
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<BatchJobBeanRemote> connectBatchJobBean() {
        //ejb:cpx-ear/cpx-service-ejb/CpxP21ImportBean!de.lb.cpx.service.ejb.CpxP21ImportBeanRemote
        final String beanJndiName = "ejb:cpx-ear/CheckpointXImportService/BatchJobBean!de.lb.cpx.service.ejb.BatchJobBeanRemote";
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<LockService> connectLockServiceBean() {
        final String beanJndiName = "ejb:cpx-ear/cpx-service-ejb/LockServiceBean!de.lb.cpx.service.ejb.LockService";
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

//    public EjbProxy<CaseLockService> connectCaseLockServiceBean() {
//        final String beanJndiName = "ejb:cpx-ear/cpx-service-ejb/CaseLockServiceBean!de.lb.cpx.service.ejb.CaseLockService";
//        return new EjbProxy<>(jndiUserContext, beanJndiName);
//    }
//    
//    public EjbProxy<DBLockServiceRemote> connectDBLockServiceBean() {
//        final String beanJndiName = "ejb:cpx-ear/cpx-service-ejb/DBLockServiceBean!de.lb.cpx.service.ejb.DBLockServiceRemote";
//        return new EjbProxy<>(jndiUserContext, beanJndiName);
//    }
    public EjbProxy<BatchResultEJBRemote> connectBatchResultBean() {
        final String beanJndiName = buildJndiName(BatchResultEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<CaseServiceBeanRemote> connectCaseServiceBean() {
        final String beanJndiName = buildJndiName(CaseServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<ProcessServiceBeanRemote> connectProcessServiceBean() {
        final String beanJndiName = buildJndiName(ProcessServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<MenuCacheBeanRemote> connectMenuCacheBean() {
        final String beanJndiName = buildJndiName(MenuCacheBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<SearchListServiceEJBRemote> connectSearchListServiceBean() {
        final String beanJndiName = buildJndiName(SearchListServiceEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<TemplateServiceBeanRemote> connectTemplateServiceBean() {
        final String beanJndiName = buildJndiName(TemplateServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<CaseMergingServiceBeanRemote> connectCaseMergingServiceBean() {
        final String beanJndiName = buildJndiName(CaseMergingServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

   public EjbProxy<ReadmissionServiceEJBRemote> connectReadmissionServiceBean() {
        final String beanJndiName = buildJndiName(ReadmissionServiceEJBRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<TextTemplateServiceBeanRemote> connectTextTemplateServiceBean() {
        final String beanJndiName = buildJndiName(TextTemplateServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<RuleEditorBeanRemote> connectRuleEditorBean() {
        final String beanJndiName = buildJndiNameForRuleService(RuleEditorBeanRemote.class, false);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<RuleLockBeanRemote> connectRuleLockBean() {
        final String beanJndiName = buildJndiNameForRuleService(RuleLockBeanRemote.class, false);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }

    public EjbProxy<RuleCheckServiceBeanRemote> connectRuleCheckBean() {
        final String beanJndiName = buildJndiNameForRuleCheckService(RuleCheckServiceBeanRemote.class, false);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }
    
    public EjbProxy<RiskServiceBeanRemote> connectRiskServiceBean() {
        final String beanJndiName = buildJndiName(RiskServiceBeanRemote.class);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }
//    public EjbProxy<RuleReadBeanRemote> connectRuleReadBean() {
//        final String beanJndiName = buildJndiNameForRuleService(RuleReadBeanRemote.class, false);
//        return new EjbProxy<>(jndiUserContext, beanJndiName);
//    }

    public EjbProxy<TransferCatalogBeanRemote> connectTransferCatalogBean() {
        final String beanJndiName = buildJndiName(TransferCatalogBeanRemote.class, false);
        return new EjbProxy<>(jndiUserContext, beanJndiName);
    }
}
