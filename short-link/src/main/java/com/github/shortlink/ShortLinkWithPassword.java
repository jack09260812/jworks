package com.github.shortlink;

/**
 * 带密码短链接实体
 * Created by lijinwei on 2017/4/13.
 */
public class ShortLinkWithPassword implements ShortLinkGetter {
    private String shortLink;
    private String url;
    private String password;

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
