module Module exposing(Type(Constructor, C2), some<caret>Port, (<->))

type Type = Constructor Int Int Int | C2

port somePort : Cmd Int

infix 3 <->