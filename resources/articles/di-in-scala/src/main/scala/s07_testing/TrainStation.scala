package s07_testing

import s07_testing.loading.LoadingModule
import s07_testing.shunting.ShuntingModule
import s07_testing.station.StationModule

package shunting {

  import com.softwaremill.macwire.wire

  case class PointSwitcher()

  case class TrainCarCoupler()

  case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler) {
    def shunt() = {
      println("shunting!")
    }
  }

  trait ShuntingModule {
    lazy val pointSwitcher = wire[PointSwitcher]
    lazy val trainCarCoupler = wire[TrainCarCoupler]
    lazy val trainShunter = wire[TrainShunter]
  }

}

package loading {

  import com.softwaremill.macwire.wire
  import s07_testing.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  trait LoadingModule {
    this: ShuntingModule =>

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s07_testing.loading.TrainLoader
  import s07_testing.shunting.TrainShunter

  case class TrainDispatch()

  case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  trait StationModule {
    this: ShuntingModule with LoadingModule =>

    lazy val trainDispatch = wire[TrainDispatch]

    lazy val trainStation = wire[TrainStation]

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule

  modules.trainStation.prepareAndDispatchNextTrain()

}
