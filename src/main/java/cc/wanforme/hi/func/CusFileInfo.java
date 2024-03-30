package cc.wanforme.hi.func;

import co.casterlabs.rakurai.json.annotating.JsonClass;

/** json序列化的规则，请查看 co.casterlabs.rakurai.json.annotating.DefaultJsonSerializer#getFields(java.lang.Class)
 * <br/>
 * Field注解 - JsonField 用来单个暴露
 * Field注解 - JsonExclude 用来去除字段
 *
 * 类注解 - @JsonClass(exposeAll=true) 暴露全部字段。
 * */
@JsonClass(exposeAll = true)
public class CusFileInfo {

    public String name;
    public Long size;
    public Boolean isDir;
    public String path;
    public String ext;

    public CusFileInfo(String name, Long size, Boolean isDir, String path, String ext) {
        this.name = name;
        this.size = size;
        this.isDir = isDir;
        this.path = path;
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
