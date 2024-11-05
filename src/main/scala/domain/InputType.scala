package domain

import enumeratum._

/** same as Language and ContentType */
sealed trait InputType extends EnumEntry

case object InputType extends Enum[InputType] with CirceEnum[InputType]:
  case object tweet extends InputType

  case object link extends InputType

  val values = findValues
