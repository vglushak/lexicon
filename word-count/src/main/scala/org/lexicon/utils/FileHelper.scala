package org.lexicon.utils

import scala.io.Codec

object FileHelper {

  implicit val codec = Codec("UTF-8")

  def getResource(s: String) = {
    val is = getClass.getClassLoader.getResourceAsStream(s)
    scala.io.Source.fromInputStream(is).getLines().mkString("\n")
  }

}
