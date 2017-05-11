/*
 * Copyright (c) 2016 Google Inc. All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.google.cloud.tools.gradle.appengine.standard;

import com.google.cloud.tools.gradle.appengine.BuildResultFilter;
import com.google.cloud.tools.gradle.appengine.TestProject;
import com.google.cloud.tools.gradle.appengine.core.AppEngineCorePlugin;
import com.google.cloud.tools.gradle.appengine.core.DeployExtension;
import com.google.cloud.tools.gradle.appengine.util.ExtensionUtil;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.specs.Spec;
import org.gradle.testkit.runner.BuildResult;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Test App Engine Standard Plugin configuration. */
public class AppEngineStandardPluginTest {

  @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder();

  private TestProject createTestProject() throws IOException {
    return new TestProject(testProjectDir.getRoot()).addBuildFile().addAppEngineWebXml();
  }

  @Test
  public void testDeploy_taskTree() throws IOException {
    BuildResult buildResult = createTestProject().applyGradleRunner("appengineDeploy", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStage",
            ":appengineDeploy");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testDeployCron_taskTree() throws IOException {
    BuildResult buildResult =
        createTestProject().applyGradleRunner("appengineDeployCron", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStage",
            ":appengineDeployCron");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testDeployDispatch_taskTree() throws IOException {
    BuildResult buildResult =
        createTestProject().applyGradleRunner("appengineDeployDispatch", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStage",
            ":appengineDeployDispatch");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testDeployDos_taskTree() throws IOException {
    BuildResult buildResult =
        createTestProject().applyGradleRunner("appengineDeployDos", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStage",
            ":appengineDeployDos");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testDeployIndex_taskTree() throws IOException {
    BuildResult buildResult =
        createTestProject().applyGradleRunner("appengineDeployIndex", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStage",
            ":appengineDeployIndex");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testDeployQueue_taskTree() throws IOException {
    BuildResult buildResult =
        createTestProject().applyGradleRunner("appengineDeployQueue", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStage",
            ":appengineDeployQueue");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testRun_taskTree() throws IOException {
    BuildResult buildResult = createTestProject().applyGradleRunner("appengineRun", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineRun");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testStart_taskTree() throws IOException {
    BuildResult buildResult = createTestProject().applyGradleRunner("appengineStart", "--dry-run");

    final List<String> expected =
        ImmutableList.of(
            ":compileJava",
            ":processResources",
            ":classes",
            ":war",
            ":explodeWar",
            ":assemble",
            ":appengineStart");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testStop_taskTree() throws IOException {
    BuildResult buildResult = createTestProject().applyGradleRunner("appengineStop", "--dry-run");

    final List<String> expected = Collections.singletonList(":appengineStop");

    Assert.assertEquals(expected, BuildResultFilter.extractTasks(buildResult));
  }

  @Test
  public void testDefaultConfiguration() throws IOException {
    Project p =
        new TestProject(testProjectDir.getRoot())
            .addAppEngineWebXml()
            .applyStandardProjectBuilder();

    ExtensionAware ext =
        (ExtensionAware) p.getExtensions().getByName(AppEngineCorePlugin.APPENGINE_EXTENSION);
    DeployExtension deployExt = new ExtensionUtil(ext).get(AppEngineCorePlugin.DEPLOY_EXTENSION);
    StageStandardExtension stageExt =
        new ExtensionUtil(ext).get(AppEngineStandardPlugin.STAGE_EXTENSION);
    RunExtension run = new ExtensionUtil(ext).get(AppEngineStandardPlugin.RUN_EXTENSION);

    Assert.assertEquals(
        new File(p.getBuildDir(), "exploded-" + p.getName()), stageExt.getSourceDirectory());
    Assert.assertEquals(new File(p.getBuildDir(), "staged-app"), stageExt.getStagingDirectory());
    Assert.assertEquals(
        new File(p.getBuildDir(), "staged-app/WEB-INF/appengine-generated"),
        deployExt.getAppEngineDirectory());
    Assert.assertEquals(
        Collections.singletonList(new File(p.getBuildDir(), "staged-app/app.yaml")),
        deployExt.getDeployables());
    Assert.assertEquals(
        Collections.singletonList(new File(p.getBuildDir(), "exploded-" + p.getName())),
        run.getServices());
    Assert.assertFalse(new File(testProjectDir.getRoot(), "src/main/docker").exists());
    Assert.assertEquals(20, run.getStartSuccessTimeout());
  }

  @Test
  public void testAppEngineTaskGroupAssignment() throws IOException {
    Project p =
        new TestProject(testProjectDir.getRoot())
            .addAppEngineWebXml()
            .applyStandardProjectBuilder();

    p.getTasks()
        .matching(
            new Spec<Task>() {
              @Override
              public boolean isSatisfiedBy(Task task) {
                return task.getName().startsWith("appengine");
              }
            })
        .all(
            new Action<Task>() {
              @Override
              public void execute(Task task) {
                Assert.assertEquals(
                    AppEngineStandardPlugin.APP_ENGINE_STANDARD_TASK_GROUP, task.getGroup());
              }
            });
  }
}
