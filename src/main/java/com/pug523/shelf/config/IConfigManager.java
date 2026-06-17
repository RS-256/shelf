package com.pug523.shelf.config;

import java.io.Serializable;

public interface IConfigManager<T extends Serializable> {
    T getConfig();

    void load();

    void save();
}
