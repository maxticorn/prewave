package domain

import enumeratum._

/** same as Language and InputType */
sealed trait ContentType extends EnumEntry

object ContentType extends Enum[ContentType] with CirceEnum[ContentType]:
  case object text extends ContentType

  case object short extends ContentType

  val values = findValues
