module Test exposing ((*.*), SomeType)

type SomeType
    = A
    | B

infixl 2 *.<caret>*
(*.*) = 5

type alias Alias = SomeType