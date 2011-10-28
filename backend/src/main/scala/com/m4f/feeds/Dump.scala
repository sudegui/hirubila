package com.m4f.feeds

import com.m4f.business.domain._
import scala.actors.Actor
import scala.collection.mutable.Buffer

abstract class DumpMessage
case class LoadFeed extends DumpMessage
case class Store(courses: Buffer[Course]) extends DumpMessage
case class Stop extends DumpMessage

abstract class FeedMessage
case class Parse extends FeedMessage

abstract class CatalogMessage
case class Create(course:Course) extends FeedMessage

class Dump(school: School) extends Actor {
	def act() {
		val feed:Feed = new Feed(school.getFeed())
		feed.start()
		loop {
			react {
    			case LoadFeed =>
    				println("Provider loading feed  ")
    				feed ! Parse
    			case Store(courses) =>
    				println("Provider storing courses  ")
    				
    			case Stop =>
    				Console.println("Pong: stop")
    				exit()
    	}
    }
  }

}

class Feed(url: String) extends Actor {
  def act() {
		loop {
			react {
    			case Parse =>
    				println("Parsing feed  " + url)
    				//Resulta
    				val course: Buffer[Course] = Buffer()
    				sender ! Store
    		case Stop =>
    			Console.println("Pong: stop")
    			exit()
    	}
    }
  }
}

class Catalog() extends Actor {
  def act() {
		loop {
			react {
    			case Create(course) =>
    				println("Creating course catalog")
    				//Resulta
    				val course: Buffer[Course] = Buffer()
    				sender ! Store
    			case Stop =>
    				Console.println("Pong: stop")
    				exit()
    	}
    }
  }
}

