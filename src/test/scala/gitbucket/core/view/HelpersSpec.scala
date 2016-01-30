package gitbucket.core.view

import org.specs2.mutable._

class HelpersSpec extends Specification {

  import helpers._

  "detect and render links" should {

    "pass identical string when no link is present" in {
      val before = "Description"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual before
    }

    "convert a single link" in {
      val before = "http://example.com"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """<a href="http://example.com">http://example.com</a>"""
    }

    "convert a single link within trailing text" in {
      val before = "Example Project. http://example.com"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """Example Project. <a href="http://example.com">http://example.com</a>"""
    }

    "convert a mulitple links within text" in {
      val before = "Example Project. http://example.com. (See also https://github.com/)"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """Example Project. <a href="http://example.com">http://example.com</a>. (See also <a href="https://github.com/">https://github.com/</a>)"""
    }

    "properly escape html metacharacters" in {
      val before = "<>&"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """&lt;&gt;&amp;"""
    }

    "escape html metacharacters adjacent to a link" in {
      val before = "<http://example.com>"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """&lt;<a href="http://example.com">http://example.com</a>&gt;"""
    }

    "stop link recognition at a metacharacter" in {
      val before = "http://exa<mple.com"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """<a href="http://exa">http://exa</a>&lt;mple.com"""
    }

    "make sure there are no double quotes in the href attribute" in {
      val before = "http://exa\"mple.com"
      val after = detectAndRenderLinks(before).toString()
      after mustEqual """<a href="http://exa&quot;mple.com">http://exa"mple.com</a>"""
    }
  }
}
