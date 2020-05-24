
package com.jh.automatic_titrator.ui.chat.renderer;

import com.jh.automatic_titrator.ui.chat.utils.ViewPortHandler;

public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
