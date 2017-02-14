package com.akka.http.utils

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.ext.JavaTypesSerializers
import org.json4s.{CustomSerializer, DefaultFormats, jackson}

trait AutoMarshaller extends Json4sSupport {
  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats ++ List(ISODateTimeSerializer) ++ org.json4s.ext.JodaTimeSerializers.all ++ JavaTypesSerializers.all

  case object ISODateTimeSerializer extends CustomSerializer[DateTime](format => ( {
    case JString(s) => ISODateTimeFormat.dateTimeParser().parseDateTime(s)
    case JNull => null
  }, {
    case d: DateTime => JString(format.dateFormat.format(d.toDate))
  }))
}