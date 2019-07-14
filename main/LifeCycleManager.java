package main;

import database.loader.LoaderThread;
import database.loader.Project;
import javafx.application.Platform;
import userinterface.main.center.VideoPlayer;
import userinterface.scene.SceneUtil;
import utils.FileUtil;

import java.io.File;

public abstract class LifeCycleManager {
	private static Project project;
    
    public static void initialize() {
        VideoPlayer.checkLibs();

        InstanceManager.createInstances();

        SceneUtil.createIntroScene();
        SceneUtil.stageLayoutIntro();
        SceneUtil.showIntroScene();

        Platform.runLater(() -> {
            SceneUtil.createProjectScene();
            SceneUtil.createMainScene();
        });
    }
    public static void startLoading(Project project) {
		LifeCycleManager.project = project;
        
        String projectFilePath = project.getProjectFilePath();
        String projectDirectory = projectFilePath.substring(0, projectFilePath.lastIndexOf(File.separatorChar));
		String sourceDirectory = project.getSourceDirectory();

        FileUtil.initialize(projectDirectory, sourceDirectory);
        new LoaderThread().start();
        SceneUtil.showMainScene();
        SceneUtil.stageLayoutMain();
		InstanceManager.createInstancesEvents();
    }
	
	public static Project getProject() {
		return project;
	}
}
