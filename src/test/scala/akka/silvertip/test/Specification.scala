package akka.silvertip.test

import java.util.concurrent.{Executors, TimeUnit}
import org.specs.SpecificationWithJUnit
import org.specs.mock.Mockito

trait Specification extends SpecificationWithJUnit with Mockito {
  def waitUntil(done: => Boolean) = {
    val executor = Executors.newSingleThreadScheduledExecutor
    executor.scheduleAtFixedRate(new Runnable {
      def run { if (done) executor.shutdown }
    }, 0, 1, TimeUnit.SECONDS)
    executor.awaitTermination(10, TimeUnit.SECONDS)
  }
}
