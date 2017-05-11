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

package com.google.cloud.tools.gradle.appengine.flexible;

import com.google.cloud.tools.gradle.appengine.core.AppEngineCorePlugin;
import com.google.cloud.tools.gradle.appengine.core.DeployExtension;
import com.google.cloud.tools.gradle.appengine.util.ExtensionUtil;
import java.io.File;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.bundling.War;

/** Plugin definition for App Engine flexible environments. */
public class AppEngineFlexiblePlugin implements Plugin<Project> {

  public static final String APP_ENGINE_FLEXIBLE_TASK_GROUP = "App Engine flexible environment";
  private static final String STAGE_TASK_NAME = "appengineStage";

  private static final String STAGED_APP_DIR_NAME = "staged-app";
  public static final String STAGE_EXTENSION = "stage";

  private Project project;
  private StageFlexibleExtension stageExtension;

  @Override
  public void apply(Project project) {
    this.project = project;
    project.getPluginManager().apply(AppEngineCorePlugin.class);

    configureExtensions();
    createStageTask();

    AppEngineCorePlugin.overrideCoreTasksGroup(project, APP_ENGINE_FLEXIBLE_TASK_GROUP);
  }

  private void configureExtensions() {
    // obtain extensions defined by core plugin.
    ExtensionAware appengine =
        new ExtensionUtil(project).get(AppEngineCorePlugin.APPENGINE_EXTENSION);

    // create the flexible stage extension and set defaults.
    stageExtension =
        appengine.getExtensions().create(STAGE_EXTENSION, StageFlexibleExtension.class, project);
    File defaultStagedAppDir = new File(project.getBuildDir(), STAGED_APP_DIR_NAME);
    stageExtension.setStagingDirectory(defaultStagedAppDir);
    stageExtension.setAppEngineDirectory(new File(project.getProjectDir(), "src/main/appengine"));
    File dockerOptionalDir = new File(project.getProjectDir(), "src/main/docker");
    if (dockerOptionalDir.exists()) {
      // only set the docker directory if we find it.
      stageExtension.setDockerDirectory(dockerOptionalDir);
    }

    // obtain deploy extension set defaults
    DeployExtension deploy = new ExtensionUtil(appengine).get(AppEngineCorePlugin.DEPLOY_EXTENSION);
    deploy.setDeployables(new File(defaultStagedAppDir, "app.yaml"));
    // grab default project configuration from staging default
    deploy.setAppEngineDirectory(stageExtension.getAppEngineDirectory());

    project.afterEvaluate(
        new Action<Project>() {
          @Override
          public void execute(Project project) {
            // we can only set the default location of "archive" after project evaluation (callback)
            if (stageExtension.getArtifact() == null) {
              if (project.getPlugins().hasPlugin(WarPlugin.class)) {
                War war = (War) project.getProperties().get("war");
                stageExtension.setArtifact(war.getArchivePath());
              } else if (project.getPlugins().hasPlugin(JavaPlugin.class)) {
                Jar jar = (Jar) project.getProperties().get("jar");
                stageExtension.setArtifact(jar.getArchivePath());
              } else {
                throw new GradleException("Could not find JAR or WAR configuration");
              }
            }
          }
        });
  }

  private void createStageTask() {
    StageFlexibleTask stageTask =
        project
            .getTasks()
            .create(
                STAGE_TASK_NAME,
                StageFlexibleTask.class,
                new Action<StageFlexibleTask>() {
                  @Override
                  public void execute(final StageFlexibleTask stageTask) {
                    stageTask.setGroup(APP_ENGINE_FLEXIBLE_TASK_GROUP);
                    stageTask.setDescription(
                        "Stage an App Engine flexible environment application for deployment");
                    stageTask.dependsOn(BasePlugin.ASSEMBLE_TASK_NAME);

                    project.afterEvaluate(
                        new Action<Project>() {
                          @Override
                          public void execute(Project project) {
                            stageTask.setStagingConfig(stageExtension);
                          }
                        });
                  }
                });
    project.getTasks().getByName(AppEngineCorePlugin.DEPLOY_TASK_NAME).dependsOn(stageTask);
  }
}
