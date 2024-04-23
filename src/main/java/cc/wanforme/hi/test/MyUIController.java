package cc.wanforme.hi.test;

import cc.wanforme.mswebview.MsWebviewProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

public class MyUIController {

    private MsWebviewProperties appProperties;
    private  ResourceLoader resourceLoader;

    public MyUIController(MsWebviewProperties appProperties, ResourceLoader resourceLoader) {
        this.appProperties = appProperties;
        this.resourceLoader = resourceLoader;
    }

    /** 此请求用于映射前端的静态资源，前端url最终会404，然后交给 ErrorController 处理，并重定向到前端首页 */
    @GetMapping("${jpp-ms-webview.front-base-url2}/**")
    public void appRoot(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String loc = request.getRequestURI().substring(appProperties.getFrontBaseUrl().length());
//        String staticLoc = appProperties.getFrontBaseDir() + loc;

        String loc = request.getRequestURI().substring("/jpp".length());
        String staticLoc = appProperties.getFrontBaseDir() + loc;

        if (!exist(staticLoc)) {
            staticLoc = appProperties.getFrontIndexHtml();
        }

        request.getRequestDispatcher(staticLoc).forward(request, response);
    }

    private boolean exist(String loc) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + loc);
        return resource.exists();
    }

}