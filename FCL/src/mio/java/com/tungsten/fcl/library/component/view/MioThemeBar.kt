package com.tungsten.fcl.library.component.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * [mio] 澪式主题色横条：以主题色实心填充自身（澪的顶部工具栏语言）。
 * 自注册 ThemeEngine 刷新，主题变化时跟随换色，无需外部挂钩。
 */
class MioThemeBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        ThemeEngine.getInstance().registerEvent(this) {
            paint.color = ThemeEngine.getInstance().getTheme().getColor()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}
