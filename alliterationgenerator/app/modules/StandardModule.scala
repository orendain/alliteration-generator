package modules

import scaldi.Module
import services._

class StandardModule extends Module {
  bind [GeneratorService] to new PrecachingGenerator
  bind [TokenBuilder] to new BasicTokenBuilder
}
