package com.example.custom_map_svg_library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.custom_map_svg_library.models.Marker
import com.example.custom_map_svg_library.models.Part
import com.example.custom_map_svg_library.models.Text
import com.example.custom_map_svg_library.utils.DrawingHelper
import com.example.custom_map_svg_library.utils.EventHandler
import com.example.custom_map_svg_library.utils.MarkerBuilder
import com.example.custom_map_svg_library.utils.OnPartClickListener
import com.example.custom_map_svg_library.utils.PartManager
import com.example.custom_map_svg_library.utils.XmlParser

class SchemeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr : Int = 0
) : View(context, attrs, defStyleAttr) {


    //Менеджеры и хелперы для управления частями и рисования
    private val partManager = PartManager()
    private val drawingHelper = DrawingHelper()
    private val xmlParser = XmlParser(context)
    private val eventHandler =  EventHandler(this)

    private val titles : MutableList<Text> = mutableListOf()
    private val markers : MutableList<Marker> = mutableListOf()
    private val partToMarkerMap = mutableMapOf<Part, Marker>()

    var selectedFillColor : Int = DefaultValues.DEFAULT_SELECTED_COLOR
    var selectedStrokeColor : Int = DefaultValues.DEFAULT_SELECTED_STROKE_COLOR
    var strokeWidth : Float? = null
    var strokeAlpha : Float? = null
    var fillAlpha : Float? = null
    var strokeLineCap : String? = null
    var strokeLineJoin : String? = null
    var strokeMiterLimit : Float? = null
    var isEnabledPart : Boolean = true

    var isMultiSelectEnabled: Boolean = false
    private var onPartClickListener : OnPartClickListener? = null

    //Переменные для сохранения текущего касания
    private var relativeX = 0f
    private var relativeY = 0f
    private var isMoving = false

    fun showScheme(xmlName : Int){
        val listParts = xmlParser.parse(xmlName)
        setListParts(listParts)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawingHelper.drawPart(
            canvas,
            partManager.getPart(),
            isMultiSelectEnabled,
            partManager.getSelectedPart(),
            partManager.getSelectedParts(),
            markers,
            titles,
            selectedFillColor, selectedStrokeColor, strokeWidth, strokeAlpha, fillAlpha, strokeLineCap, strokeLineJoin, strokeMiterLimit)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (partManager.getPart().isEmpty()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            var measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
            var measuredHeight = MeasureSpec.getSize(heightMeasureSpec)

            val resizeWidth = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
            val resizeHeight = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY

            val svgWidth = XmlParser.svgRight - XmlParser.svgLeft
            val svgHeight = XmlParser.svgBottom - XmlParser.svgTop
            val svgAspectRatio = svgHeight / svgWidth

            if (resizeWidth && resizeHeight) {
                measuredWidth = svgWidth.toInt()
                measuredHeight = svgHeight.toInt()
            } else if (resizeWidth) {
                measuredWidth = (measuredHeight / svgAspectRatio).toInt()
            } else if (resizeHeight) {
                measuredHeight = (measuredWidth * svgAspectRatio).toInt()
            }


            setMeasuredDimension(measuredWidth, measuredHeight)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                relativeX = event.x
                relativeY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                if(!isMoving){
                    val clickedPart = findClickedPart(relativeX, relativeY)
                    clickedPart?.let {
                        onPartClickListener?.onPartClick(it)
                    }
                }
                isMoving = false
                return true
            }
            MotionEvent.ACTION_MOVE->{
                if(!isMoving && (Math.abs(event.x - relativeX) > TOUCH_SLOP || Math.abs(event.y - relativeY) > TOUCH_SLOP)){
                    isMoving = true
                }
                if(isMoving){
                    val dx = (event.x - relativeX) * SENSITIVITY
                    val dy = (event.y - relativeY) * SENSITIVITY
                    eventHandler.updateTranslation(dx, dy)
                    relativeX = event.x
                    relativeY = event.y
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    // Устанавливает список частей для отображения и вызывает перерисовку представления.
    private fun setListParts(part: List<Part>) {
        partManager.setPart(part)
        invalidate()
    }


    //Блок получения части(частей)
    fun getParts() : List<Part>{
        return partManager.getPart()
    }

    fun getPartById(id: String): Part? {
        return partManager.getPartById(id)
    }

    fun getPartsByName(name: String): List<Part> {
        return partManager.getPartsByName(name)
    }

    fun getPartsByIndex(index: Int): Part? {
        return partManager.getPartByIndex(index)
    }

    // Определяет, на какую часть было совершено касание по координатам x и y.
    private fun findClickedPart(x: Float, y: Float): Part? {
        return partManager.handleTouch(x, y)
    }

    fun clickToPart(part: Part) {
        if (part.isEnabledPart) {
            if (isMultiSelectEnabled) {
                multiSelectPart(part)
            } else {
                selectPart(part)
            }
            invalidate()
        }
    }

    private fun selectPart(part: Part?) {
        partManager.selectPart(part)
    }

    fun deselectPart(part: Part?) {
        partManager.deselectedParts(part)
        invalidate()
    }

    fun multiSelectPart(part: Part?){
        partManager.multiSelectPart(part)
        invalidate()
    }

    fun deselectAllParts(){
        partManager.clearSelectPart()
        invalidate()
    }


    /**
     * Изменяем состояние part на блокировку нажатия
     * @param part Часть, блокировку которой нужно изменить
     * @param id Поиск части по id и изменение блокировки.
     */
    fun togglePartEnabled(part : Part){
        part.isEnabledPart = !isEnabledPart
    }

    fun togglePartEnabled(id: String) {
        val part = partManager.getPartById(id)
        if (part != null) {
            part.isEnabledPart = !isEnabledPart
        } else {
            Log.e("blockPart", "Cannot block part because not found id: $id")
        }
    }

    // Заполняет указанные части указанным цветом и вызывает перерисовку представления.
    fun fillPartsWithColor(color: Int, parts : List<Part>){
        partManager.setFillColorParts(color, parts)
        invalidate()
    }

    fun fillPartsWithColorByIds(color: Int, ids : List<String>){
        partManager.setFillColorPartsByIds(color, ids)
        invalidate()
    }

    /**
     * Изменяет цвет указанной части.
     * @param part Часть, цвет которой нужно изменить.
     * @param color Цвет, на который необходимо изменить часть.
     */
    fun changeColorPart(part: Part, color: Int){
        if (partManager.changeColorPart(part, color)) {
            invalidate()
        }
    }
    /**
     * Изменяет цвет части по её идентификатору.
     * @param partId Идентификатор части, цвет которой нужно изменить.
     * @param color Цвет, на который необходимо изменить часть.
     */
    fun changeColorPart(partId: String, color: Int){
        if (partManager.changeColorPart(partId, color)) {
            invalidate()
        }
    }


    /**
     * Масштабирует вид к заданной части.
     * @param part Часть, к которой нужно масштабировать
     */
    fun zoomToPart(part: Part) {
        val widthRegion = part.region.bounds.width()
        val heightRegion = part.region.bounds.height()
        val centerX = part.region.bounds.centerX()
        val centerY = part.region.bounds.centerY()
        eventHandler.zoomInRegion(relativeX, relativeY, widthRegion, heightRegion, centerX, centerY)
    }


    //Блок изменения масштаба view.
    fun zoomIn() {
        val targetScale = scaleX * ZOOM_IN_FACTOR
        val clampedScale = Math.max(1f, Math.min(targetScale, 10f))
        eventHandler.zoomToScale(clampedScale)
    }

    fun zoomOut() {
        val targetScale = scaleX * ZOOM_OUT_FACTOR
        val clampedScale = Math.max(1f, Math.min(targetScale, 10f))
        eventHandler.zoomToScale(clampedScale)
    }

    fun resetZoom() {
        eventHandler.zoomToScale(ZOOM_RESET_FACTOR)
    }

    /**
     * Добавляет текст, центрированный относительно части модели данных или по указанным координатам.
     *
     * @param text Текст для добавления, можно опустить, если в модели Part имеется title
     * @param part Модель данных Part, к которой добавляется текст.
     *             Текст центрируется относительно этой части.
     * @param x Координата X центра текста (если part не указан).
     * @param y Координата Y центра текста (если part не указан).
     * @param paint Paint для стиля текста.
     */


    fun addText(text: String, x: Float, y: Float, paint: Paint) {
        titles.add(Text(x, y, text, paint))
        invalidate()
    }

    fun addText(title: String, part: Part, paint: Paint) {
        val x = part.region.bounds.exactCenterX()
        val y = part.region.bounds.exactCenterY()
        val text = Text(x, y, title, paint)
        titles.add(text)
        invalidate()
    }

    fun addText(part: Part, paint: Paint) {
        val x = part.region.bounds.exactCenterX()
        val y = part.region.bounds.exactCenterY()
        val title = part.name
        val text = Text(x, y, title, paint)
        titles.add(text)
        invalidate()
    }



    /**
     * Добавляет маркер (круг или изображение) и центрирует его относительно части модели данных или по указанным координатам.
     *
     * @param drawable Ресурс изображения для отрисовки (формат png, jpeg, jpg).
     *                Оставьте этот параметр пустым для отрисовки метки круга.
     * @param radius Радиус круга или размер стороны квадрата метки в пикселях.
     * @param part Модель данных Part, к которой добавляется метка.
     *             Метка центрируется относительно этой части.
     * @param x Координата X центра маркера (если part не указан).
     * @param y Координата Y центра маркера (если part не указан).
     * @param paint Paint для стиля маркера.
     */

    fun addMarker(x: Float, y: Float, radius: Float, paint: Paint?) : Marker {
        val marker = MarkerBuilder(x, y, radius)
            .setPaint(paint)
            .build()
        markers.add(marker)
        invalidate()
        return marker
    }

    fun addMarker(part: Part, radius: Float, paint: Paint?) : Marker{
        val x = part.region.bounds.exactCenterX()
        val y = part.region.bounds.exactCenterY()
        val marker = MarkerBuilder(x, y, radius)
            .setPaint(paint)
            .build()
        markers.add(marker)
        invalidate()
        return marker
    }

    fun addMarker(part: Part, radius: Float, drawable: Drawable?) : Marker {
        if(drawable != null){
            val bounds = part.region.bounds
            val x = bounds.exactCenterX() - (radius / 2)
            val y = bounds.exactCenterY() - (radius / 2)
            val marker = MarkerBuilder(x, y, radius)
                .setDrawable(drawable)
                .build()
            markers.add(marker)
            invalidate()
            return marker
        }
        else{
            return addMarker(part, radius, paint = null)
        }
    }

    fun addMarker(x: Float, y: Float, radius: Float, drawable: Drawable?) : Marker {
        if(drawable != null){
            val marker = MarkerBuilder(x, y, radius)
                .setDrawable(drawable)
                .build()
            markers.add(marker)
            return marker
        }
        else{
            return addMarker(x, y, radius = radius, paint = null)
        }
    }

    fun removeMarker(marker: Marker) {
        markers.remove(marker)
        invalidate()
    }

    fun removeAllMarkers(){
        markers.clear()
        invalidate()
    }



    fun setOnPartClickListener(listener: OnPartClickListener) {
        onPartClickListener = listener
    }

    companion object{
        private const val TOUCH_SLOP = 25
        private const val ZOOM_IN_FACTOR = 1.5f
        private const val ZOOM_OUT_FACTOR = 0.5f
        private const val ZOOM_RESET_FACTOR = 1f
        private const val SENSITIVITY = 1.2f
    }
}