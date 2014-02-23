import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsSuccess

package object models {

  implicit val jodaDateFormat = new Format[org.joda.time.DateTime] {
    override def reads(d: JsValue): JsResult[DateTime] = {
      JsSuccess(new DateTime(d.as[JsObject].\("$date").as[JsNumber].value.toLong))
    }
    override def writes(d: DateTime): JsValue = {
      JsObject(Seq("$date" -> JsNumber(d.getMillis)))
    }
  }

}
