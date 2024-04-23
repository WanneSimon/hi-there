package cc.wanforme.hi.func;

import dev.webview.webview_java.bridge.JavascriptFunction;
import dev.webview.webview_java.bridge.JavascriptObject;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class EmojiOp extends JavascriptObject {
    private static final String DEFAULT_CFG = "paths.txt";

    private File cfg;
    private List<String> paths = new ArrayList<>();
    private Stage stage;

    public EmojiOp(Stage stage) throws IOException {
        this(DEFAULT_CFG);
        this.stage = stage;
    }
    public EmojiOp(String cfgFile) throws IOException {
        cfg = new File(cfgFile);
        this.load();
    }

    /**
     * 读取设置的路径
     * @throws IOException
     */
    private void load() throws IOException {
        if(!cfg.exists()) {
            return;
        }

        paths.clear();
        List<String> list = Files.readAllLines(cfg.toPath(), StandardCharsets.UTF_8);
        paths.addAll(list);
    }

    /**
     *
     * @throws IOException
     */
    private void save() throws IOException {
        File parent = cfg.getParentFile();
        if ( parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        String str = String.join("\r\n", paths);
        Files.writeString(cfg.toPath(), str, StandardCharsets.UTF_8);
    }

    /** 列出目录下的文件 */
    @JavascriptFunction
    public List<CusFileInfo> listImage(String path) {
        File file = new File(path);
        if (!file.exists()) {
           return new ArrayList<>();
        }

        File[] fs = file.listFiles();
        List<CusFileInfo> collect = Arrays.stream(fs).map(e -> {
            String ext = null;
            String name = e.getName();
            int index = name.lastIndexOf('.');
            if ( index > 0 && index!=name.length()-1 ) {
                ext = name.substring(index+1);
            }

            return new CusFileInfo(e.getName(), e.length(), e.isDirectory(),
                    e.getAbsolutePath(), ext);
        }).collect(Collectors.toList());
        return collect;
    }

    /** 获取文件的 base64 */
    @JavascriptFunction
    public String open(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(new File(path).toPath());
        String encode = Base64.getEncoder().encodeToString(bytes);
        return encode;
    }

    /** 获取文件的 base64 */
    @JavascriptFunction
    public String selectFolder() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int re = chooser.showOpenDialog(null);
        if ( re == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            if (dir!=null) {
                return dir.getPath();
            }
        }

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select directory");
//        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*.*"));
//        File file = fileChooser.showOpenDialog(stage);
//        if (file != null) {
//            return file.getPath();
//        }

        return null;
    }

    @JavascriptFunction
    public List<String> get() {
        return paths;
    }

    @JavascriptFunction
    public boolean savePaths(String[] paths) {
        if (paths == null) {
            this.paths.clear();
            return true;
        }

        this.paths.clear();
        this.paths.addAll(Arrays.asList(paths));

        try {
            this.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @JavascriptFunction
    public boolean copyImg(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return false;
        }

        // openfx
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        try {
            Image image = new Image(new FileInputStream(f));
            content.putImage(image);

            // support gif for qq
            List<File> fileList = new ArrayList<>(1);
            fileList.add(f);
            content.putFiles(fileList);

            clipboard.setContent(content);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        return true;
    }

}
