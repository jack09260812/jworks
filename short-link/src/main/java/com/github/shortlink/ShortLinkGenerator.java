package com.github.shortlink;

/**
 * Created by lijinwei on 2017/4/13.
 */
public interface ShortLinkGenerator<T extends ShortLinkGetter> {
    T generator(String url);
}
