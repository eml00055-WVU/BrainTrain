package com.braintrain.app.model

/** Points awarded for each correctly answered question. */
const val POINTS_PER_CORRECT = 100

/** Points deducted for each incorrectly answered question. */
const val PENALTY_PER_WRONG = 40

/** Bonus points factor for seconds still left on the clock when a player clears
 *  every level before time runs out. */
const val SPEED_BONUS_PER_SECOND = 5

/** Speed bonus for completing a round with [timeLeft] seconds remaining. */
fun speedBonus(timeLeft: Int): Int = timeLeft * SPEED_BONUS_PER_SECOND
