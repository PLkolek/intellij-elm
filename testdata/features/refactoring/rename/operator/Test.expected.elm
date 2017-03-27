module Test exposing ((<->), SomeType)

type SomeType
    = A
    | B

infixl 2 <->
(<->) = 5

type alias Alias = SomeType