module Test exposing ((*.*), SomeType)

type SomeType
    = A
    | B

infixl 2 *.<caret>*

type alias Alias = SomeType