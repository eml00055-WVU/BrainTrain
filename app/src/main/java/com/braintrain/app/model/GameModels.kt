package com.braintrain.app.model

/** Which full screen is currently showing. */
enum class Screen { TITLE, MODE_SELECT, SHAPES, EQUATIONS, SYNONYMS }

/** What state is the current answer in */
enum class AnswerState { PLAYING, CORRECT, WRONG }

/** Struct to hold shape pattern levels */
data class ShapeLevel(
    val pattern: List<ShapeName>,
    val answer: ShapeName,
    val options: List<ShapeName>,
    val hint: String,
)

/** Struct to hold equation levels */
data class EquationLevel(
    val display: String,
    val answer: Int,
    val options: List<Int>,
)

/** Struct to hold synonym levels */
data class SynonymLevel(
    val word: String,
    val pos: String,
    val answer: String,
    val options: List<String>,
)

/** A floating background shape decoration on the title screen. */
data class Floater(
    val shape: ShapeName,
    val xPercent: Float,
    val yPercent: Float,
    val size: Int,
    val delayMs: Int,
    val durationMs: Int,
)
