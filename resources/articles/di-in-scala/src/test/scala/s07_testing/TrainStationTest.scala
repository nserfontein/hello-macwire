package s07_testing

import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import s07_testing.shunting.{PointSwitcher, ShuntingModule}

class TrainStationTest extends FlatSpec with MockFactory {

  it should "work" in {

    // given
    val mockPointSwitcher = mock[PointSwitcher]

    // when
    val moduleToTest = new ShuntingModule {
      override lazy val pointSwitcher = mockPointSwitcher
    }

    moduleToTest.trainShunter.shunt()

    // then
    // verify(mockPointSwitcher).switch(...)

  }


}
