package org.lexicon.utils

import scala.collection.concurrent.TrieMap
import org.slf4j.LoggerFactory


case class WordInfo(word: String, var count: Int) {
  override def toString = s"$word=$count"
}

class TextAnalyzer {

  val logger = LoggerFactory.getLogger(getClass)

  def splitText(text: String) = {
    text.replaceAll("[!?#,\\.\\(\\)\\[\\]\\{\\}\"-:;]", "").split("\\s")
  }

  def buildMap(words: List[String]) = {
    val wordsMap = new TrieMap[String, WordInfo]()
    words.foreach({
      w =>
        val rec = wordsMap.getOrElse(w.toLowerCase, WordInfo(w, 0))
        rec.count = rec.count + 1
        wordsMap.put(w.toLowerCase, rec)
    })
    Map(wordsMap.toList: _*)
  }

  def mergeMaps(wordMaps: Map[String, WordInfo]*) = {
    val res = wordMaps.toList.foldLeft(Map[String, WordInfo]())({
      (originalMap, newMap) =>
        val res = originalMap.map({
          r => {
            val pr = newMap.get(r._1)
            pr.foreach(wi => r._2.count = r._2.count + wi.count)
            r
          }
        }) ++ newMap.filterKeys(originalMap.get(_).isEmpty)
        logger.trace("Merging results: " + res)
        res
    })
    logger.debug(s"Map after merging contains ${res.size} records")
    res
  }

  def toDescList(wordMap: Map[String, WordInfo]) = {
    wordMap.map(_._2).toList.sortBy(- _.count)
  }
}


object TextAnalyzerObj extends TextAnalyzer {}
