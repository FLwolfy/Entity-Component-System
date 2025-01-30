package ecs.engine.component;

import ecs.engine.base.GameComponent;
import ecs.engine.base.GameScene;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class RenderHandler extends GameComponent {

  // Settings
  public int renderOrder = 0;

  // accessible variables
  public double rawWidth;
  public double rawHeight;
  public double width;
  public double height;
  
  // instance variables
  private Node image;
  private GraphicsContext graphicsContext;

  @Override
  public ComponentUpdateOrder COMPONENT_UPDATE_ORDER() {
    return ComponentUpdateOrder.RENDER;
  }

  @Override
  public void onAttached() {
    rawWidth = 0;
    rawHeight = 0;
    width = 0;
    height = 0;
  }

  @Override
  public void start() {
    graphicsContext = GameScene.getRenderGC();
  }

  @Override
  public void transformUpdate() {
    if (image == null) {
      return;
    }

    handleRenderShape();
  }
  
  @Override
  public void update() {
    if (image == null) {
      return;
    }

    // Capture the transformed image
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    WritableImage snapshot = image.snapshot(params, null);

    // Draw the image at the correct transformed position
    graphicsContext.drawImage(snapshot, transform.position.getX() - width / 2, transform.position.getY() - height / 2);
  }

  private void handleRenderShape() {
    // update the image position
    Bounds bounds = image.getBoundsInLocal();
    double centerX = bounds.getMinX() + bounds.getWidth() / 2;
    double centerY = bounds.getMinY() + bounds.getHeight() / 2;

    image.getTransforms().clear();
    image.getTransforms().addAll(
        new Translate(
            transform.position.getX() - centerX,
            transform.position.getY() - centerY
        ),
        new Rotate(
            transform.rotation,
            centerX,
            centerY
        ),
        new Scale(
            transform.scale.getX(),
            transform.scale.getY(),
            centerX,
            centerY
        )
    );
    width = image.getBoundsInParent().getWidth();
    height = image.getBoundsInParent().getHeight();
  }

  /* API BELOW */

  public void setImage(Node image) {
    this.image = image;
    this.rawWidth = image.getBoundsInParent().getWidth();
    this.rawHeight = image.getBoundsInParent().getHeight();
  }

  public Node getImage() {
    return image;
  }
}
