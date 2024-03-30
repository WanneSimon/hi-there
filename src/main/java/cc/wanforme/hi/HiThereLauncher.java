package cc.wanforme.hi;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static cc.wanforme.ofx.BaseOFXApplication.launchOFX;

@SpringBootApplication
public class HiThereLauncher {
    public static void main(String[] args) {
        launchOFX(HiThereFx.class, HiThereLauncher.class, MainView.class, args);
    }
}
