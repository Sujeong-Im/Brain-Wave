package com.example.matchingproto

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class HalfExpandBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val appBarLayout = dependency as AppBarLayout
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(appBarLayout.y) / maxScroll.toFloat()

        val halfHeight = child.height /*/ 2*/
        var distance:Float = maxScroll * percentage - halfHeight
        Log.d("san",distance.toString())
        Log.d("maxscroll",maxScroll.toString())

        /*if(distance<=maxScroll.toFloat()/2){
            distance = maxScroll.toFloat()/2
        }*/
        child.y = -distance

        return true
    }

}