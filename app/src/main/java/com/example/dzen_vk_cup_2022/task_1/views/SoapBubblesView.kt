package com.example.dzen_vk_cup_2022.task_1.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_1.repositories.CategoriesRepository
import java.lang.Integer.max
import kotlin.properties.Delegates


class SoapBubblesView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val displayWidth: Int
    private val displayHeight: Int

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, R.style.GlobalSoapBubblesFieldStyle)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.soapBubblesFieldStyle)
    constructor(context: Context) : this(context, null)

    private var backgroundColorButton by Delegates.notNull<Int>()
    private var selectColorButton by Delegates.notNull<Int>()
    private var borderColorButton by Delegates.notNull<Int>()
    private var textColor by Delegates.notNull<Int>()
    private lateinit var paintCircle: Paint
    private lateinit var paintFillCircle: Paint
    private lateinit var paintFillCircleSelected: Paint
    private lateinit var paintText: Paint

    private val repository = CategoriesRepository()
    private lateinit var circleMaker: CircleMaker
    private val listCategorySelected = mutableListOf<String>()
    var listCategory = listOf<String>()
        set(value) {
            field = value
            circleMaker = CircleMaker(resources, value, DELTA, displayWidth, displayHeight)
            requestLayout()
            invalidate()
        }

    init {
        initialAttributes(attrs, defStyleAttr, defStyleRes)
        initPaint()
        if (isInEditMode) {
            listCategory =
                listOf("Политика", "Новости", "Наука и образование", "История", "Литература", "Изотерика", "Исскуство", "Автомобили", "Хобби")
        }
        val dm = DisplayMetrics()
        context.display?.getMetrics(dm)
        displayWidth = dm.widthPixels
        displayHeight = dm.heightPixels
    }

    private fun initialAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SoapBubblesView, defStyleAttr, defStyleRes)
        backgroundColorButton = typedArray.getColor(R.styleable.SoapBubblesView_colorButton, BACKGROUND_COLOR_BUTTON)
        selectColorButton = typedArray.getColor(R.styleable.SoapBubblesView_colorButtonSelected, SELECT_COLOR_BUTTON)
        borderColorButton = selectColorButton
        typedArray.recycle()
    }

    private fun initPaint() {
        paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
        paintCircle.color = borderColorButton
        paintCircle.style = Paint.Style.STROKE
        paintCircle.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)

        paintFillCircle = Paint()
        paintFillCircle.color = backgroundColorButton
        paintFillCircle.style = Paint.Style.FILL

        paintFillCircleSelected = Paint()
        paintFillCircleSelected.color = selectColorButton
        paintFillCircleSelected.style = Paint.Style.FILL

        paintText = Paint()

        val typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
        paintText.typeface = typeface
        paintText.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
        paintText.color = Color.WHITE
    }

    private var imagePaint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val listCircle = circleMaker.getCircles()
        listCircle.forEach {
            //Рисуем окружность
            canvas?.drawCircle(it.a, it.b, it.r, paintCircle)
            if (listCategorySelected.contains(it.text)) {
                canvas?.drawCircle(it.a, it.b, it.r, paintFillCircleSelected)
            } else {
                canvas?.drawCircle(it.a, it.b, it.r, paintFillCircle)
            }

            //Рисуем текст
            canvas?.drawText(it.text, it.a - getPixelsFromDP((it.text.length * 3.5f)), it.b + (it.r / 2), paintText)

            //Рисуем круг
            val image = repository.mapPictures[it.text]!!
            val bitmapNew = getBitmap(
                image,
                width = ((it.r * 2) / 3).toInt()
            )  //TODO Получить bitmap нужно заранее, вне метода onDraw (FPS может проседать, но пока всё норм)
            canvas?.drawBitmap(bitmapNew, it.a - (bitmapNew.width / 2), it.b - (bitmapNew.height - it.r / 5), imagePaint);
        }
    }

    private fun getBitmap(res: Int, width: Int): Bitmap {
        val myBitmap = BitmapFactory.decodeResource(resources, res)
        val scale: Float = (myBitmap.height.toFloat() / myBitmap.width).coerceAtMost(width.toFloat() / myBitmap.height)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.width, myBitmap.height, matrix, true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                circleMaker.getCircles().forEach {
                    if (belongingOfCirclePoint(it, event.x, event.y)) { //TODO Нужно упростить поиск нажатого элемента (чтобы не перебирать все)
                        if (listCategorySelected.contains(it.text)) {
                            listCategorySelected.remove(it.text)
                        } else {
                            listCategorySelected.add(it.text)
                        }
                        return@forEach
                    }
                }
                invalidate()
            }
        }
        return false
    }

    fun getListCategorySelected(): MutableList<String> = listCategorySelected

    /**
     * Вызывается по факту - когда компановщик назначил размер
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * Сдесь есть шанс договориться с компановщиком
     * Вызывается когда компановщик хочет измерить размер View
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minimumWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minimumHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val sumHeightCircle: Float = circleMaker.getCircles().maxOf { it.b + it.r + DELTA }

        val desiredWidth = max(sumHeightCircle.toInt(), minimumWidth)
        val desiredHeight = max(sumHeightCircle.toInt(), minimumHeight)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    private fun getPixelsFromDP(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

    companion object {
        const val BACKGROUND_COLOR_BUTTON = Color.YELLOW
        const val SELECT_COLOR_BUTTON = Color.GRAY
        const val DELTA = 50f
    }
}