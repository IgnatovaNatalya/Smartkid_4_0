package ru.mamsikgames.smartkid.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import ru.mamsikgames.smartkid.R


class TextViewOutline @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    AppCompatTextView(
        context!!, attrs
    ) {
    // data
    private var mOutlineSize = 0
    private var mOutlineColor = 0
    private var mTextColor = 0

    private var mShadowRadius = 0f
    private var mShadowDx = 0f
    private var mShadowDy = 0f
    private var mShadowColor = 0

    private var mShadow2Radius = 0f
    private var mShadow2Dx = 0f
    private var mShadow2Dy = 0f
    private var mShadow2Color = 0

    private var mShaderBackground = 0f

    init {
        setAttributes(attrs)
    }

    private fun setAttributes(attrs: AttributeSet?) {
        // set defaults
        mOutlineSize = DEFAULT_OUTLINE_SIZE
        mOutlineColor = DEFAULT_OUTLINE_COLOR
        // text color
        mTextColor = currentTextColor
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.TextViewOutline)
            // outline size
            if (a.hasValue(R.styleable.TextViewOutline_outlineSize)) {
                mOutlineSize = a.getDimension(
                    R.styleable.TextViewOutline_outlineSize,
                    DEFAULT_OUTLINE_SIZE.toFloat()
                ).toInt()
            }
            // outline color
            if (a.hasValue(R.styleable.TextViewOutline_outlineColor)) {
                mOutlineColor =
                    a.getColor(R.styleable.TextViewOutline_outlineColor, DEFAULT_OUTLINE_COLOR)
            }
            // shadow (the reason we take shadow from attributes is because we use API level 15 and only from 16 we have the get methods for the shadow attributes)
            if (a.hasValue(R.styleable.TextViewOutline_android_shadowRadius)
                || a.hasValue(R.styleable.TextViewOutline_android_shadowDx)
                || a.hasValue(R.styleable.TextViewOutline_android_shadowDy)
                || a.hasValue(R.styleable.TextViewOutline_android_shadowColor)
            ) {
                mShadowRadius = a.getFloat(R.styleable.TextViewOutline_android_shadowRadius, 0f)
                mShadowDx = a.getFloat(R.styleable.TextViewOutline_android_shadowDx, 0f)
                mShadowDy = a.getFloat(R.styleable.TextViewOutline_android_shadowDy, 0f)
                mShadowColor =
                    a.getColor(R.styleable.TextViewOutline_android_shadowColor, Color.TRANSPARENT)

                mShadow2Radius = a.getFloat(R.styleable.TextViewOutline_shadow2Radius, 0f)
                mShadow2Dx = a.getFloat(R.styleable.TextViewOutline_shadow2Dx, 0f)
                mShadow2Dy = a.getFloat(R.styleable.TextViewOutline_shadow2Dy, 0f)
                mShadow2Color =
                    a.getColor(R.styleable.TextViewOutline_shadow2Color, Color.TRANSPARENT)

                mShaderBackground = a.getFloat(R.styleable.TextViewOutline_shaderBackground, 0f)
            }
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPaintToOutline()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun setPaintToShadow2() {
        val paint: Paint = paint
        paint.shader = null
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = mOutlineSize.toFloat()
        super.setTextColor(mShadow2Color)
        super.setShadowLayer(mShadow2Radius, mShadow2Dx, mShadow2Dy, mShadow2Color)
        //super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    }

    private fun setPaintToOutline() {
        val paint: Paint = paint
        paint.shader = null
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = mOutlineSize.toFloat()
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        super.setTextColor(mOutlineColor)
        super.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
        //super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    }

    /*private fun setPaintToRegular() {
        val paint: Paint = paint
        paint.shader = null
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f
        super.setTextColor(mTextColor)
        super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
        //super.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
    }*/

    private fun setPaintToDraw() {
        val paint:Paint = paint

        if(mShaderBackground>0) {
            paint.style = Paint.Style.FILL
            paint.strokeWidth = 0f
            val bitmapOneBg: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.bg_title_text2)
            if (bitmapOneBg!=null)
                paint.shader = BitmapShader(bitmapOneBg, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
        }
        else {
            paint.style = Paint.Style.FILL
            paint.strokeWidth = 0f
            super.setTextColor(mTextColor)
            super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
        }
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        mTextColor = color
    }

    /*fun setOutlineSize(size: Int) {
        mOutlineSize = size
    }*/

    /*fun setOutlineColor(color: Int) {
        mOutlineColor = color
    }*/

    override fun onDraw(canvas: Canvas) {
        setPaintToShadow2()
        super.onDraw(canvas)

        setPaintToOutline()
        super.onDraw(canvas)

        //setPaintToRegular()

        setPaintToDraw()
        super.onDraw(canvas)
    }


    companion object {
        // constants
        private const val DEFAULT_OUTLINE_SIZE = 0
        private const val DEFAULT_OUTLINE_COLOR = Color.TRANSPARENT
    }
}