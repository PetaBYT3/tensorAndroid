package com.xliiicxiv.tensor.template

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath

class PolygonShape(
    private val polygon: RoundedPolygon,
    private val matrix: Matrix = Matrix()
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val androidPath = android.graphics.Path()
        polygon.toPath(androidPath)

        val composePath = androidPath.asComposePath()

        val bounds = composePath.getBounds()
        val scaleX = size.width / bounds.width
        val scaleY = size.height / bounds.height

        matrix.reset()
        matrix.scale(scaleX, scaleY)
        matrix.translate(-bounds.left, -bounds.top)
        composePath.transform(matrix)

        return Outline.Generic(composePath)
    }
}