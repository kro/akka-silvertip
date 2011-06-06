package akka.silvertip

import silvertip.Events

class EventsDispatchRunnable(events: Events) extends Runnable {
  def run = events.dispatch
}

class EventsDispatchThread(val events: Events) extends Thread(new EventsDispatchRunnable(events))
