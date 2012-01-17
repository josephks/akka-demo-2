package demo

import akka.actor.Actor._

import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import akka.event.Logging


class AkkaBcastServer extends akka.actor.Actor  with akka.actor.ActorLogging{

  var sinks = Set[ActorRef]()

  def receive = {
    case RegisterSink() =>
      sinks += sender
      log.info("registred sink")
    case bm @ BroadcastMessage(_,_) =>
      log.info("sending "+bm+" to all sinks: "+sinks)
      sinks.foreach( sink => sink ! bm)
    case "ping" => sender ! "pong"
    case something @ _ => // ignore other
      log.warning("received something else: "+something)
  }
}

object AkkaBcastServer {
  val actorName = "broadcast-actor"
  val serviceName = "bacastsevice"
}

case class RegisterSink(){}
case class RegisterWith(a:ActorRef){}
case class BroadcastMessage(domain: String, msg: String)


object AkkaBcastMain extends App{

  private def usage = {
    Console.err.println("usage: demo.AkkaBcastMain [host] port")
    System.exit(1)
  }
  override def main(args: Array[String]): Unit = {
    val (hostStr:Option[String], portStr) = args match{
      case Array(host, port) =>   (Some(host), port.toInt)
      case Array(port) =>   (None, port.toInt)
      case _ => usage
    }
    val sb = new StringBuilder
    sb.append ("""
                     bcast {
  include "common"

  akka {
    remote.server.port = """ + portStr   )
    hostStr.foreach( x => sb.append("""
          remote.server.hostname =  """ +   x )  )
    sb.append("""
      cluster.nodename = "n1"
  }
}
""")


    Console.out.println("servermain: creating system")
    val system = ActorSystem(AkkaBcastServer.serviceName, ConfigFactory.parseString(sb.toString))
    Console.out.println("servermain: creating actor")
    val remoteActor = system.actorOf(Props[AkkaBcastServer], AkkaBcastServer.actorName)
    Console.out.println("servermain: pinging local actor")
    import akka.util.duration._
    val fut = remoteActor ?( "ping", timeout=10.seconds)
    Console.out.println("got fut")
    fut.onComplete(x => Console.out.println("rec from server: "+x))
    Console.out.println("servermain: finished")
  }

}