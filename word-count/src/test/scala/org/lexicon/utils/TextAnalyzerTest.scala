package org.lexicon.utils

import org.scalatest.FunSuite
import org.junit.Assert._
import FileHelper._

class TextAnalyzerTest extends FunSuite {

  import TextAnalyzerObj._

  test("Common test") {
    val hello = "Hello"
    val world = "World"
    val res = splitText(s"$hello, $world! How do you do?")
    assertEquals("Expect 6 word to be created", 6, res.length)
    assertEquals(s"First word should be $hello", hello, res(0))
    assertEquals(s"Second word should be $world", world, res(1))

    val map = buildMap(res.toList)
    assertEquals("Map to contain 5 records", 5, map.size)
    assertEquals("Hello 1", 1, map.get(hello.toLowerCase).get.count)
    assertEquals("Do 2", 2, map.get("do").get.count)
    val map2 = buildMap(splitText("Do you know, who is who?").toList)
    val map3 = buildMap(splitText("How are you?").toList)
    val resMap = mergeMaps(map, map2, map3)
    println(toDescList(resMap))
    assertEquals("Do 3", 3, resMap.get("do").get.count)
  }

  test("Based on resource") {
    val str = getResource("tolst_war_peace.txt")
    val res = toDescList(buildMap(splitText(str).toList))
    println(res)
  }

}
