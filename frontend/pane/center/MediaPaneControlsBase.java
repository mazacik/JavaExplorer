package application.frontend.pane.center;

import application.backend.base.entity.Entity;
import application.backend.loader.cache.CacheReader;
import application.backend.util.ConverterUtil;
import application.backend.util.FileUtil;
import application.backend.util.enums.Direction;
import application.frontend.component.simple.TextNode;
import application.frontend.decorator.ColorUtil;
import application.frontend.decorator.SizeUtil;
import application.main.InstanceCollector;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.File;

public class MediaPaneControlsBase extends BorderPane implements InstanceCollector {
	private TextNode btnPlayPause;
	
	private TextNode btnSkipBackward;
	private TextNode btnSkipForward;
	
	private TextNode btnSkipBackward5s;
	private TextNode btnSkipForward5s;
	
	private TextNode btnMute;
	private TextNode btnSnapshot;
	
	private TextNode btnPrevious;
	private TextNode btnNext;
	
	private TextNode lblTimeCurrent;
	private TextNode lblTimeTotal;
	
	private ProgressBar progressBar = new ProgressBar();
	
	public MediaPaneControlsBase(VideoPlayer videoPlayer) {
		progressBar.prefWidthProperty().bind(this.prefWidthProperty());
		progressBar.setProgress(0);
		progressBar.skinProperty().addListener((observable, oldValue, newValue) -> progressBar.lookup(".bar").setStyle("-fx-background-insets: 1 1 1 1; -fx-padding: 0.25em;"));
		
		btnSkipBackward = new TextNode("SkipBackward", true, true, false, true);
		btnSkipBackward5s = new TextNode("SkipBackward5s", true, true, false, true);
		btnPlayPause = new TextNode("PlayPause", true, true, false, true);
		btnSkipForward = new TextNode("SkipForward", true, true, false, true);
		btnSkipForward5s = new TextNode("SkipForward5s", true, true, false, true);
		btnMute = new TextNode("Mute", true, true, false, true);
		btnSnapshot = new TextNode("Snapshot", true, true, false, true);
		
		btnPrevious = new TextNode("Previous", true, true, false, true);
		btnNext = new TextNode("Next", true, true, false, true);
		
		lblTimeCurrent = new TextNode("00:00:00", true, true, false, true);
		lblTimeTotal = new TextNode("23:59:59", true, true, false, true);
		
		this.setBackground(ColorUtil.getBackgroundDef());
		
		initEvents(videoPlayer);
	}
	private void initEvents(VideoPlayer videoPlayer) {
		if (videoPlayer != null) {
			btnPlayPause.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
				if (videoPlayer.hasMedia()) {
					if (videoPlayer.isPlaying()) {
						videoPlayer.pause();
					} else {
						videoPlayer.resume();
					}
				}
			});
			
			long timeDelta = 5000; // time in milliseconds
			btnSkipBackward5s.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
				if (videoPlayer.hasMedia()) {
					videoPlayer.getControls().skipTime(-timeDelta);
					if (!videoPlayer.isPlaying()) {
						videoPlayer.renderFrame();
						float position = videoPlayer.getPosition();
						setVideoProgress(progressBar.getProgress() - position);
					}
				}
			});
			btnSkipForward5s.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
				if (videoPlayer.hasMedia()) {
					videoPlayer.getControls().skipPosition(+timeDelta);
					if (!videoPlayer.isPlaying()) {
						videoPlayer.renderFrame();
						float position = videoPlayer.getPosition();
						setVideoProgress(progressBar.getProgress() + position);
					}
				}
			});
			
			float skipDelta = 0.05f; // 0.01f = 1% of media length
			btnSkipBackward.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
				if (videoPlayer.hasMedia()) {
					videoPlayer.getControls().skipPosition(-skipDelta);
					if (!videoPlayer.isPlaying()) {
						videoPlayer.renderFrame();
						setVideoProgress(progressBar.getProgress() - skipDelta);
					}
				}
			});
			btnSkipForward.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
				if (videoPlayer.hasMedia()) {
					videoPlayer.getControls().skipPosition(+skipDelta);
					if (!videoPlayer.isPlaying()) {
						videoPlayer.renderFrame();
						setVideoProgress(progressBar.getProgress() + skipDelta);
					}
				}
			});
			
			progressBar.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					if (videoPlayer.hasMedia()) {
						double eventX = event.getX();
						double barWidth = progressBar.getWidth();
						double position = (eventX / barWidth);
						videoPlayer.setPosition(position);
						if (!videoPlayer.isPlaying()) {
							videoPlayer.renderFrame();
						}
					}
				}
			});
			
			btnMute.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, videoPlayer::swapMute);
			btnSnapshot.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
				Entity entity = target.get();
				File cacheFile = new File(FileUtil.getCacheFilePath(entity));
				int thumbSize = (int) SizeUtil.getGalleryTileSize();
				mediaPane.getVideoPlayer().snapshot(cacheFile, thumbSize, thumbSize);
				
				Image cache = CacheReader.get(entity);
				entity.getGalleryTile().setImage(cache);
			});
		}
		
		btnPrevious.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
			target.move(Direction.LEFT);
			select.set(target.get());
			reload.doReload();
		});
		btnNext.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, () -> {
			target.move(Direction.RIGHT);
			select.set(target.get());
			reload.doReload();
		});
	}
	
	public void setVideoMode(boolean enabled) {
		if (enabled) {
			HBox hBoxLeft = new HBox();
			hBoxLeft.getChildren().add(btnPrevious);
			hBoxLeft.getChildren().add(lblTimeCurrent);
			
			HBox hBoxCenter = new HBox();
			hBoxCenter.getChildren().add(btnSkipBackward5s);
			hBoxCenter.getChildren().add(btnSkipBackward);
			hBoxCenter.getChildren().add(btnPlayPause);
			hBoxCenter.getChildren().add(btnSkipForward);
			hBoxCenter.getChildren().add(btnSkipForward5s);
			hBoxCenter.getChildren().add(btnMute);
			hBoxCenter.setAlignment(Pos.CENTER);
			
			HBox hBoxRight = new HBox();
			hBoxRight.getChildren().add(lblTimeTotal);
			hBoxRight.getChildren().add(btnNext);
			
			this.setLeft(hBoxLeft);
			this.setRight(hBoxRight);
			this.setCenter(hBoxCenter);
			this.setBottom(progressBar);
		} else {
			if (mediaPane.getVideoPlayer() != null)
				mediaPane.getVideoPlayer().pause();
			
			this.setLeft(btnPrevious);
			this.setRight(btnNext);
			this.setCenter(null);
			this.setBottom(null);
		}
	}
	public void setVideoProgress(double value) {
		if (value > 0 && value <= 1) Platform.runLater(() -> progressBar.setProgress(value));
	}
	
	public void setTimeCurrent(long time) {
		Platform.runLater(() -> lblTimeCurrent.setText(ConverterUtil.msToHMS(time)));
	}
	public void setTimeTotal(long time) {
		Platform.runLater(() -> lblTimeTotal.setText(ConverterUtil.msToHMS(time)));
	}
}