module Module exposing (Type(BadCons))

import BadModule
import ModuleToImport

type alias Alias = BadType

type Type = Cons ModuleToImport<caret>.
