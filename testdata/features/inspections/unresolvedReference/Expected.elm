module Module exposing (Type(BadCons),
    (...), badPort)

import BadModule
import ModuleToImport

type alias Alias = BadType

type Type = Cons ModuleToImport<caret>.

properValue = improperValue

infixl 2 <>>