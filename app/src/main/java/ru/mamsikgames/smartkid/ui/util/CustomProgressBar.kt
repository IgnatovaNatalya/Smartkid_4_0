package ru.mamsikgames.smartkid.ui.util

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ru.mamsikgames.smartkid.R


class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Параметры по умолчанию
    private var bgColor = Color.parseColor("#E8F8F5")   // Светло-голубой фон
    private var progressColor = Color.parseColor("#2ECC71") // Яркий салатовый
    private var borderColor = Color.parseColor("#3498DB")  // Голубая граница
    private var borderWidth = 2f.dpToPx()                  // Толщина границы
    private var bgCornerRadius = 8f.dpToPx()               // Закругление фона
    private var progressCornerRadius = 6f.dpToPx()         // Закругление прогресса
    private var progressPadding = 4f.dpToPx()              // Отступ прогресса от границ

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = bgColor
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
        color = borderColor
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = progressColor
    }

    private var progress = 0.5f // 0..1

    private val bgRect = RectF()
    private val progressRect = RectF()

    init {
        // Чтение кастомных атрибутов из XML
        context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar).run {
            try {
                bgColor = getColor(R.styleable.CustomProgressBar_bgColor, bgColor)
                progressColor = getColor(R.styleable.CustomProgressBar_progressColor, progressColor)
                borderColor = getColor(R.styleable.CustomProgressBar_borderColor, borderColor)
                borderWidth = getDimension(R.styleable.CustomProgressBar_borderWidth, borderWidth)
                bgCornerRadius = getDimension(R.styleable.CustomProgressBar_bgCornerRadius, bgCornerRadius)
                progressCornerRadius = getDimension(R.styleable.CustomProgressBar_progressCornerRadius, progressCornerRadius)
                progressPadding = getDimension(R.styleable.CustomProgressBar_progressPadding, progressPadding)
            } finally {
                recycle()
            }
        }

        bgPaint.color = bgColor
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        progressPaint.color = progressColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateRects()
    }

    private fun updateRects() {
        // Фон (включая границу)
        bgRect.set(0f, 0f, width.toFloat(), height.toFloat())

        // Прогресс (с отступами)
        progressRect.set(
            progressPadding,
            progressPadding,
            progressPadding + (width - 2 * progressPadding) * progress,
            height - progressPadding
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. Рисуем фон
        canvas.drawRoundRect(bgRect, bgCornerRadius, bgCornerRadius, bgPaint)

        // 2. Рисуем границу
        if (borderWidth > 0) {
            canvas.drawRoundRect( bgRect,
                 bgCornerRadius, bgCornerRadius,
                 borderPaint)
        }

        // 3. Рисуем прогресс (если есть)
        if (progress > 0) {
            canvas.drawRoundRect(progressRect, progressCornerRadius, progressCornerRadius, progressPaint)
        }
    }

    fun setProgress(value: Float, animate: Boolean = false) {
        progress = value.coerceIn(0f, 1f)
        updateRects()
        invalidate()
    }

    private fun Float.dpToPx(): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        resources.displayMetrics
    )
        //this * resources.displayMetrics.density
}