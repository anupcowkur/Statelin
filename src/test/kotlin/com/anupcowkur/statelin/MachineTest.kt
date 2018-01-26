package com.anupcowkur.statelin

import io.kotlintest.matchers.fail
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec

class MachineTest : StringSpec() {

    init {

        "Should add transitions" {
            val state1 = State1("John")
            val state2 = State2(36.0)
            val state3 = State3("John", "Smith")
            val state4 = State4(24f)

            val action1 = Action1("John")
            val action2 = Action2(36.0)
            val action3 = Action3("John", "Smith")
            val action4 = Action4(24f)

            val machine = Machine(state1)

            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))
            machine.addTransition(Transition(state2, action3, state3, { _, _, _ -> }))
            machine.addTransition(Transition(state3, action4, state4, { _, _, _ -> }))
            machine.addTransition(Transition(state4, action1, state1, { _, _, _ -> }))
            machine.transitions.size shouldBe 4
        }

        "Should add children" {
            val state1 = State1("John")
            val state2 = State2(36.0)
            val state3 = State3("John", "Smith")

            val machine = Machine(state1)
            val childMachine1 = Machine(state2)
            val childMachine2 = Machine(state3)

            machine.addChildMachine(childMachine1)
            machine.addChildMachine(childMachine2)
            machine.children.size shouldBe 2
        }


        "Should throw exception on duplicate transition in this machine" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            // valid transition added to current machine
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val exception = shouldThrow<IllegalArgumentException> {
                // same transition added again to current machine
                machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))
            }

            exception.message shouldBe "Transition $state1 -> $action2 -> $state2 is already added"
        }

        "Should throw exception on adding duplicate transition in child machine" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            // valid transition added to current machine
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val childMachine = Machine(state1)

            // Child added to current machine. Child has no duplicate transitions yet so
            // shouldn't throw exception here.
            machine.addChildMachine(childMachine)

            val exception = shouldThrow<IllegalArgumentException> {
                // Duplicate transition added to child. Should throw exception here.
                childMachine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))
            }

            exception.message shouldBe "Transition $state1 -> $action2 -> $state2 is already added"
        }

        "Should check duplicate transitions while adding child machine" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            // valid transition added to current machine
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val childMachine = Machine(state1)
            // duplicate transition added to child
            childMachine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val exception = shouldThrow<IllegalArgumentException> {
                // Should throw exception when child is added to current machine
                machine.addChildMachine(childMachine)
            }

            exception.message shouldBe "Transition $state1 -> $action2 -> $state2 is already added"
        }


        "Should invoke transition callback on trigger" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            var invocationCounter = 0
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> invocationCounter++ }))

            machine.trigger(action2)

            invocationCounter shouldBe 1
        }

        "Should return true if trigger is handled in current machine" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val result = machine.trigger(action2)

            result shouldBe true
        }

        "Should return true if trigger is handled in child machine" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)
            val childMachine = Machine(state1)
            machine.addChildMachine(childMachine)

            childMachine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val result = machine.trigger(action2)

            result shouldBe true
        }

        "Should return false if trigger is not handled in current machine" {
            val state1 = State1("John")

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            val result = machine.trigger(action2)

            result shouldBe false
        }

        "Should return false if trigger is not handled by current machine or it's children" {
            val state1 = State1("John")

            val action2 = Action2(36.0)

            val machine = Machine(state1)
            val childMachine = Machine(state1)
            machine.addChildMachine(childMachine)

            val result = machine.trigger(action2)

            result shouldBe false
        }

        "Should pass trigger to child if not handled" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            var childMachineInvocationCounter = 0
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> childMachineInvocationCounter++ }))

            machine.trigger(action2)

            childMachineInvocationCounter shouldBe 1
        }

        "Should set new state on trigger" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(state1)

            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            machine.trigger(action2)

            machine.state shouldBe state2
        }

    }
}