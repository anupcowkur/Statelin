package com.anupcowkur.statelin

class State(private val name: String, internal val onEnter: (() -> Unit)? = null, internal val onExit: (() -> Unit)? = null) {

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = other is State && other.name == name
}