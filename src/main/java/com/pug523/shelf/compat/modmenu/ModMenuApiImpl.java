package com.pug523.shelf.compat.modmenu;

import com.pug523.shelf.ShelfConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ShelfConfigScreen::createConfigScreen;
    }
}
