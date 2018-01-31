package com.anupcowkur.statelin

class State(private val name: String, internal val onEnter: (() -> Unit)? = null, internal val onExit: (() -> Unit)? = null) {
}