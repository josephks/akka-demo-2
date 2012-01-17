package demo
 import akka.actor.Actor._

import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import akka.util.duration._
/**
 * Created by IntelliJ IDEA.
 * User: jks
 * Date: 1/16/12
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */

object Pinger extends App{


  class PingActor extends  Actor{

    def receive = {
      case (remoteActor:ActorRef, msg:String) =>
        val fut = remoteActor ? (msg, timeout=10.seconds)
        Console.out.println("got fut")
        fut.onComplete(x => Console.out.println("rec from server: "+x))
      case x =>
        Console.out.println("PingActor: received: "+x)
    }
  }

  override def main(args: Array[String]): Unit = {
    val HostPort = new scala.util.matching.Regex("""(\w+:)?(\d+)""")
    val (hostStr, portStr) = args match{
      case Array(HostPort(host, port)) =>
        Console.err.println("host: "+host+" port: "+port)
        (if (host == null || host.length == 0) "localhost" else host, port.toInt.toString)
      case _ => Console.err.println("error")
      System.exit(1)
    }
    val configStr = """
                     bcast {
  include "common"

  akka {
      cluster.nodename = "np"
  }
}
"""
    val system = ActorSystem("pinger",ConfigFactory.parseString(configStr))
    val remoteActor = system.actorFor("akka://" + AkkaBcastServer.serviceName +"@"+hostStr+":"+portStr+"/user/"+ AkkaBcastServer.actorName)

    val client = system.actorOf(Props[PingActor])
    client ! (remoteActor, "ping")
  }
}