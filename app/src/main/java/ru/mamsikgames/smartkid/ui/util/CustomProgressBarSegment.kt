package ru.mamsikgames.smartkid.ui.util


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import ru.mamsikgames.smartkid.R

class CustomProgressBarSegment @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Цвета по умолчанию
    private var bgColor = Color.parseColor("#E8F8F5")   // Светло-голубой фон
    private var progressColor = Color.parseColor("#2ECC71") // Яркий салатовый
    private var trackColor = Color.parseColor("#E8F8F5")   // Светло-голубой
    private var borderColor = Color.parseColor("#3498DB")  // Голубая граница
    private var borderWidth = 4f.dpToPx()                  // Толщина границы
    private var bgCornerRadius = 8f.dpToPx()               // Закругление фона

    private var progressCornerRadius = 6f.dpToPx()                 // Закругление углов
    private var segmentGap = 2f.dpToPx()                   // Промежуток между сегментами
    private var progressPadding = 6f.dpToPx()              // Отступ прогресса от границ

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = trackColor
        style = Paint.Style.FILL
    }


    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = bgColor
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }

    private var progress = 1f // 0..1

    private val bgRect = RectF()
    private val progressRect = RectF() //а сегменты?

    init {
        // Чтение кастомных атрибутов из XML
        context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar).run {
            try {
                bgColor = getColor(R.styleable.CustomProgressBar_bgColor, bgColor)
                progressColor = getColor(R.styleable.CustomProgressBar_progressColor, progressColor)
                trackColor = getColor(R.styleable.CustomProgressBar_progressColor, trackColor)
                borderColor = getColor(R.styleable.CustomProgressBar_borderColor, borderColor)

                borderWidth = getDimension(R.styleable.CustomProgressBar_borderWidth, borderWidth)
                bgCornerRadius =
                    getDimension(R.styleable.CustomProgressBar_bgCornerRadius, bgCornerRadius)
                progressCornerRadius =
                    getDimension(R.styleable.CustomProgressBar_bgCornerRadius, progressCornerRadius)
                progressPadding =
                    getDimension(R.styleable.CustomProgressBar_progressPadding, progressPadding)
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

    private fun updateRects() { //еще сегменты надо
        // Фон (включая границу)
        bgRect.set(1f.dpToPx(), 1f.dpToPx(), width.toFloat() - 1f.dpToPx(), height.toFloat() - 1f.dpToPx())

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

        val width = width.toFloat()
        val height = height.toFloat()

        // 1. Рисуем фон
        canvas.drawRoundRect(bgRect, bgCornerRadius, bgCornerRadius, bgPaint)

        // 2. Рисуем границу
        if (borderWidth > 0) {
            canvas.drawRoundRect( bgRect,
                bgCornerRadius, bgCornerRadius,
                borderPaint)
        }

        // 2. Рисуем прогресс (сегментированный)
        val segmentCount = 10 // Количество сегментов
        val segmentWidth = (width - (segmentCount - 1) * segmentGap) / segmentCount

        for (i in 0 until segmentCount) {
            val segmentStart = i * (segmentWidth + segmentGap) + progressPadding
            val segmentProgress = (progress * segmentCount - i).coerceIn(0f, 1f)

            if (segmentProgress > 0) {
                val segmentRect = RectF(
                    segmentStart,
                    progressPadding,
                    segmentStart +  segmentWidth  * segmentProgress - segmentGap/2,
                    height - progressPadding
                )
                canvas.drawRoundRect(
                    segmentRect,
                    progressCornerRadius,
                    progressCornerRadius,
                    progressPaint
                )
            }
        }

    }

    // Установка прогресса с анимацией
    fun setProgress(value: Float, animate: Boolean = true) {
        if (animate) {
            ValueAnimator.ofFloat(progress, value).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animator ->
                    progress = animator.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            progress = value
            invalidate()
        }
    }

    // Анимация успеха (мигание + подпрыгивание)
    fun playSuccessAnimation() {
        // 1. Подпрыгивание
        animate()
            .translationY(-10f)
            .setDuration(200)
            .withEndAction { animate().translationY(0f).setDuration(200).start() }
            .start()

        // 2. Мигание белым
        val originalColor = progressPaint.color
        ValueAnimator.ofArgb(progressColor, Color.WHITE, progressColor).apply {
            duration = 600
            addUpdateListener { animator ->
                progressPaint.color = animator.animatedValue as Int
                invalidate()
            }
            doOnEnd { progressPaint.color = originalColor }
            start()
        }
    }

    // Конвертация dp в пиксели
    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density
}

