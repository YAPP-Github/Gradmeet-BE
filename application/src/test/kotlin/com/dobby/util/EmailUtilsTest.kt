package com.dobby.util

import com.dobby.exception.EmailFormatInvalidException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch

class EmailUtilsTest : BehaviorSpec({

    given("학교 메일 판별 규칙") {
        `when`("표준 학교 도메인 규칙에 해당하는 이메일이면") {
            then("국내 ac.kr 메일은 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("dobby@yonsei.ac.kr") shouldBe true
            }
            then("해외 edu 메일은 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("ssim6@gsu.edu") shouldBe true
            }
            then("영국 ac.uk 메일은 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("dobby@ox.ac.uk") shouldBe true
            }
            then("호주 edu.au 메일은 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("dobby-socks@griffithuni.edu.au") shouldBe true
            }
        }

        `when`("예외 목록에 등록된 학교 도메인이면") {
            then("이화여대 ewhain.net 메일은 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("christer10@ewhain.net") shouldBe true
            }
            then("상명대 sangmyung.kr 메일은 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("dobby@sangmyung.kr") shouldBe true
            }
            then("상명대 하위 도메인 메일도 학교 메일로 판별한다") {
                EmailUtils.isUnivMail("dobby@mail.sangmyung.kr") shouldBe true
            }
        }

        `when`("학교 도메인이 아닌 이메일이면") {
            then("일반 포털 메일은 학교 메일이 아니다") {
                EmailUtils.isUnivMail("christer10@naver.com") shouldBe false
                EmailUtils.isUnivMail("dobby@gmail.com") shouldBe false
            }
            then("예외 목록에 없는 co.kr 메일은 학교 메일이 아니다") {
                EmailUtils.isUnivMail("venew@gccompany.co.kr") shouldBe false
            }
            then("예외 목록에 없는 kr 메일은 학교 메일이 아니다") {
                EmailUtils.isUnivMail("dobby@gradmeet.kr") shouldBe false
            }
        }
    }

    given("이메일 도메인 추출") {
        `when`("@가 포함된 이메일이면") {
            then("@ 뒤의 도메인을 반환한다") {
                EmailUtils.extractDomain("dobby@sangmyung.kr") shouldBe "sangmyung.kr"
            }
        }

        `when`("@가 없는 문자열이면") {
            then("EmailFormatInvalidException 예외가 발생해야 한다") {
                shouldThrow<EmailFormatInvalidException> {
                    EmailUtils.extractDomain("sangmyung.kr")
                }
            }
        }
    }

    given("인증 코드 생성") {
        `when`("코드를 생성하면") {
            then("6자리 숫자 형식이어야 한다") {
                repeat(100) {
                    EmailUtils.generateCode() shouldMatch Regex("^\\d{6}$")
                }
            }
        }
    }
})
