package api

import spray.http.StatusCodes._
import spray.http._
import spray.routing._
import directives.RouteDirectives
import spray.util.LoggingContext
import spray.httpx.marshalling.Marshaller
import spray.http.HttpHeaders.RawHeader
import akka.actor.Actor
import scala.util.control.NonFatal
//import spray.routing.directives.CompletionMagnet

/**
 * Holds potential error response with the HTTP status and optional body
 *
 * @param responseStatus the status code
 * @param response the optional body
 */
case class ErrorResponseException(responseStatus: StatusCode, response: Option[HttpEntity]) extends Exception

/**
 * Provides a hook to catch exceptions and rejections from routes, allowing custom
 * responses to be provided, logs to be captured, and potentially remedial actions.
 *
 * Note that this is not marshalled, but it is possible to do so allowing for a fully
 * JSON API (e.g. see how Foursquare do it).
 */
trait FailureHandling {
  this: HttpService =>

  // For Spray > 1.1-M7 use routeRouteResponse
  // see https://groups.google.com/d/topic/spray-user/zA_KR4OBs1I/discussion
  def rejectionHandler: RejectionHandler = RejectionHandler.Default

  def exceptionHandler(implicit log: LoggingContext) = ExceptionHandler {

    case e: IllegalArgumentException => ctx =>
      loggedFailureResponse(ctx, e,
        message = "The server was asked a question that didn't make sense: " + e.getMessage,
        error = NotAcceptable)

    case e: NoSuchElementException => ctx =>
      loggedFailureResponse(ctx, e,
        message = "The server is missing some information. Try again in a few moments.",
        error = NotFound)

    case t: Throwable => ctx =>
      // note that toString here may expose information and cause a security leak, so don't do it.
      loggedFailureResponse(ctx, t)
  }

  private def loggedFailureResponse(ctx: RequestContext,
                                    thrown: Throwable,
                                    message: String = "The server is having problems.",
                                    error: StatusCode = InternalServerError)
                                   (implicit log: LoggingContext): Unit = {
    log.error(thrown, ctx.request.toString)
    ctx.complete(error, message)
  }

}

/**
 * Allows you to construct Spray ``HttpService`` from a concatenation of routes; and wires in the error handler.
 * @param route the (concatenated) route
 */
class RoutedHttpService(route: Route) extends HttpServiceActor {

  implicit def handler(implicit log: LoggingContext) = ExceptionHandler {
    case NonFatal(ErrorResponseException(statusCode, entity)) => ctx =>{
      log.error(s"Request could not be handled normally $statusCode, $entity ")
      ctx.complete(statusCode, entity)
    }

    case NonFatal(e) => ctx =>
      log.error(s"Request could not be handled normally ${e.getMessage} ")
      ctx.complete(InternalServerError)
    }

  def receive: Receive =
    runRoute(route)(handler, RejectionHandler.Default, context, RoutingSettings.default, LoggingContext.fromActorRefFactory)


}
