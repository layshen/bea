package com.epiboly.bea2.util

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author mao
 * @time 2021/10/29
 * @describe  1.dp 1.sp
 */

inline val Int.dp: Float get() = toFloat().dp
inline val Int.sp: Float get() = toFloat().sp
inline val Float.dp: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
inline val Float.sp: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)