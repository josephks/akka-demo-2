package demo

import akka.actor.Actor._
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }


object AkkaBcastTestClient extends App{

  private def usage(): Nothing = {
    Console.err.println("usage: AkkaBcastMain [host:]port msg [msg [msg] ...]")
    System.exit(1)
    throw new Exception("can't get here")
  }
  override def main(args: Array[String]): Unit = {
    val HostPort = new scala.util.matching.Regex("""(\w+:)?(\d+)""")
    val (hostStr, portStr) = args match{
      case Array(HostPort(host, port), _, _*) =>  (if (host == null || host.length == 0) "localhost" else host, port.toInt.toString)
      case _ => usage()
    }
    val configStr = """
  akka {
  actor {
    provider = "akka.remote.RemoteActorRefProvider"
  }
          remote.transport = "akka.remote.netty.NettyRemoteSupport"
      cluster.nodename = "client"
  }
"""

    val system = ActorSystem("testClient",ConfigFactory.parseString(configStr))
    val actor = system.actorFor("akka://" + AkkaBcastServer.serviceName +"@"+hostStr+":"+portStr+"/user/"+ AkkaBcastServer.actorName)
    for(arg <- args.slice(1, args.length)){
      actor  !  BroadcastMessage("net.tupari.akkabcast",  arg)
       Console.err.println("sent msg: "+arg)
    }

  }

}


