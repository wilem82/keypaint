package com.github.wilemyvu.keypaint

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.effect.BlendMode
import javafx.scene.effect.BoxBlur
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.StrokeType
import javafx.stage.Stage

fun main(args: Array<String>) {
    Application.launch(Keypaint::class.java, *args)
}

class Keypaint : Application() {
    override fun start(primaryStage: Stage) {

        primaryStage.title = "Keypaint"
        primaryStage.isFullScreen = true

        val root = Group()
        val scene = Scene(root, 800.0, 600.0, Color.BLACK)
        primaryStage.scene = scene

        val colors = Rectangle(
                scene.width,
                scene.height,
                LinearGradient(
                        0.0,
                        1.0,
                        1.0,
                        0.0,
                        true,
                        CycleMethod.NO_CYCLE,
                        listOf(
                                Stop(0.00, Color.web("#f8bd55")),
                                Stop(0.14, Color.web("#c0fe56")),
                                Stop(0.28, Color.web("#5dfbc1")),
                                Stop(0.43, Color.web("#64c2f8")),
                                Stop(0.57, Color.web("#be4af7")),
                                Stop(0.71, Color.web("#ed5fc2")),
                                Stop(0.85, Color.web("#ef504c")),
                                Stop(1.00, Color.web("#f2660f"))
                        )
                )
        ).also {
            it.widthProperty().bind(scene.widthProperty())
            it.heightProperty().bind(scene.heightProperty())
        }

        val circles = Group().also {
            it.effect = BoxBlur(5.0, 5.0, 3)
        }

        val blendModeGroup = Group(Group(Rectangle(scene.width, scene.height, Color.BLACK), circles), colors)
        colors.blendMode = BlendMode.OVERLAY

        root.children.add(blendModeGroup)

        root.onKeyPressed = EventHandler<KeyEvent> {
            val code = it.code

            if (code == KeyCode.SPACE) {
                circles.children.clear()
                return@EventHandler
            }

            val rows = arrayOf(
                    arrayOf(KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y, KeyCode.U, KeyCode.I, KeyCode.O, KeyCode.P, KeyCode.OPEN_BRACKET, KeyCode.CLOSE_BRACKET),
                    arrayOf(KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.SEMICOLON, KeyCode.QUOTE),
                    arrayOf(KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N, KeyCode.M, KeyCode.COMMA, KeyCode.PERIOD, KeyCode.SLASH)
            )
            val typedRow = rows.withIndex().find { it.value.contains(code) }?.index ?: return@EventHandler
            val sceneRowHeight = scene.height / rows.size

            val typedCol = rows[typedRow].withIndex().find { it.value == code }?.index ?: return@EventHandler
            val sceneColWidth = scene.width / rows[typedRow].size

            val size = 100.0

            circles.children.add(Circle(
                    (sceneColWidth * typedCol) + (Math.random() * sceneColWidth),
                    (sceneRowHeight * typedRow) + (Math.random() * sceneRowHeight),
                    size,
                    Color.web("white", 0.05)
            ).also {
                it.strokeType = StrokeType.OUTSIDE
                it.stroke = Color.web("white", 0.16)
                it.strokeWidth = 4.0
            })
        }

        root.requestFocus()

        primaryStage.show()
    }

}