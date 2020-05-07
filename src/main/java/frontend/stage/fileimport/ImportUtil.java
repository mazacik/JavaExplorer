package frontend.stage.fileimport;

import backend.cache.CacheLoader;
import backend.control.reload.Notifier;
import backend.control.reload.Reload;
import backend.list.BaseList;
import backend.list.entity.Entity;
import backend.list.entity.EntityList;
import backend.misc.FileUtil;
import backend.misc.Project;
import backend.override.Thread;
import javafx.application.Platform;
import main.Main;

import java.io.File;
import java.util.logging.Logger;

public abstract class ImportUtil {
	public static void startImportThread(ImportMode importMode, String directory, BaseList<File> fileList) {
		new Thread(() -> {
			EntityList entityList = ImportUtil.importFiles(importMode, directory, fileList);
			
			if (entityList != null && !entityList.isEmpty()) {
				CacheLoader.startCacheThread(entityList);
				
				Main.ENTITYLIST.addAll(entityList);
				Main.ENTITYLIST.sort();
				
				Main.FILTER.getLastImport().setAll(entityList);
			}
			
			Platform.runLater(() -> {
				ImportStage.displayResults(entityList);
				Reload.notify(Notifier.ENTITYLIST_CHANGED);
				Reload.start();
			});
		}).start();
	}
	
	private static EntityList importFiles(ImportMode importMode, String directory, BaseList<File> fileList) {
		Main.STAGE.getMainScene().showLoadingBar(Thread.currentThread(), fileList.size());
		
		EntityList entityList = new EntityList();
		for (File fileFrom : fileList) {
			if (Thread.currentThread().isInterrupted()) return null;
			
			Entity entity = ImportUtil.importFile(importMode, directory, fileFrom);
			if (entity != null) {
				entityList.add(entity);
			}
			
			Main.STAGE.getMainScene().advanceLoadingBar(Thread.currentThread());
		}
		
		Main.STAGE.getMainScene().hideLoadingBar(Thread.currentThread());
		
		return entityList;
	}
	private static Entity importFile(ImportMode importMode, String directory, File fileFrom) {
		String pathFrom = fileFrom.getAbsolutePath();
		String pathTo = Project.getCurrent().getDirectory() + File.separator + FileUtil.createEntityName(fileFrom, directory);
		
		File fileTo = new File(pathTo);
		
		if (!fileTo.exists()) {
			if (ImportUtil.doImport(importMode, pathFrom, pathTo)) {
				return new Entity(fileTo);
			}
		} else {
			if (fileTo.length() != fileFrom.length()) {
				//it's a different file with the same name, prepend it with it's size and import it
				pathTo = Project.getCurrent().getDirectory() + File.separator + fileFrom.length() + "-" + FileUtil.createEntityName(fileFrom, directory);
				if (ImportUtil.doImport(importMode, pathFrom, pathTo)) {
					return new Entity(new File(pathTo));
				}
			} else {
				Logger.getGlobal().info("IMPORT: Identical file already exists:\n" + fileFrom.getName());
			}
		}
		
		return null;
	}
	private static boolean doImport(ImportMode importMode, String pathFrom, String pathTo) {
		switch (importMode) {
			case COPY:
				return FileUtil.copyFile(pathFrom, pathTo);
			case MOVE:
				return FileUtil.moveFile(pathFrom, pathTo);
			default:
				return false;
		}
	}
}
