package msc4791.marquee

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MarqueeTextView: AppCompatTextView {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView).run {
            marqueeDirection = getInteger(R.styleable.MarqueeTextView_marquee_direction, MARQUEE_LEFT)
            expandToParent = getBoolean(R.styleable.MarqueeTextView_expand_to_parent, false)
            recycle()
        }
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView).run {
            marqueeDirection = getInteger(R.styleable.MarqueeTextView_marquee_direction, MARQUEE_LEFT)
            expandToParent = getBoolean(R.styleable.MarqueeTextView_expand_to_parent, false)
            recycle()
        }
    }

    private var scrollValue: Float
    var marqueeDirection: Int
    var expandToParent: Boolean

    init {
        ellipsize = null
        isSingleLine = true

        scrollValue = 0f
        marqueeDirection = MARQUEE_LEFT
        expandToParent = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(expandToParent) {
            if (marqueeDirection == MARQUEE_UP || marqueeDirection == MARQUEE_DOWN) {
                val copyCount = context.resources.displayMetrics.heightPixels / layout.height
                if(copyCount > 2) {
                    text = text.toString().plus("\n").repeat(copyCount + 1).substringBeforeLast("\n")
                    isSingleLine = false
                }
            }
            if (marqueeDirection == MARQUEE_LEFT || marqueeDirection == MARQUEE_RIGHT) {
                val copyCount = (context.resources.displayMetrics.widthPixels / layout.getLineWidth(0)).toInt()
                if(copyCount > 2) {
                    text = text.toString().plus(" ").repeat(copyCount + 1)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        when(marqueeDirection) {
            MARQUEE_UP -> {
                canvas?.translate(0f, -scrollValue)
            }
            MARQUEE_DOWN -> {
                canvas?.translate(0f, scrollValue)
            }
            MARQUEE_LEFT -> {
                canvas?.translate(-scrollValue, 0f)
            }
            MARQUEE_RIGHT -> {
                canvas?.translate(scrollValue, 0f)
            }
        }
        layout.draw(canvas)
        canvas?.restore()
        when(marqueeDirection) {
            MARQUEE_UP -> {
                canvas?.translate(0f, -scrollValue + layout.height)
            }
            MARQUEE_DOWN -> {
                canvas?.translate(0f, scrollValue - layout.height)
            }
            MARQUEE_LEFT -> {
                canvas?.translate(- scrollValue + layout.getLineWidth(0), 0f)
            }
            MARQUEE_RIGHT -> {
                canvas?.translate(scrollValue - layout.getLineWidth(0), 0f)
            }
        }
        layout.draw(canvas)

        scrollValue += (MARQUEE_PIXELS_PER_SECOND * context.resources.displayMetrics.density) / MARQUEE_RESOLUTION
        when(marqueeDirection) {
            MARQUEE_UP -> {
                if(scrollValue > layout.height) {
                    scrollValue = 0f
                }
            }
            MARQUEE_DOWN -> {
                if(scrollValue > layout.height) {
                    scrollValue = 0f
                }
            }
            MARQUEE_LEFT -> {
                if(scrollValue > layout.getLineWidth(0)) {
                    scrollValue = 0f
                }
            }
            MARQUEE_RIGHT -> {
                if(scrollValue > layout.getLineWidth(0)) {
                    scrollValue = 0f
                }
            }
        }
        invalidate()

        super.onDraw(canvas)
    }

    companion object {
        private const val MARQUEE_RESOLUTION = 1000 / 30
        private const val MARQUEE_PIXELS_PER_SECOND = 30
        const val MARQUEE_UP = 0
        const val MARQUEE_DOWN = 1
        const val MARQUEE_LEFT = 2
        const val MARQUEE_RIGHT = 3
    }
}