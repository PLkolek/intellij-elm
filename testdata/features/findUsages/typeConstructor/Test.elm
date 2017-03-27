module Test exposing(SomeType(Constructor))

type SomeType = Co<caret>nstructor

someValue = Constructor

otherValue =
    case someValue of
        Constructor -> 123