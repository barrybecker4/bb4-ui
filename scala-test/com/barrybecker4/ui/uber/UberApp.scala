/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.ui1.application.ApplicationFrame
import com.barrybecker4.ui1.components.SplashScreen
import com.barrybecker4.ui1.util.GUIUtil
import com.barrybecker4.ui1.util.Log
import javax.swing.JTabbedPane
import java.util.Collections


/**
  * An app that tries to demonstrate the use of most of the UI components in this package.
  * @author Barry Becker
  */
object UberApp {
  val IMAGE_ROOT = "com/barrybecker4/ui1/uber/images/" // NON-NLS

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    new UberApp
  }
}

class UberApp() extends ApplicationFrame("UberApp Demo") {
  override protected def createUI(): Unit = {
    val loc = getClass.getPackage.getName
    AppContext.initialize("ENGLISH", Collections.singletonList(loc + ".message"), new Log)
    val image = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "pool_pennies_small.jpg")
    new SplashScreen(image, this, 2000)

    val mainPanel = new JTabbedPane

    mainPanel.addTab("Input Elements", new MainTexturePanel)
    mainPanel.addTab("Histogram", new HistogramTestPanel)
    mainPanel.addTab("Multi-Function", new MultiFunctionTestPanel)
    this.add(mainPanel)
    super.createUI()
  }
}
