package andvalidate

import play.api.data._
import play.api.data.Forms._

class AndValidateSpec extends org.specs2.mutable.Specification with org.specs2.matcher.EitherMatchers {

  case class Person(first: String, last: String)

  val formMapping = mapping("first" -> nonEmptyText,
    "last" -> nonEmptyText)(Person.apply _)(Person.unapply _).verifying(p => p.first != p.last)

  val form = Form(formMapping)

  private def shouldHaveGlobalError(validatedForm: Form[Person]) = {
    validatedForm.errors.size should_== 1
    validatedForm.globalErrors.size should_== 1
    validatedForm.hasGlobalErrors should_== true
  }

  private def shouldHaveLocalError(validatedForm: Form[Person]) = {
    validatedForm.errors.size should_== 1
    validatedForm.globalErrors.size should_== 0
    validatedForm.hasGlobalErrors should_== false
  }

  private def shouldHaveError(either: Either[Seq[FormError],Person]) = {
    either.isLeft should_== true
    either.left.get.size should_== 1
  }

  private def shouldHaveError(tuple: (Map[String,String], Seq[FormError])) = {
    tuple._2.size should_== 1
  }

  "Form" should {
    "use local constraints on bind" in {
      shouldHaveLocalError(form.bind(Map("first" -> "", "last" -> "Jackson")))
    }

    "use local constraints on fillAndValidate" in {
      shouldHaveLocalError(form.fillAndValidate(Person("", "Jackson")))
    }

    "use global constraints on bind" in {
      shouldHaveGlobalError(form.bind(Map("first" -> "Jackson", "last" -> "Jackson")))
    }

    "use global constraints on fillAndValidate" in {
      shouldHaveGlobalError(form.fillAndValidate(Person("Jackson", "Jackson")))
    }
  }

  "Mapping" should {
    "use local constraints on bind" in {
      shouldHaveError(formMapping.bind(Map("first" -> "", "last" -> "Jackson")))
    }

    "use local constraints on unbindAndValidate" in {
      shouldHaveError(formMapping.unbindAndValidate(Person("", "Jackson")))
    }

    "use global constraints on bind" in {
      shouldHaveError(formMapping.bind(Map("first" -> "Jackson", "last" -> "Jackson")))
    }

    "use global constraints on unbindAndValidate" in {
      shouldHaveError(formMapping.unbindAndValidate(Person("Jackson", "Jackson")))
    }
  }

}
