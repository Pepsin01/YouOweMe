package com.youoweme.model

@JvmInline
/*
Precision is set to two decimal places
 */
value class FixedPointDouble(val value: Int) : Comparable<FixedPointDouble> {
    private val precision: Double get() = 100.0
    operator fun plus(other: FixedPointDouble) = FixedPointDouble(value + other.value)
    operator fun minus(other: FixedPointDouble) = FixedPointDouble(value - other.value)
    operator fun times(other: FixedPointDouble) = FixedPointDouble(value * other.value)
    operator fun div(other: FixedPointDouble) = FixedPointDouble(value / other.value)
    override fun compareTo(other: FixedPointDouble) = value.compareTo(other.value)

    override fun toString(): String {
        return value.toDouble().toString()
    }

    /*
    Underlying floating point number. Arithmetic is done in fixed floating point.
     */
    fun toDouble(): Double = value / precision
}

fun Int.toFixed() = FixedPointDouble(this)