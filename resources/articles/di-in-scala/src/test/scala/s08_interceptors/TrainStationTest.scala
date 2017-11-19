package s08_interceptors

import com.softwaremill.macwire.aop.{Interceptor, NoOpInterceptor}
import org.scalatest.FlatSpec
import s08_interceptors.shunting.ShuntingModule

class TrainStationTest extends FlatSpec {

  it should "work" in {

    // given
    val moduleToTest = new ShuntingModule {
      override def logEvents: Interceptor = NoOpInterceptor
    }

    moduleToTest.trainShunter.shunt()

    // then
    // verify(mockPointSwitcher).switch(...)

  }

}
