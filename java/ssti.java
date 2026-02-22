// Уязвимость VMware Automation - стиль уязвимости CVE-2020-4006
@Controller 
public class ErrorController {

    @RequestMapping("/error")
    public String handleGenericError(@RequestParam String errorMessage, Model model) {
        // УЯЗВИМОСТЬ: Пользовательский ввод напрямую в модель
        model.addAttribute("errorObj", new ErrorObject(errorMessage));
        return "customerror"; // Рендерит шаблон customerror.ftl
    }
}

// Файл шаблона: customerror.ftl 
public class ErrorTemplate {
    // УЯЗВИМОСТЬ: Прямое вычисление пользовательского ввода
    // ${errorObj.message?html}
    // ${errorObj.code?html}
}