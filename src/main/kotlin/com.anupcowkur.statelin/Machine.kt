package com.anupcowkur.statelin

class Machine(state: State) {

    var state: State = state
        private set

    internal val transitions = mutableListOf<Transition>()

    internal val children = mutableListOf<Machine>()

    fun addTransition(transition: Transition) {
        if (isDuplicateTransition(transition)) {
            throw IllegalArgumentException("Transition ${transition.oldState} -> ${transition.action} -> ${transition.newState} is already added")
        }

        transitions.add(transition)
    }

    private fun isDuplicateTransition(transition: Transition): Boolean {
        if (isDuplicateTransitionInThisMachine(transition)) {
            return true
        }

        if (isDuplicateTransitionInChildMachine(transition)) {
            return true
        }

        return false
    }

    private fun isDuplicateTransitionInThisMachine(transition: Transition): Boolean {
        transitions.forEach({
            if (it.oldState == transition.oldState && it.action == transition.action && it.newState == transition.newState) {
                return true
            }
        })

        return false
    }

    private fun isDuplicateTransitionInChildMachine(transition: Transition): Boolean {
        children.forEach({
            return it.isDuplicateTransition(transition)
        })

        return false
    }

    fun addChildMachine(childMachine: Machine) {
        children.add(childMachine)
    }

    fun trigger(action: Action): Boolean {
        if (handleTriggerInThisMachine(action)) {
            return true
        }

        if (handleTriggerInChildMachine(action)) {
            return true
        }

        return false
    }

    private fun handleTriggerInThisMachine(action: Action): Boolean {
        transitions.forEach({
            if (it.oldState == state && it.action == action) {
                it.onTransition(state, it.action, it.newState)
                state = it.newState
                return true
            }
        })

        return false
    }

    private fun handleTriggerInChildMachine(action: Action): Boolean {
        children.forEach({
            if (it.trigger(action)) {
                return true
            }
        })

        return false
    }

}