package com.radovan.spring.utils

import java.nio.charset.Charset
import java.util.Random

import org.springframework.stereotype.Component

@Component
class RandomStringUtil {
  def getAlphaNumericString(n: Int): String = {
    val array = new Array[Byte](256)
    new Random().nextBytes(array)
    val randomString = new String(array, Charset.forName("UTF-8"))
    val r = new StringBuffer
    var k = 0
    var x = n
    while ( {
      k < randomString.length
    }) {
      val ch = randomString.charAt(k)
      if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (x > 0)) {
        r.append(ch)
        x -= 1
      }

      {
        k += 1; k - 1
      }
    }
    r.toString
  }
}

