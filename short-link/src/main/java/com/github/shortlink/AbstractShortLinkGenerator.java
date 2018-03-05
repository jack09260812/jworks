package com.github.shortlink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lijinwei on 2017/4/13.
 */
public abstract class AbstractShortLinkGenerator<T extends ShortLinkGetter> implements ShortLinkGenerator<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected StringGenerator stringGenerator;
    protected ShortLinkStorage shortLinkStorage;

    public AbstractShortLinkGenerator() {

    }

    public AbstractShortLinkGenerator(StringGenerator stringGenerator, ShortLinkStorage shortLinkStorage) {
        this.stringGenerator = stringGenerator;
        this.shortLinkStorage = shortLinkStorage;
    }

    /**
     * 实现url生成短链接
     *
     * @param url
     * @return
     */
    public T generator(String url) {
        String shortLink = stringGenerator.generator(url);
        boolean valid = validate(shortLink);
        if (valid) {
            T t = generator(url, shortLink);
            shortLinkStorage.save(t);
            return t;
        } else {
            logger.error("短链接：{}验证失败，原始链接：{}", shortLink, url);
            return null;
        }

    }

    protected boolean validate(String shortLink) {
        return true;
    }

    protected abstract T generator(String url, String shortLink);
}
