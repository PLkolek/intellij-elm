module Module exposing(Type(Constructor, C2), (<->))

type Type = Constructor Int Int Int | C2

infix 3 <->
(<-<caret>>) = 2