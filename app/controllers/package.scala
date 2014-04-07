import play.api.i18n.Messages

/**
 * Created by Diego on 06/04/2014.
 */
package object controllers {

  val SuccessMessage:(String,String) = "success_message" -> Messages("success_message")
  val ErrorMessage:(String,String) = "error_message" -> Messages("error_message")
  val NotFoundMessage:(String,String) = "error_message" -> Messages("not_found")

}
