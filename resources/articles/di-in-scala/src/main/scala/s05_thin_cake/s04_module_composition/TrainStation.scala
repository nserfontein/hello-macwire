package s05_thin_cake.s04_module_composition

import s05_thin_cake.s04_module_composition.loading.LoadingModule
import s05_thin_cake.s04_module_composition.shunting.ShuntingModule
import s05_thin_cake.s04_module_composition.station.StationModule

package shunting {

  import com.softwaremill.macwire.{Module, wire}

  case class PointSwitcher()

  case class TrainCarCoupler()

  case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

  @Module
  trait ShuntingModule {
    lazy val pointSwitcher = wire[PointSwitcher]
    lazy val trainCarCoupler = wire[TrainCarCoupler]
    lazy val trainShunter = wire[TrainShunter]
  }

}

package loading {

  import com.softwaremill.macwire.{Module, wire}
  import s05_thin_cake.s04_module_composition.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  @Module
  trait LoadingModule {
    this: ShuntingModule =>

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s05_thin_cake.s04_module_composition.loading.TrainLoader
  import s05_thin_cake.s04_module_composition.shunting.TrainShunter

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

package stats {

  import com.softwaremill.macwire.wire
  import s05_thin_cake.s04_module_composition.loading.TrainLoader
  import s05_thin_cake.s04_module_composition.shunting.TrainShunter

  case class LoadingStats(trainLoader: TrainLoader)

  case class ShuntingStats(trainShunter: TrainShunter)

  class StatsModule(shuntingModule: ShuntingModule, loadingModule: LoadingModule) {

    lazy val loadingStats = wire[LoadingStats]
    lazy val shuntingStats = wire[ShuntingStats]
  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule

  modules.trainStation.prepareAndDispatchNextTrain()

}
