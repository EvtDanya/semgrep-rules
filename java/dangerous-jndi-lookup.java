package com.example.vulnerable.dangerousjndilookup;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// @RequestParam in controller with direct lookup
@RestController
public class VulnerableController1 {
    public Object jndiLookup(@RequestParam String userInput2, @RequestParam String userInput) throws NamingException {
        Context ctx = new InitialContext();
		// ruleid: dangerous-jndi-lookup
        return ctx.lookup(userInput);
    }
}

// HttpServletRequest getParameter with JNDI lookup
public class VulnerableServlet1 {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws NamingException {
        String userInput = request.getParameter("jndiName");
        Context ctx = new InitialContext();
		// ruleid: dangerous-jndi-lookup
        Object result = ctx.lookup(userInput);
    }
}

// HttpServletRequest getHeader with JNDI lookup
public class VulnerableServlet2 {
    public void processRequest(HttpServletRequest request) throws NamingException {
        String userInput = request.getHeader("X-JNDI-Name");
		// ruleid: dangerous-jndi-lookup
        new InitialContext().lookup(userInput);
    }
}

// BufferedReader readLine with ContextFactory lookup
public class VulnerableFileProcessor {
    public void processFile(BufferedReader reader) throws NamingException {
        String userInput = reader.readLine();
        Context ctx = ContextFactory.getInitialContext();
		// ruleid: dangerous-jndi-lookup
        ctx.lookup(userInput);
    }
}

// Multiple steps before sink
public class VulnerableService {
    public Object lookupWithProcessing(HttpServletRequest request) throws NamingException {
        String temp = request.getParameter("input");
        String processed = temp.trim();
        String finalInput = processed.toLowerCase();
        
        InitialContext ctx = new InitialContext();
		// ruleid: dangerous-jndi-lookup
        return ctx.lookup(finalInput);
    }
}

// LDAP context lookup
public class VulnerableLDAP {
    public void ldapSearch(HttpServletRequest request) throws NamingException {
        String userFilter = request.getParameter("filter");
        InitialDirContext ctx = new InitialDirContext();
		// ruleid: dangerous-jndi-lookup
        ctx.lookup(userFilter);
    }
}

// if condition with true branch containing lookup
public class SafeController1 {
    public Object safeLookup(@RequestParam String userInput) throws NamingException {
        if (true) {
            Context ctx = new InitialContext();
			// ruleid: dangerous-jndi-lookup
            return ctx.lookup(userInput);
        }
        return null;
    }
}

// Sanitized with validateJNDIName
public class SafeController1 {
    public Object safeLookup(@RequestParam String userInput) throws NamingException {
        if (validateJNDIName(userInput)) {
            Context ctx = new InitialContext();
            // todo ok
			// ruleid: dangerous-jndi-lookup
            return ctx.lookup(userInput);
        }
        return null;
    }
}

// Sanitized with whitelist
public class SafeController2 {
    private Set<String> JNDI_WHITELIST = Set.of("java:/comp/env/jdbc/db1", "java:/comp/env/jdbc/db2");
    
    public Object safeLookup(@RequestParam String userInput) throws NamingException {
        if (JNDI_WHITELIST.contains(userInput)) {
            Context ctx = new InitialContext();
			// todo ok
			// ruleid: dangerous-jndi-lookup
            return ctx.lookup(userInput);
        }
        return null;
    }
}

// Hardcoded JNDI name (no user input)
public class SafeLookup1 {
    public Object hardcodedLookup() throws NamingException {
        Context ctx = new InitialContext();
		// ok: dangerous-jndi-lookup
        return ctx.lookup("java:/comp/env/jdbc/production");
    }
}

// Input not used in lookup
public class SafeLookup2 {
    public void unrelatedOperations(@RequestParam String userInput) throws NamingException {
        System.out.println("User input: " + userInput);
        Context ctx = new InitialContext();
		// ok: dangerous-jndi-lookup
        ctx.lookup("java:/comp/env/jdbc/safe");
    }
}

// Input transformed and validated
public class SafeLookup3 {
    public Object validatedLookup(HttpServletRequest request) throws NamingException {
        String input = request.getParameter("name");
        if (input != null && input.matches("^[a-zA-Z0-9/:.]+$")) {
            if (input.startsWith("java:/comp/env/")) {
                Context ctx = new InitialContext();
				// todo ok
			    // ruleid: dangerous-jndi-lookup
                return ctx.lookup(input);
            }
        }
        return null;
    }
}

// Different variable name, but hardcoded
public class SafeLookup4 {
    public void processWithHardcoded(@RequestParam String unused) throws NamingException {
        String jndiName = "java:/comp/env/jdbc/safe";
        InitialContext ctx = new InitialContext();
		// ok: dangerous-jndi-lookup
        ctx.lookup(jndiName);
    }
}

// Input used elsewhere, not in JNDI
public class SafeLookup5 {
    public void mixedOperations(@RequestParam String userInput) throws NamingException {
        log.info("User requested: " + userInput);
        
        Context ctx = new InitialContext();
		// ok: dangerous-jndi-lookup
        ctx.lookup("java:/comp/env/jdbc/constant");
    }
}

// Input sanitized by custom validation method
public class SafeLookup7 {
    private boolean isValidJNDIName(String name) {
        return name != null && name.startsWith("java:") && !name.contains("ldap:");
    }
    
    public Object lookupWithCustomValidation(@RequestParam String userInput) throws NamingException {
        if (isValidJNDIName(userInput)) {
            Context ctx = new InitialContext();
			// todo ok
			// ruleid: dangerous-jndi-lookup
            return ctx.lookup(userInput);
        }
        return null;
    }
}