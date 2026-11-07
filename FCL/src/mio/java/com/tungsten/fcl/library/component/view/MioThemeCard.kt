package com.tungsten.fcl.library.component.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * [mio] 澪式主题色卡片：以主题色实心圆角填充自身（澪右下"开始游戏"块语言）。
 * 继承 ConstraintLayout，替换 version_card 等容器类名时保持 ID 与点击行为兼容。
 * 自注册 ThemeEngine 刷新，主题变化时跟随换色。
 */
class MioThemeCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // 澪的启动块为直角矩形（layout_startgame 无 corners）
    private val radiusPx = 0f

    init {
        // ViewGroup 默认 willNotDraw=true，需显式开启自定义绘制
        setWillNotDraw(false)
        ThemeEngine.getInstance().registerEvent(this as View) {
            paint.color = ThemeEngine.getInstance().getTheme().getColor()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            0f, 0f, width.toFloat(), height.toFloat(),
            radiusPx, radiusPx, paint
        )
    }
}
