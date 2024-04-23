package cc.wanforme.hi.test;

import cc.wanforme.mswebview.MsWebviewProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

//@Configuration
public class MyConfiguration {

    @Bean
    public WebProperties.Resources Resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public ErrorViewResolver MyJppErrorViewResolver(ApplicationContext applicationContext, WebProperties.Resources resources, MsWebviewProperties appProperties) {
        return new MyJppErrorViewResolver(applicationContext, resources, appProperties);
    }

    @Bean
    public MyUIController MyUIController(MsWebviewProperties appProperties, ResourceLoader resourceLoader){
        return new MyUIController(appProperties, resourceLoader);
    }

}
