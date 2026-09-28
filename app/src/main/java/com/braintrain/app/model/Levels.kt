package com.braintrain.app.model

import androidx.compose.ui.graphics.Color

// ─── Shape pattern levels ──────────────

val SHAPE_LEVELS: List<ShapeLevel> = listOf(
    ShapeLevel(
        pattern = listOf(ShapeName.SQUARE, ShapeName.CIRCLE, ShapeName.SQUARE, ShapeName.TRIANGLE),
        answer = ShapeName.SQUARE,
        options = listOf(ShapeName.SQUARE, ShapeName.CIRCLE, ShapeName.TRIANGLE, ShapeName.PENTAGON),
        hint = "The pattern repeats every 4 shapes",
    ),
    ShapeLevel(
        pattern = listOf(ShapeName.CIRCLE, ShapeName.TRIANGLE, ShapeName.CIRCLE, ShapeName.TRIANGLE),
        answer = ShapeName.CIRCLE,
        options = listOf(ShapeName.PENTAGON, ShapeName.CIRCLE, ShapeName.SQUARE, ShapeName.TRIANGLE),
        hint = "Two shapes keep swapping places",
    ),
    ShapeLevel(
        pattern = listOf(ShapeName.TRIANGLE, ShapeName.TRIANGLE, ShapeName.CIRCLE, ShapeName.TRIANGLE),
        answer = ShapeName.TRIANGLE,
        options = listOf(ShapeName.SQUARE, ShapeName.PENTAGON, ShapeName.TRIANGLE, ShapeName.CIRCLE),
        hint = "One shape appears most often",
    ),
    ShapeLevel(
        pattern = listOf(ShapeName.PENTAGON, ShapeName.CIRCLE, ShapeName.PENTAGON, ShapeName.CIRCLE),
        answer = ShapeName.PENTAGON,
        options = listOf(ShapeName.TRIANGLE, ShapeName.PENTAGON, ShapeName.CIRCLE, ShapeName.SQUARE),
        hint = "Every other shape is the same",
    ),
    ShapeLevel(
        pattern = listOf(ShapeName.STAR, ShapeName.SQUARE, ShapeName.CIRCLE, ShapeName.STAR),
        answer = ShapeName.SQUARE,
        options = listOf(ShapeName.CIRCLE, ShapeName.TRIANGLE, ShapeName.PENTAGON, ShapeName.SQUARE),
        hint = "The pattern repeats from the start",
    ),
)

// ─── Equation levels ──────────

val EQUATION_LEVELS: List<EquationLevel> = listOf(
    EquationLevel("6 + 9", 15, listOf(12, 15, 17, 13)),
    EquationLevel("18 − 5", 13, listOf(13, 11, 23, 14)),
    EquationLevel("7 × 3", 21, listOf(18, 21, 24, 19)),
    EquationLevel("28 ÷ 4", 7, listOf(5, 8, 7, 6)),
    EquationLevel("5 + 13 − 4", 14, listOf(14, 12, 18, 22)),
    EquationLevel("3 × 6 − 7", 11, listOf(11, 9, 13, 15)),
    EquationLevel("(8 + 4) × 3", 36, listOf(32, 36, 40, 24)),
    EquationLevel("(15 − 3) ÷ 4", 3, listOf(2, 3, 4, 6)),
    EquationLevel("4 × 4 + 9", 25, listOf(25, 29, 21, 33)),
    EquationLevel("36 ÷ 6 − 2", 4, listOf(3, 4, 5, 8)),
)

// ─── Synonym levels ────────────

val SYNONYM_LEVELS: List<SynonymLevel> = listOf(
    SynonymLevel("HAPPY", "adjective", "Joyful", listOf("Joyful", "Gloomy", "Restless", "Tired")),
    SynonymLevel("FAST", "adjective", "Swift", listOf("Heavy", "Swift", "Slow", "Rough")),
    SynonymLevel("BRAVE", "adjective", "Courageous", listOf("Timid", "Clumsy", "Curious", "Courageous")),
    SynonymLevel("SMART", "adjective", "Clever", listOf("Clever", "Foolish", "Careless", "Loud")),
    SynonymLevel("HUGE", "adjective", "Enormous", listOf("Tiny", "Enormous", "Thin", "Quiet")),
    SynonymLevel("SAD", "adjective", "Melancholy", listOf("Cheerful", "Melancholy", "Lively", "Proud")),
    SynonymLevel("FRIEND", "noun", "Companion", listOf("Rival", "Stranger", "Companion", "Enemy")),
    SynonymLevel("TIRED", "adjective", "Weary", listOf("Weary", "Alert", "Fresh", "Eager")),
    SynonymLevel("STRANGE", "adjective", "Peculiar", listOf("Normal", "Peculiar", "Familiar", "Expected")),
    SynonymLevel("NOISY", "adjective", "Rowdy", listOf("Silent", "Rowdy", "Hushed", "Calm"))
)

// ─── Shared option accent colors  ───

val OPTION_BG: List<Color> = listOf(
    Color(0xFFFFE8E8), Color(0xFFFFF8D9), Color(0xFFE8F8EA), Color(0xFFE2EEFF),
)
val OPTION_ACCENT: List<Color> = listOf(
    Color(0xFFFF6B6B), Color(0xFFF5A623), Color(0xFF6BCB77), Color(0xFF4D96FF),
)

// ─── Title screen floating decorations ──────────────

val FLOATERS: List<Floater> = listOf(
    Floater(ShapeName.CIRCLE, 0.12f, 0.18f, 38, 0, 3800),
    Floater(ShapeName.SQUARE, 0.72f, 0.10f, 30, 600, 4200),
    Floater(ShapeName.TRIANGLE, 0.55f, 0.48f, 34, 1100, 3500),
    Floater(ShapeName.PENTAGON, 0.20f, 0.58f, 26, 300, 4800),
    Floater(ShapeName.STAR, 0.80f, 0.52f, 32, 900, 3200),
    Floater(ShapeName.SQUARE, 0.42f, 0.16f, 22, 1500, 5000),
    Floater(ShapeName.CIRCLE, 0.68f, 0.72f, 18, 400, 4000),
)

const val TIMER_START_SECONDS = 60
