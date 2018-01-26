package com.anupcowkur.statelin

class Machine(state: State) {

    var state: State = state
        private set

    internal val transitions = mutableListOf<Transition>()

    fun addTransition(transition: Transition) {
        transitions.forEach({
            if (it.oldState == transition.oldState && it.action == transition.action && it.newState == transition.newState) {
                throw IllegalArgumentException("Transition ${it.oldState} -> ${it.action} -> ${it.newState} is already added")
            }
        })

        transitions.add(transition)
    }

    fun trigger(action: Action) {
        transitions.forEach({
            if (it.oldState == state && it.action == action) {
                it.onTransition(state, it.action, it.newState)
                state = it.newState
                return@forEach
            }
        })
    }

}