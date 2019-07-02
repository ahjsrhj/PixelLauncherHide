package cn.imrhj.pixellauncherhide.ui

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator


/**
 * Created by rhj on 23/02/2018.
 */
class FloatingBehavior(context: Context?, attrs: AttributeSet?) : FloatingActionButton.Behavior(context, attrs) {
    private var hide = false
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && !hide) {
            hide = true
            hide(child)
        } else if (dyConsumed < 0 && hide) {
            hide = false
            show(child)
        }
    }

    private fun hide(child: FloatingActionButton) {
        val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
        child.animate()
                .translationY((child.height + layoutParams.bottomMargin).toFloat())
                .setInterpolator(LinearInterpolator())
                .setDuration(250)
                .start()
    }

    private fun show(child: FloatingActionButton) {
        child.animate()
                .translationY(0f)
                .setInterpolator(BounceInterpolator())
                .setDuration(250)
                .start()
    }
}
