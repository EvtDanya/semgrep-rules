package com.example.vulnerable.expressionlanguageinjection;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// JSF EL-injection
@Controller
public class HelloController {
    public void setFirstName(@RequestParam String name) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        // ruleid: expression-language-injection
        ctx.getApplication().evaluateExpressionGet(ctx, "#{" + name + "}", String.class);
    }
}

public class VulnerableELInjectionController {
    @RequestMapping("/spel")
    public String spelInjection2(@RequestParam String userInput) {
        ExpressionParser parser = new SpelExpressionParser();
        // ruleid: expression-language-injection
        Expression exp = parser.parseExpression("#{" + userInput + "}");
        return exp.getValue().toString();
    }

    @RequestMapping("/safe")
    public String safeSpel() {
        ExpressionParser parser = new SpelExpressionParser();
        // ok: expression-language-injection
        Expression exp = parser.parseExpression("'Hello World'");
        return exp.getValue().toString();
    }
}

// EL-injection through annotation @Value
// TODO
@Component
public class ConfigService {
    @Value("#{systemProperties['user.input']}")
    private String userControlledProperty;
}

@Controller
public class VulnerableSpringController {
    @RequestMapping("/eval")
    public String evaluateExpression(@RequestParam String expr, Model model) {
        ExpressionParser parser = new SpelExpressionParser();
        // ruleid: expression-language-injection
        Expression exp = parser.parseExpression(expr);
        Object result = exp.getValue();
        model.addAttribute("result", result);
        return "result";
    }
}