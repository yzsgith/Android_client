package com.sea.auspicious_sign

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

import org.junit.Assert.assertEquals
import org.junit.Test

class AbFunctionTest {

    // 定义被测试的函数（可以放在被测试类中，这里直接写）
    private fun <R, B> ab(r: R, b: B, block: (R, B) -> UInt): UInt = block(r, b)

    @Test
    fun testAbFunction() {
        val x = 5
        val y = 3
        val result = ab(x, y) { a, b ->
            (a * a + b * b).toUInt()
        }
        // 期望结果：5平方 + 3平方 = 25 + 9 = 34
        assertEquals(34u, result)  // 注意 UInt 类型字面量用 34u
    }
}