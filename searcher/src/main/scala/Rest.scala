import api.Api
import receiver.web.{BootedCore, CoreActors}
import receiver.web.Web

object Rest extends App with BootedCore with CoreActors with Api with Web