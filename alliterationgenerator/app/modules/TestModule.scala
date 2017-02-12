package modules

import scaldi.Module
import services._

class TestModule extends Module {
  bind [GeneratorService] to new BasicGenerator
  bind [TokenBuilder] to FakeTokenBuilder
}
