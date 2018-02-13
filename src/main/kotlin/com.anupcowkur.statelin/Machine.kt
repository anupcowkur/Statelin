package com.anupcowkur.statelin

class Machine(state: State) {
    var state: State = state
        set(value) {
            if (state == value) {
                return
            }

            state.onExit?.invoke()
            field = value
            state.onEnter?.invoke()
        }

    internal val eventHandlers = mutableSetOf<TriggerHandler>()

    init {
        state.onEnter?.invoke()
    }

    fun addTriggerHandler(triggerHandler: TriggerHandler) {
        if (!eventHandlers.add(triggerHandler)) {
            throw IllegalArgumentException("TriggerHandler ${triggerHandler.state} -> ${triggerHandler.trigger} is already added")
        }
    }

    fun trigger(trigger: Trigger) {
        eventHandlers.firstOrNull { it.state == state && it.trigger == trigger }?.handler?.invoke()
    }

}