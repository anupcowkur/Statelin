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

    internal val eventHandlers = mutableListOf<TriggerHandler>()

    init {
        state.onEnter?.invoke()
    }

    fun addTriggerHandler(triggerHandler: TriggerHandler) {
        if (isDuplicateTransition(triggerHandler)) {
            throw IllegalArgumentException("TriggerHandler ${triggerHandler.state} -> ${triggerHandler.trigger} is already added")
        }

        eventHandlers.add(triggerHandler)
    }

    private fun isDuplicateTransition(triggerHandler: TriggerHandler): Boolean {
        eventHandlers.forEach({
            if (it.state == triggerHandler.state && it.trigger == triggerHandler.trigger) {
                return true
            }
        })

        return false
    }

    fun trigger(trigger: Trigger) {
        eventHandlers.forEach({
            if (it.state == state && it.trigger == trigger) {
                it.handler.invoke()
                return
            }
        })
    }

}