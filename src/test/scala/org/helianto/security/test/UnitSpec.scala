package org.helianto.test

import org.scalatest._
import org.scalatest.mockito.MockitoSugar

abstract class UnitSpec extends FlatSpec with Matchers
  with MockitoSugar
