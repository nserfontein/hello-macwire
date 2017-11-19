package s05_thin_cake.s01_abstract_members

import s05_thin_cake.s01_abstract_members.loading.LoadingModule
import s05_thin_cake.s01_abstract_members.shunting.ShuntingModule
import s05_thin_cake.s01_abstract_members.station.StationModule

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
  import s05_thin_cake.s01_abstract_members.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  trait LoadingModule {

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

    def pointSwitcher: PointSwitcher

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s05_thin_cake.s01_abstract_members.loading.TrainLoader
  import s05_thin_cake.s01_abstract_members.shunting.TrainShunter

  case class TrainDispatch()

  case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  trait StationModule {

    lazy val trainDispatch = wire[TrainDispatch]

    lazy val trainStation = wire[TrainStation]

    def trainShunter: TrainShunter
    def trainLoader: TrainLoader

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule

  modules.trainStation.prepareAndDispatchNextTrain()

}
