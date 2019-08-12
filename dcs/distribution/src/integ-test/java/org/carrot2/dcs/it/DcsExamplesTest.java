/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * https://www.carrot2.org/carrot2.LICENSE
 */
package org.carrot2.dcs.it;

import com.carrotsearch.console.launcher.ExitCode;
import com.carrotsearch.console.launcher.ExitCodes;
import com.carrotsearch.console.launcher.Launcher;
import java.io.IOException;
import java.net.URI;
import org.assertj.core.api.Assertions;
import org.carrot2.dcs.examples.E01_DcsConfiguration;
import org.carrot2.dcs.examples.E02_DcsCluster;
import org.junit.Test;

public class DcsExamplesTest extends AbstractDistributionTest {
  @Test
  public void runExamples() throws IOException {
    try (DcsService service = startDcs()) {
      URI dcsService = service.getAddress().resolve("/service/");

      ExitCode exitCode;

      exitCode =
          new Launcher()
              .runCommand(
                  new E01_DcsConfiguration(),
                  E01_DcsConfiguration.ARG_DCS_URI,
                  dcsService.toString());
      Assertions.assertThat(exitCode).isEqualTo(ExitCodes.SUCCESS);

      exitCode =
          new Launcher()
              .runCommand(new E02_DcsCluster(), E02_DcsCluster.ARG_DCS_URI, dcsService.toString());
      Assertions.assertThat(exitCode).isEqualTo(ExitCodes.SUCCESS);
    }
  }
}
