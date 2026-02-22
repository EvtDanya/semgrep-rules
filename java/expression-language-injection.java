// Инъекция EL в JSF в Spring-контроллере
@Controller
public class HelloController {
    public void setFirstName(String name) {
        // УЯЗВИМОСТЬ: Прямое вычисление EL пользовательского ввода
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getApplication().evaluateExpressionGet(ctx, "#{" + name + "}", String.class);
    }
}

// Инъекция Spring EL через аннотацию @Value
@Component
public class ConfigService {
    @Value("#{systemProperties['user.input']}")
    private String userControlledProperty; // УЯЗВИМО, если user.input контролируется пользователем
}

@Controller
public class VulnerableSpringController {
    @RequestMapping("/eval")
    public String evaluateExpression(@RequestParam String expr, Model model) {
        // УЯЗВИМОСТЬ: Прямое вычисление Spring EL
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expr); // Контролируется пользователем
        Object result = exp.getValue();
        model.addAttribute("result", result);
        return "result";
    }
}