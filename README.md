# Statelin 
[![CircleCI](https://circleci.com/gh/anupcowkur/statelin.svg?style=svg&circle-token=1ded70d2f302eaa1ca4a304c084d8d3ef9c7171d)](https://circleci.com/gh/anupcowkur/statelin)

Statelin is a finite state machine implemented purely in Kotlin.

# Installation

MAVEN

```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```

```xml
<dependency>
  <groupId>com.anupcowkur</groupId>
  <artifactId>statelin</artifactId>
  <version>0.1.0</version>
</dependency>
```

GRADLE

```groovy
repositories {
    jcenter()
}
```

```groovy
compile 'com.anupcowkur:statelin:0.1.0'
```

# Usage
Create a machine with an initial state:

```kotlin
val stateA = State("A")
val machine = Machine(stateA)
```

Create a new state with optional enter and exit callbacks:

```kotlin
val stateB = State(name = "B",
                    onEnter = { print("Entering state B") },
                    onExit = { print("Exiting state B") })
```

Transition from A to B:

```kotlin
machine.state = stateB // "Entering state B" will be printed
```

Add a trigger with a handler:

```kotlin
val onSubmitClick = Trigger("onSubmitClick")
machine.addTriggerHandler(TriggerHandler(stateB, onSubmitClick, {
                // Do whatever. Handle business logic, set a new state etc
            }))
```

**Note:** There can be only one trigger handlers for each state and trigger pair.
i.e. `stateA` and `triggerX` can have only one handler. Adding another handler for the
same state and trigger will throw an exception. 

That's all there is to know!

# What can I use this for?
State machines are useful for modeling any kind of stateful UI or business
logic. Let's look at an example.

Let's model an autocompleting search box like google search.

- Our machine will be initialized in `Init` state.
- Once users enter a text, we will trigger `onTextEntered` and put it into the `Loading` state.
- Once we have the results from an API call, we can put the machine into a `Showing results` state. 
- If new text is entered, we will trigger `onTextEntered` again and the process repeats.

Our states will be:

```kotlin
val stateInit = State("Init")
val stateShowingResults = State(name = "Showing results",
        onEnter = {
            populateOrUpdateList()
        })
val stateLoading = State(name = "Loading",
        onEnter = {
            setUiToLoading()
            results = callAPI()
            machine.state = stateShowingResults
        })
```

Our triggers will be:

```kotlin
val triggerOnTextEntered = Trigger("onTextEntered")
```


And here's our machine and trigger handlers:

```kotlin
val machine = Machine(stateInit)

machine.addTriggerHandler(TriggerHander(stateInit, triggerOnTextEntered, {
    machine.state = stateLoading
}))
machine.addTriggerHandler(TriggerHander(stateShowingResults, triggerOnTextEntered, {
    machine.state = stateLoading
}))
```
  
State machines allow us to explicitly model the possible states and transitions of
our applications. This allows us to define what's possible in the system upfront
and prevent bugs like "When X happens and Y happens and Z happens, some random thing
happens". 


State machines are useful simply because it's easier to program for what's possible
 than to defend against all the things that should not be possible.
 
# Contributing

You can report bugs in the issues tracker. Please add a sample app
 with a minimal test case that reproduces the bug you are reporting.
 
Feature requests are also welcome, preferably with a pull request and tests :-)
