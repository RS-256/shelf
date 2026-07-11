package com.pug523.shelf.gui.widget;

import com.pug523.shelf.config.Option;
import com.pug523.shelf.compat.BuiltinRegistriesCompat;
import com.pug523.shelf.compat.ComponentCompat;
import com.pug523.shelf.compat.GuiCompat;
import com.pug523.shelf.compat.IdentifierCompat;
import com.pug523.shelf.compat.JavaCompat;
import com.pug523.shelf.gui.layout.LayoutEngine;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemInputFieldWidget extends OptionWidget<Item> {
    private final StringInputFieldWidget stringInputWidget;
    private final Option<String> stringBridge;
    private final Function<String, List<String>> autoCompleter;
    private List<String> currentSuggestions = new ArrayList<>();
    private boolean showSuggestions = false;

    // TODO: move these to layout config
    private static final int AUTO_COMPLETION_MAX_COUNT = 7;

    public ItemInputFieldWidget(Option<Item> option, Function<String, List<String>> customAutoCompleter) {
        super(option);

        this.autoCompleter = customAutoCompleter != null ? customAutoCompleter : query -> {
            List<String> suggestions = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            for (Identifier id : BuiltinRegistriesCompat.ITEM.keySet()) {
                String idStr = id.toString();
                if (idStr.contains(lowerQuery)) {
                    suggestions.add(idStr);
                }
                if (suggestions.size() >= AUTO_COMPLETION_MAX_COUNT) {
                    break;
                }
            }
            return suggestions;
        };

        this.stringBridge = new Option<>(option.getName(), option.getDescriptionKey(),
            getItemRegistryName(option.getDefaultValue()), () -> getItemRegistryName(option.getPendingValue()),
            this::applyItem, JavaCompat.listOf());

        this.stringInputWidget = new StringInputFieldWidget(stringBridge, text -> true, this::onTextChange);
    }

    private void onTextChange(String input) {
        updateSuggestions(input);

        Item foundItem = findItem(input);

        if (input.isEmpty()) {
            this.option.setPendingValue(Items.AIR);
        } else {
            this.option.setPendingValue(foundItem);
        }

        this.stringBridge.setPendingValue(input);
    }

    private Item findItem(String input) {
        Identifier id = IdentifierCompat.tryParse(input);
        if (id != null && BuiltinRegistriesCompat.ITEM.containsKey(id)) {
            return BuiltinRegistriesCompat.getItem(id);
        }

        for (Item item : BuiltinRegistriesCompat.ITEM) {
            if (getItemDescription(item).getString().equalsIgnoreCase(input)) {
                return item;
            }
        }
        return Items.AIR;
    }

    private void applyDynamicTextColor(String input) {
        if (input.isEmpty() || findItem(input) != Items.AIR || !currentSuggestions.isEmpty()) {
            this.stringInputWidget.editBox.setTextColor(0xFFFFFF);
        } else {
            this.stringInputWidget.editBox.setTextColor(0xFF5555);
        }
    }

    private void updateSuggestions(String input) {
        if (input.isEmpty()) {
            this.currentSuggestions.clear();
            this.showSuggestions = false;
        } else {
            this.currentSuggestions = this.autoCompleter.apply(input);
            this.showSuggestions = !this.currentSuggestions.isEmpty();
        }
    }

    private void applyItem(String input) {
        Item foundItem = findItem(input);
        if (foundItem != Items.AIR) {
            this.option.setPendingValue(foundItem);
        }
    }

    private String getItemRegistryName(Item item) {
        return BuiltinRegistriesCompat.ITEM.getKey(item).toString();
    }

    private Component getItemDescription(Item item) {
        return ComponentCompat.translatable(item.getDescriptionId());
    }

    @Override
    public void render(Font font, GuiCompat gui, LayoutEngine layout, int x, int y, int width, int height, int mouseX,
                       int mouseY) {

        String value = this.stringBridge.getPendingValue();
        if (!value.equals(this.stringInputWidget.editBox.getValue())) {
            this.stringInputWidget.editBox.setValue(value);
            updateSuggestions(value);
        }

        this.stringInputWidget.render(font, gui, layout, x, y, width, height, mouseX, mouseY);

        applyDynamicTextColor(this.stringInputWidget.editBox.getValue());

        if (this.showSuggestions && this.stringInputWidget.editBox.isFocused()) {
            int boxX = this.stringInputWidget.editBox.getX();
            int boxY = this.stringInputWidget.editBox.getY() + this.stringInputWidget.editBox.getHeight();
            int boxWidth = this.stringInputWidget.editBox.getWidth();

            for (int i = 0; i < currentSuggestions.size(); i++) {
                int itemY = boxY + (i * 14);
                boolean isHovered = mouseX >= boxX && mouseX < boxX + boxWidth && mouseY >= itemY
                    && mouseY < itemY + 14;

                gui.fill(boxX, itemY, boxX + boxWidth, itemY + 14, isHovered ? 0xFF444444 : 0xFF222222);
                gui.text(font, ComponentCompat.literal(currentSuggestions.get(i)), boxX + 4, itemY + 3,
                    isHovered ? 0xFFFFCC00 : 0xFFFFFFFF, false);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int modifiers, LayoutEngine layout) {
        if (this.showSuggestions && this.stringInputWidget.editBox.isFocused()) {
            int boxX = this.stringInputWidget.editBox.getX();
            int boxY = this.stringInputWidget.editBox.getY() + this.stringInputWidget.editBox.getHeight();
            int boxWidth = this.stringInputWidget.editBox.getWidth();

            for (int i = 0; i < currentSuggestions.size(); i++) {
                int itemY = boxY + (i * 14);
                if (mouseX >= boxX && mouseX < boxX + boxWidth && mouseY >= itemY && mouseY < itemY + 14) {
                    String selected = currentSuggestions.get(i);
                    this.stringInputWidget.editBox.setValue(selected);
                    this.showSuggestions = false;
                    return true;
                }
            }
        }
        return this.stringInputWidget.mouseClicked(mouseX, mouseY, button, modifiers, layout);
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers, LayoutEngine layout) {
        if (this.showSuggestions && !currentSuggestions.isEmpty() && keycode == GLFW.GLFW_KEY_TAB) {
            this.stringInputWidget.editBox.setValue(currentSuggestions.get(0));
            this.showSuggestions = false;
            return true;
        }
        return this.stringInputWidget.keyPressed(keycode, scancode, modifiers, layout);
    }

    @Override
    public boolean charTyped(int codepoint, int modifiers, LayoutEngine layout) {
        return this.stringInputWidget.charTyped(codepoint, modifiers, layout);
    }
}
