package demo

import akka.actor.Actor._
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

import com.typesafe.config.ConfigFactory

class AkkaBcastTestSink extends  Actor  with akka.actor.ActorLogging{

  def receive = {
    case RegisterWith(a) => a ! RegisterSink
    case msg => log.info("sink received: "+msg)
  }

}

object AkkaBcastTestSink extends App {
  private def usage:Nothing = {
    Console.err.println("usage: "+this.getClass().getName()+" [host:]port ")
    System.exit(1)
    throw new Exception("can't get here")
  }

  override def main(args: Array[String]): Unit = {
    val HostPort = new scala.util.matching.Regex("""(\w+:)?(\d+)""")
    val (hostStr, portStr) = args match{
      case Array(HostPort(host, port)) =>
        System.err.println("host: "+host+" port: "+port)
        (if (host == null || host.length == 0) "localhost" else host, port.toInt.toString)
      case _ => usage
    }
    val configStr = """
  akka {
  actor {
    provider = "akka.remote.RemoteActorRefProvider"
  }
          remote.transport = "akka.remote.netty.NettyRemoteSupport"
      cluster.nodename = "nsink"
  }
"""
    val system = ActorSystem("testSink",ConfigFactory.parseString(configStr))
    val sink = system.actorOf(Props[AkkaBcastTestSink])
    val remoteActor =    system.actorFor("akka://" + AkkaBcastServer.serviceName +"@"+hostStr+":"+portStr+"/user/"+ AkkaBcastServer.actorName)
    sink ! new RegisterWith(remoteActor)
    Console.out.println("registered sink")
  }

}