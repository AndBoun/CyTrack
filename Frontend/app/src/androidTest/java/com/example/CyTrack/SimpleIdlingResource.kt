package com.example.CyTrack

import androidx.test.espresso.IdlingResource

class SimpleIdlingResource : IdlingResource {

    @Volatile
    private var isIdleNow = true
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return isIdleNow
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        resourceCallback = callback
    }

    fun setIdleState(isIdleNow: Boolean) {
        this.isIdleNow = isIdleNow
        if (isIdleNow && resourceCallback != null) {
            resourceCallback!!.onTransitionToIdle()
        }
    }
}