package com.omsi.composetemplate.ui.utils

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.*


@OptIn(ExperimentalTextApi::class)
@Composable
fun GaugeMeter(
    currentValue:Int,
    minValue:Int=0,
    maxValue:Int=100,
    textSize: TextUnit = 20.sp,
    udm:String="",
    bigStepValue : Int = 10,
    smallStepValue : Int = 2,
    modifier: Modifier =Modifier){

    val currentPercentage = (currentValue.coerceAtLeast(minValue)-minValue)*1F/(maxValue-minValue)
    val currentSteps = (currentValue.coerceAtLeast(minValue)-minValue)*1F/smallStepValue.coerceAtLeast(1)
    val numberOfMarkers = ((maxValue-minValue)*1f/smallStepValue+1).roundToInt()
    val arcDegrees = 275F //max length
    val startArcAngle = 135f //starts from vertical axis
    val startStepAngle = startArcAngle.toInt()-180 //different ref sys for markers
    val degreesMarkerStep = arcDegrees / (numberOfMarkers-1)
    val arcStrokeWidth = 20f

    //Colors
    val (mainColor, secondaryColor) = when {
        currentPercentage*100 < 90 -> Color(0xFF388E3C) to Color(0xFFC8E6C9)
        currentPercentage*100 < 95 -> Color(0xFFF57C00) to Color(0xFFFFE0B2)
        else -> Color(0xFFD32F2F) to Color(0xFFFFCDD2)
    }
    val paint = Paint().apply {
        color = mainColor
    }

    val textPaint = android.graphics.Paint().apply {
        this.textSize = textSize.value
        this.color = Color.Black.toArgb()
        this.textAlign = android.graphics.Paint.Align.CENTER
    }


    Box(modifier = modifier){
        Canvas(
            modifier= Modifier
                .aspectRatio(1f)
                .fillMaxSize(),
            onDraw = {
                drawIntoCanvas { canvas->
                    val w = drawContext.size.width
                    val h = drawContext.size.height
                    val centerOffset = Offset(w/2f, h/2f)
                    val centerArcSize = Size(w/1f-arcStrokeWidth, h/1f-arcStrokeWidth )
                    val centerArcOffset = Offset(centerOffset.x - centerArcSize.width/2f, centerOffset.y - centerArcSize.height/2f)
                    val centerArcStroke = Stroke(arcStrokeWidth, 0f, StrokeCap.Butt)
                    val textRadius = centerArcSize.width/2-1.3f*textSize.value

                    //Drawing external Arc
                    drawArc(
                        secondaryColor,
                        startArcAngle,
                        arcDegrees.toFloat(),
                        false,
                        topLeft = centerArcOffset,
                        size = centerArcSize,
                        style = centerArcStroke
                    )
                    drawArc(
                        mainColor,
                        startArcAngle,
                        (currentPercentage*arcDegrees),
                        false,
                        topLeft = centerArcOffset,
                        size=centerArcSize,
                        style = centerArcStroke
                    )
                    //Drawing the pointer circle
                 //   drawCircle(mainColor, 50f, centerOffset)
                 //   drawCircle(Color.White, 25f, centerOffset)
                    drawCircle(Color.Black, w/30, centerOffset)

                    //Drawing Line Markers
                  //  for((counter, degrees) in (startStepAngle..(startStepAngle+arcDegrees) step degreesMarkerStep).withIndex()){
                    for(counter in 0 until numberOfMarkers){
                        val degrees = counter*degreesMarkerStep+startStepAngle
                        val value = counter*smallStepValue+minValue
                        val lineStartX = 0f
                        paint.color = Color.Black

                        val lineEndX = if (value%bigStepValue == 0){
                            paint.strokeWidth = 3f
                            1.3f*textSize.value
                        } else{
                            paint.strokeWidth = 1f
                            textSize.value
                        }
                        canvas.save()
                        canvas.rotate(degrees.toFloat(), w/2f, h/2f)
                        canvas.drawLine(
                            Offset(lineStartX, h/2f),
                            Offset(lineEndX, h/2f),
                            paint
                        )
                        canvas.restore()

                        //Drawing values
                        if (value%bigStepValue == 0) {
                            val angleRad = degrees /180f * PI
                            val text = "$value"
                            val bounds = Rect()
                            textPaint.getTextBounds(text,0, text.length, bounds) //textSize*floor(log10(counter.toFloat()) + 1)
                            val textWidth = bounds.width()

                            drawContext.canvas.nativeCanvas.apply {
                                drawText(
                                    text,
                                    w / 2f - textRadius* cos(angleRad.toFloat())+textWidth/2*cos(angleRad.toFloat()),
                                    w / 2f - textRadius * sin(angleRad.toFloat()) + bounds.height() / 2,
                                    textPaint
                                )
                            }
                        }
                    }

                    //Drawing Indicator
                    canvas.save()
                    val degrees = currentSteps*degreesMarkerStep+startStepAngle
                    canvas.rotate(degrees, w/2f, h/2f)
                    paint.color = Color.Black
                    canvas.drawPath(
                        Path().apply {
                            moveTo(w/2, (h/2)-5)
                            lineTo(w/2, h/2 + 5)
                            lineTo(w/10f, h/2)
                            lineTo(w/2, h/2 - 5)
                            close()
                        },
                        paint
                    )
                    canvas.restore()

                }
            }
        )
    }
}

@Preview(widthDp = 110, heightDp = 110)
@Composable
fun GaugeMeterPreview(){
    GaugeMeter(65)
}