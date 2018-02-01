package com.anupcowkur.statelin

data class TriggerHandler(val state: State,
                          val trigger: Trigger,
                          val handler: () -> Unit)