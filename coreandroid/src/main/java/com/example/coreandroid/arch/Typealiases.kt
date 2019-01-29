package com.example.coreandroid.arch

typealias Reducer<Action, State> = Action.(State) -> State

typealias Middleware<Action, State> = (Action, State, (Action) -> Unit) -> Action

typealias EventFactory<Action, State, Event> = (Action, State) -> Event