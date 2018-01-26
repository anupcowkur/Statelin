package com.anupcowkur.statelin

data class State1(val name: String) : State()
data class State2(val price: Double) : State()
data class State3(val firstName: String, val lastName: String) : State()
data class State4(val temperature: Float) : State()


data class Action1(val name: String) : Action()
data class Action2(val price: Double) : Action()
data class Action3(val firstName: String, val lastName: String) : Action()
data class Action4(val temperature: Float) : Action()