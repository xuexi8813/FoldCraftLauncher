package com.tungsten.fcl.library.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLMenuView;
import com.tungsten.fcllibrary.util.ConvertUtils;

/**
 * [mio] 澪启动器风格侧栏条目：左侧图标 + 右侧文字标签。
 * 复刻澪侧栏视觉：常态透明底 + 暖灰文字；选中 = 主题色实心条 + 白字。
 * 图标可选像素模式（mioPixelIcon，MC 像素图标不做 tint）或跟随主题 tint。
 * 继承 FCLMenuView 以保持 MainActivity 的选中/监听逻辑完全兼容。
 */
public class MioMenuView extends FCLMenuView {

    private String label = "";
    private boolean pixelIcon = false;
    private boolean selected = false;

    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect iconRect = new Rect();

    public MioMenuView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public MioMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MioMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MioMenuView);
            label = ta.getString(R.styleable.MioMenuView_mioLabel);
            pixelIcon = ta.getBoolean(R.styleable.MioMenuView_mioPixelIcon, false);
            ta.recycle();
        }
        if (label == null) label = "";
        setPadding(
                ConvertUtils.dip2px(getContext(), 12f),
                ConvertUtils.dip2px(getContext(), 4f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 4f)
        );
        setScaleType(ScaleType.FIT_XY);
        textPaint.setTextSize(ConvertUtils.dip2px(getContext(), 15f));
        textPaint.setFakeBoldText(true);
        // 接管父类注册的主题刷新回调（WeakHashMap 同键覆盖），避免父类 tint 逻辑反复污染像素图标
        ThemeEngine.getInstance().registerEvent(this, this::refreshMioTheme);
        refreshMioTheme();
    }

    /** 澪风格主题刷新：选中/常态的背景与文字色（覆盖父类的纯图标 tint 逻辑） */
    private void refreshMioTheme() {
        int[][] state = {{}};
        if (selected) {
            // 选中：主题色实心条
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.RECTANGLE);
            bg.setColor(ThemeEngine.getInstance().getTheme().getColor());
            setBackgroundDrawable(bg);
        } else {
            // 常态：透明底 + 主题色 ripple
            RippleDrawable ripple = new RippleDrawable(
                    new ColorStateList(state, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()}),
                    null, null);
            ripple.setRadius(ConvertUtils.dip2px(getContext(), 8));
            setBackgroundDrawable(ripple);
        }
        // 像素图标不 tint（ImageView 的 tint 会 mutate 进 drawable，需同时清 drawable 自身 tint）；
        // 矢量图标与文字用 primary_text（澪的暖灰/夜间白），选中态一律白字（在主题色条上）
        int normalColor = ContextCompat.getColor(getContext(), R.color.primary_text);
        if (pixelIcon) {
            setImageTintList(null);
            android.graphics.drawable.Drawable d = getDrawable();
            if (d != null) {
                d.mutate().setTintList(null);
            }
        } else {
            setImageTintList(new ColorStateList(state, new int[]{normalColor}));
        }
        textPaint.setColor(selected ? Color.WHITE : normalColor);
        invalidate();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        // 复用父类逻辑触发 OnSelectListener 回调
        super.setSelected(selected);
        refreshMioTheme();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        // 不调 super：父类 FIT_XY 铺满绘制不适合"图标+文字"横排，这里自行布局
        android.graphics.drawable.Drawable drawable = getDrawable();
        int height = getHeight();
        int viewWidth = getWidth();
        int padL = getPaddingLeft();
        int padT = getPaddingTop();
        int padB = getPaddingBottom();
        int iconSize = height - padT - padB;
        int iconRight = padL;
        if (drawable != null && iconSize > 0) {
            iconRect.set(padL, padT, padL + iconSize, padT + iconSize);
            drawable.setBounds(iconRect);
            drawable.draw(canvas);
            iconRight = iconRect.right;
        }
        if (!label.isEmpty() && viewWidth - iconRight > 0) {
            int textX = iconRight + ConvertUtils.dip2px(getContext(), 10f);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float baseline = height / 2f - (fm.ascent + fm.descent) / 2f;
            canvas.drawText(label, textX, baseline, textPaint);
        }
    }
}
