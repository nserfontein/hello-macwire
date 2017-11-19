package s09_advanced_scoping

import com.softwaremill.macwire.scopes.ThreadLocalScope
import s09_advanced_scoping.loading.LoadingModule
import s09_advanced_scoping.shunting.ShuntingModule
import s09_advanced_scoping.station.StationModule

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
  import s09_advanced_scoping.shunting.PointSwitcher

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

  import com.softwaremill.macwire.scopes.Scope
  import com.softwaremill.macwire.wire
  import s09_advanced_scoping.loading.TrainLoader
  import s09_advanced_scoping.shunting.TrainShunter

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

    def session: Scope

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule {
    override def session = new ThreadLocalScope
  }

  // implement a filter which attaches the session to the scope
  // use the filter in the server
  // example: https://github.com/adamw/macwire#scopes

  modules.trainStation.prepareAndDispatchNextTrain()

}
