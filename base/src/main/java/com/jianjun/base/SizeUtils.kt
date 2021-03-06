package com.jianjun.base

import android.content.res.Resources
import android.util.TypedValue

val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, Resources.getSystem().displayMetrics
    )

val Float.px: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this, Resources.getSystem().displayMetrics
    )

val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, Resources.getSystem().displayMetrics
    )

