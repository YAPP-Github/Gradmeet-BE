package com.dobby.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class UtmLinkUtilsTest : BehaviorSpec ({

    given("메일 트래킹 정책(utm_source=email&utm_medium=daily)") {
        `when`("원본 쿼리스트링이 없는 URL이면") {
            val url = "https://gradmeet.co.kr/post/123"
            then("UTM이 ?로 시작하여 붙는다") {
                UtmLinkUtils.add(url) shouldBe
                    "https://gradmeet.co.kr/post/123?utm_source=email&utm_medium=daily"
            }
        }


        `when`("이미 쿼리스트링이 있는 URL이면") {
            val url = "https://gradmeet.co.kr/post/123?param=test"
            then("UTM이 &로 붙는다") {
                UtmLinkUtils.add(url) shouldBe
                    "https://gradmeet.co.kr/post/123?param=test&utm_source=email&utm_medium=daily"
            }
        }

        `when`("이미 동일한 UTM이 붙어 있는 URL이면") {
            val url = "https://gradmeet.co.kr/321?utm_source=email&utm_medium=daily"
            then("중복 추가하지 않는다") {
                UtmLinkUtils.add(url) shouldBe
                    "https://gradmeet.co.kr/321?utm_source=email&utm_medium=daily"
            }
        }
    }

})
