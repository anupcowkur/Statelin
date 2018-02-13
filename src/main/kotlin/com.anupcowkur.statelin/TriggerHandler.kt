package com.anupcowkur.statelin

data class TriggerHandler(val state: State,
                          val trigger: Trigger,
                          val handler: () -> Unit) {

    override fun hashCode() = state.hashCode() * 31 + trigger.hashCode()

    override fun equals(other: Any?) = other is TriggerHandler && other.state == state && other.trigger == trigger
}