package com.meineliebe
import Base64.decode

import scala.annotation.tailrec
import scala.collection.mutable

object Base64:

  private val codes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toVector

  private val decodes: Map[Char, Int] =
    codes.zipWithIndex.toMap

  private val fin = '='

  @tailrec
  def encode(x: Array[Int], idx: Int = 0, buff: List[Char] = List.empty): List[Char] =
    def getOrZero(iIdx: Int): Int =
      if iIdx < x.length then
        x(iIdx)
      else 0

    def getOrFin(iIdx: Int, f: ⇒ Int) =
      if iIdx < x.length then
        codes(f)
      else fin

    if idx < x.length then
      val b0 = getOrZero(idx)
      val b1 = getOrZero(idx + 1)
      val b2 = getOrZero(idx + 2)

      val eb0 = getOrFin(idx, b0 >> 2)
      val eb01 = getOrFin(idx, ((b0 & 3) << 4) | (b1 >> 4))
      val eb12 = getOrFin(idx + 1, ((b1 & 15) << 2) | (b2 >> 6))
      val eb2 = getOrFin(idx + 2, b2 & 63)

      val next = eb2 :: eb12 :: eb01 :: eb0 :: buff
      encode(x, idx + 3, next)
    else
      buff

  def encode(x: String): String = encode(x.getBytes.map(_ & 0xff)).reverse.mkString

  @tailrec
  def decode(x: Array[Char], idx: Int = 0, buff: List[Byte] = List.empty): List[Byte] =

    def getIfBoth(a: Option[Int], b: Option[Int], f: (Int, Int) ⇒ Int) =
      for
        x ← a
        y ← b
      yield f(x, y)

    if idx < x.length then
      val b0 = decodes.get(x(idx))
      val b1 = decodes.get(x(idx + 1))
      val b2 = decodes.get(x(idx + 2))
      val b3 = decodes.get(x(idx + 3))

      val db01 = getIfBoth(b0, b1, (a, b) ⇒ ((a & 63) << 2) | ((b & 48) >> 4))
      val db12 = getIfBoth(b1, b2, (a, b) ⇒ ((a & 15) << 4) | ((b & 60) >> 2))
      val db23 = getIfBoth(b2, b3, (a, b) ⇒ ((a & 3) << 6) | b & 63)

      val next = List(db23, db12, db01).flatten.map(_.toByte) ::: buff
      decode(x, idx + 4, next)
    else
      buff

  def decode(base64: String): String =
    String(decode(base64.chars().toArray.map(_.toChar)).reverse.toArray)
