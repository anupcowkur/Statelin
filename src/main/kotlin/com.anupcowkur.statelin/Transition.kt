package com.anupcowkur.statelin

data class Transition(val oldState: State,
                      val action: Action,
                      val newState: State,
                      val onTransition: (oldState: State, action: Action, newState: State) -> Unit)