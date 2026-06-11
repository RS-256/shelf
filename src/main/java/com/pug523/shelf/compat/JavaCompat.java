package com.pug523.shelf.compat;

import java.util.List;

//#if MC < 11700
//$$ import java.util.Arrays;
//$$ import java.util.Collections;
//#endif

public class JavaCompat {
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        //#if MC >= 11700
        return List.of(elements);
        //#else
        //$$ if (elements.length == 0) {
        //$$     return Collections.emptyList();
        //$$ } else {
        //$$     return Collections.unmodifiableList(Arrays.asList(elements));
        //$$ }
        //#endif
    }
}
