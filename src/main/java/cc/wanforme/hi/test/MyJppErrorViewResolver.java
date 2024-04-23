package cc.wanforme.hi.test;

import cc.wanforme.mswebview.MsWebviewProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class MyJppErrorViewResolver  extends DefaultErrorViewResolver {
    private MsWebviewProperties appProperties;

    public MyJppErrorViewResolver(ApplicationContext applicationContext, WebProperties.Resources resources, MsWebviewProperties appProperties) {
        super(applicationContext, resources);
        this.appProperties = appProperties;
    }

    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        String originPath = model.get("path").toString();
        boolean isFront = status == HttpStatus.NOT_FOUND && originPath.startsWith(this.appProperties.getFrontBaseUrl());

        if (isFront) {
            return new ModelAndView(this.appProperties.getFrontIndexHtml(), model, HttpStatus.OK);
        }

        return super.resolveErrorView(request, status, model);
    }
}
