package cc.wanforme.hi;

import cc.wanforme.hi.func.EmojiOp;
import cc.wanforme.mswebview.MsWebviewProperties;
import cc.wanforme.mswebview.UIServerHolder;
import cc.wanforme.ofx.BaseOFXApplication;
import com.sun.jna.ptr.PointerByReference;
import dev.webview.webview_java.Webview;
import dev.webview.webview_java.bridge.WebviewBridge;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebviewLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(WebviewLauncher.class);
    private static WebviewLauncher instance = null;

    private MsWebviewProperties properties;
    private UIServerHolder uiServerHolder;

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    private Webview wv;
    private Stage stage;

    public WebviewLauncher() {
        if (instance!=null) {
            throw new RuntimeException("Only one Application allowed!");
        }
        instance = this;
    }

    public void startWebview(Stage stage) throws Exception {
        //Webview wv = new Webview(true, 800, 600);
        this.stage = stage;
        Scene s = stage.getScene();
        // fx started before spring
        wv = new Webview(true, new PointerByReference(FXStageDetect.getWindowPointer(stage)),
                (int)s.getWidth(), (int)s.getHeight());
        wv.setTitle("Hi there");
        wv.setFixedSize((int)s.getWidth(), (int)s.getHeight());

        // 有系统托盘，所以不关闭。空实现是为了去掉 ofx-spring-boot-support 中的默认行为 (关闭 spring web)
        stage.setOnCloseRequest(e -> {
            exit();
            // todo: future feature
            // reason: Platform.runLater() will not execute if there is no stage being showing.
            // FX 窗口关闭时， mswebview 会丢失容器句柄。所以关闭时关闭 mswebview，打开时重新创建 mswebview
            //wv.close();
        });

        String url = properties.getFrontIndexUrl();
        if (!url.startsWith("http")) {
            int port = uiServerHolder.getPort();
            String schema = sslEnabled ? "https://" : "http://";
            url = String.format("%s127.0.0.1:%s",schema,  port) + url;
        }

        bindJs();

        LOG.info("frontend - url: " + url);
        wv.loadURL(url);
        new Thread(() -> {
            wv.run();
        }).start();
        //wv.close();
    }

    private void bindJs() throws IOException {
        WebviewBridge bridge = new WebviewBridge(wv);
        bridge.defineObject("jpp", new EmojiOp(stage));
    }


    @Autowired
    public void setProperties(MsWebviewProperties properties) {
        this.properties = properties;
    }
    @Autowired
    public void setUiServerHolder(UIServerHolder uiServerHolder) {
        this.uiServerHolder = uiServerHolder;
    }

    public Webview getWv() {
        return wv;
    }
    public static WebviewLauncher getInstance() {
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    public void exit() {
        try {
            wv.close();
            stage.close();
            ConfigurableApplicationContext context = BaseOFXApplication.getSpringContext();
            if (context!=null && context.isRunning()) {
                context.close();
            }

            Platform.exit();
            System.exit(0);
        } catch (Exception e) {
            LOG.error("error on exit", e);
            System.exit(1);
        }
    }
}
