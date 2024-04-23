package cc.wanforme.hi;

import cc.wanforme.ofx.BaseOFXApplication;
import cc.wanforme.ofx.ViewHolder;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.IOException;

public class HiThereFx extends BaseOFXApplication {
    private static final Logger LOG = LoggerFactory.getLogger(HiThereFx.class);
    private static final String SYSTEM_TRAY_ICON = "images/system-tray.jpg";
    private Stage stage;

    @Override
    protected void scene(Scene scene) {
    }

    @Override
    protected void stage(Stage stage) {
        this.stage = stage;
        stage.setWidth(800);
        stage.setHeight(600);

        // set icons
        try {
            ClassPathResource iconResources = new ClassPathResource(SYSTEM_TRAY_ICON);
            stage.getIcons().add(new javafx.scene.image.Image(iconResources.getInputStream()));
        } catch (IOException e) {
            LOG.warn("Can't set icons!", e);
            WebviewLauncher.getInstance().exit();
        }
    }

    @Override
    protected void afterShow() {
        // 创建系统托盘
        setupSystemTray();

        // 启动 webview
        try {
            ApplicationContext context = ViewHolder.get().getContext();
            WebviewLauncher wvLauncher = context.getBean(WebviewLauncher.class);
            wvLauncher.startWebview(stage);
        } catch (Exception e) {
            LOG.error("Unable to start webview", e);
            System.exit(1);
        }

    }


    /** SystemTray 系统托盘 */
    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            LOG.warn("SystemTray is not supported!");
            return;
        }

        // 点击关闭按钮不退出
        Platform.setImplicitExit(false);
        SystemTray systemTray = SystemTray.getSystemTray();

        Image image = null;
        try {
            ClassPathResource iconResources = new ClassPathResource(SYSTEM_TRAY_ICON);
//            InputStream inputStream = iconResources.getInputStream();
//            byte[] bytes = inputStream.readAllBytes();
//            inputStream.close();
//            image = Toolkit.getDefaultToolkit().createImage(bytes);
            image = Toolkit.getDefaultToolkit().getImage(iconResources.getURL());
        } catch (Exception e) {
            LOG.warn("Can't load system tray!", e);
            Platform.setImplicitExit(true);
        }

        // 创建菜单
        PopupMenu menu = new PopupMenu();
        MenuItem openItem = new MenuItem("open");
        openItem.addActionListener((e) ->{
            Platform.runLater(() -> {
                stage.show();
                stage.toFront();

                // todo: future feature 重新显示 mswebview
//                try {
//                    WebviewLauncher.getInstance().startWebview(stage);
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex);
//                }
            });
        });
        menu.add(openItem);

        MenuItem exitItem = new MenuItem("exit");
        exitItem.addActionListener((e) ->{
            Platform.runLater(() -> {
                WebviewLauncher.getInstance().exit();
            });
        });
        menu.add(exitItem);

        // construct a TrayIcon
        TrayIcon trayIcon = new TrayIcon(image, "Hi there", menu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener((e)->{

        });

        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            LOG.warn("SystemTray is not supported!", e);
            Platform.setImplicitExit(true);
        }
    }


}
