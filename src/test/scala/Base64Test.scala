package com.meineliebe
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class Base64Test extends AnyFlatSpec with Matchers with ScalaCheckPropertyChecks:

  behavior of "Base64"

  it must "encode" in {
    Base64.encode("123") mustBe "MTIz"
    Base64.encode("1234") mustBe "MTIzNA=="
    Base64.encode("asdfasdfff") mustBe "YXNkZmFzZGZmZg=="
    Base64.encode("asdfasdfffdlikghndkl;fmndslfo;iw131241") mustBe "YXNkZmFzZGZmZmRsaWtnaG5ka2w7Zm1uZHNsZm87aXcxMzEyNDE="
  }

  it must "solve" in {
    Base64.encode("È") mustBe "w4g="
    Base64.encode("랹^") mustBe "6565Xg=="
  }

  it must "decode" in {
    Base64.decode("MTIz") mustBe "123"
    Base64.decode("MTIzNA==") mustBe "1234"
    Base64.decode("YXNkZmFzZGZmZg==") mustBe "asdfasdfff"
    Base64.decode("YXNkZmFzZGZmZmRsaWtnaG5ka2w7Zm1uZHNsZm87aXcxMzEyNDE=") mustBe "asdfasdfffdlikghndkl;fmndslfo;iw131241"
  }

  it must "be reversible" in {
    forAll { (s: String) ⇒
      Base64.decode(Base64.encode(s)) mustBe s
    }
  }


