package domain

import enumeratum._

/** decided to present this type in the form of enum for type safety 
 * included all languages that were found during the use of the API
 * if the api starts returning new languages, modifications will be necessary
 */
sealed trait Language extends EnumEntry

case object Language extends Enum[Language] with CirceEnum[Language]:
  case object en extends Language

  case object de extends Language

  case object es extends Language

  case object it extends Language

  case object pt extends Language

  case object ar extends Language

  case object no extends Language

  case object `ên` extends Language

  val values = findValues
