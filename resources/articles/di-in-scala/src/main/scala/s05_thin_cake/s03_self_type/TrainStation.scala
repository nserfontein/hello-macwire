package s05_thin_cake.s03_self_type

import s05_thin_cake.s03_self_type.loading.LoadingModule
import s05_thin_cake.s03_self_type.shunting.ShuntingModule
import s05_thin_cake.s03_self_type.station.StationModule

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
  import s05_thin_cake.s03_self_type.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  // dependencies expressed using self type
  trait LoadingModule {
    this: ShuntingModule =>

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s05_thin_cake.s03_self_type.loading.TrainLoader
  import s05_thin_cake.s03_self_type.shunting.TrainShunter

  case class TrainDispatch()

  case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  // dependencies expressed using self type
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
