package s05_thin_cake.s02_extends

import s05_thin_cake.s02_extends.loading.LoadingModule
import s05_thin_cake.s02_extends.shunting.ShuntingModule
import s05_thin_cake.s02_extends.station.StationModule

package shunting {

  import com.softwaremill.macwire.wire

  case class PointSwitcher()

  case class TrainCarCoupler()

  case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

  trait ShuntingModule {
    lazy val pointSwitcher = wire[PointSwitcher]
    lazy val trainCarCoupler = wire[TrainCarCoupler]
    lazy val trainShunter = wire[TrainShunter]
  }

}

package loading {

  import com.softwaremill.macwire.wire
  import s05_thin_cake.s02_extends.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  // dependencies expressed using extends
  trait LoadingModule extends ShuntingModule {

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s05_thin_cake.s02_extends.loading.TrainLoader
  import s05_thin_cake.s02_extends.shunting.TrainShunter

  case class TrainDispatch()

  case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  // dependencies expressed using extends
  trait StationModule extends ShuntingModule with LoadingModule {

    lazy val trainDispatch = wire[TrainDispatch]

    lazy val trainStation = wire[TrainStation]

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule

  modules.trainStation.prepareAndDispatchNextTrain()

}
