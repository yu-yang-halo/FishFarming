package cn.fuck.fishfarming.fir;

/**
 * Created by Administrator on 2016/1/15.
 */
public class FirVersion {

    private String name;
    private String version;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "FirVersion{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", versionShort='" + versionShort + '\'' +
                ", build='" + build + '\'' +
                ", installUrl='" + installUrl + '\'' +
                ", install_url='" + install_url + '\'' +
                ", direct_install_url='" + direct_install_url + '\'' +
                ", update_url='" + update_url + '\'' +
                '}';
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

/*
         **************************************************
         {
          "name":"iPlus","version":"120",
            "changelog":"","updated_at":1452835274,
            "versionShort":"1.1.2","build":"120",
            "installUrl":"http://download.fir.im/v2/app/install/56976d27e75e2d531800000c?download_token=dd85044c16da1670802c92d15f36f574",
            "install_url":"http://download.fir.im/v2/app/install/56976d27e75e2d531800000c?download_token=dd85044c16da1670802c92d15f36f574",
            "direct_install_url":"http://download.fir.im/v2/app/install/56976d27e75e2d531800000c?download_token=dd85044c16da1670802c92d15f36f574",
            "update_url":"http://fir.im/z51k","binary":{"fsize":12650153}
          }


     */

}
