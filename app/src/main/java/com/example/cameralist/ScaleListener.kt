package com.example.cameralist

import android.content.Context
import android.view.ScaleGestureDetector
import android.widget.ImageView

class ScaleListener(var mImageView: ImageView): ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        mScaleFactor *= scaleGestureDetector.scaleFactor
        mScaleFactor = Math.max(
            0.1f,
            Math.min(mScaleFactor, 10.0f)
        )
        mImageView?.setScaleX(mScaleFactor)
        mImageView?.setScaleY(mScaleFactor)


        return true
    }
}