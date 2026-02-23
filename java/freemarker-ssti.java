import freemarker.template.utility.ObjectConstructor;
import freemarker.core.TemplateClassResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Controller
public class FreemarkerTestCases {

    // RULE: freemarker-ssti-vulnerability

    // 1. @RequestParam directly into model.addAttribute
    @RequestMapping("/error")
    public String handleError(@RequestParam String errorMessage, Model model) {
        // ruleid: freemarker-ssti-vulnerability
        model.addAttribute("errorObj", new ErrorObject(errorMessage));
        return "customerror";
    }

    // 2. @RequestParam with explicit value= into model.addAttribute
    @RequestMapping("/search")
    public String handleSearch(@RequestParam(value = "query") String query, Model model) {
        // ruleid: freemarker-ssti-vulnerability
        model.addAttribute("result", query);
        return "searchresult";
    }

    // 3. getParameter directly into model.put
    @RequestMapping("/legacy")
    public String handleLegacy(HttpServletRequest req, Model model) {
        String input = req.getParameter("data");
        // ruleid: freemarker-ssti-vulnerability
        model.put("data", input);
        return "legacy";
    }

    // 4. getHeader into new Template(...)
    @RequestMapping("/template")
    public String handleTemplate(HttpServletRequest req, Model model) {
        String header = req.getHeader("X-Template");
        // ruleid: freemarker-ssti-vulnerability
        Object tpl = new CustomTemplate(header);
        // ruleid: freemarker-ssti-vulnerability
        model.addAttribute("tpl", tpl);
        return "view";
    }


    // 5. input passed through sanitizer
    @RequestMapping("/safe")
    public String handleSafe(@RequestParam String input, Model model) {
        String sanitized = sanitizeTemplateInput(input);
        // ok: freemarker-ssti-vulnerability
        model.addAttribute("data", sanitized);
        return "safe";
    }

    // 6. input passed through escape function
    @RequestMapping("/safe2")
    public String handleSafe2(@RequestParam String input, Model model) {
        String sanitized = escapeTemplateSpecialChars(input);
        // ok: freemarker-ssti-vulnerability
        model.addAttribute("result", sanitized);
        return "safe2";
    }

    // 7. no user input, hardcoded value only
    @RequestMapping("/static")
    public String handleStatic(Model model) {
        // ok: freemarker-ssti-vulnerability
        model.addAttribute("message", "Hello, World!");
        return "static";
    }

    // 8. user input never reaches the model directly
    @RequestMapping("/whitelist")
    public String handleWhitelist(@RequestParam String action, Model model) {
        if (ALLOWED_ACTIONS.contains(action)) {
            // ok: freemarker-ssti-vulnerability
            model.addAttribute("action", "safe_static_value");
        }
        return "whitelist";
    }

    // RULE: template-object-constructor-access

    // 1. direct use of ObjectConstructor
    public void configureUnsafe() {
        // ruleid: template-object-constructor-access
        freemarker.template.utility.ObjectConstructor oc = new freemarker.template.utility.ObjectConstructor();
    }

    // 2. SAFER_RESOLVER used instead of ALLOWS_NOTHING_RESOLVER
    public void configurePartial(Configuration cfg) {
        // ruleid: template-object-constructor-access
        cfg.setClassResolver(TemplateClassResolver.SAFER_RESOLVER);
    }

    // 3. most restrictive resolver explicitly set
    public void configureSafe(Configuration cfg) {
        // ok: template-object-constructor-access
        cfg.setClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
    }

    // 4. no resolver or ObjectConstructor references at all
    public void configureDefault(Configuration cfg) {
        // ok: template-object-constructor-access
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(java.util.Locale.US);
    }
}