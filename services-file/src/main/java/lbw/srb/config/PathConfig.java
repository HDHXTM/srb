package lbw.srb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 全局配置类
 * 
 * @author ruoyi
 */
@Component
public class PathConfig
{
    /** 上传路径 */
    @Value("${conf.filePath}")
    public String profile;

    public String getProfile()
    {
        return profile;
    }

    /**
     * 获取头像上传路径
     */
    public String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
