/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.image;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author adameck
 */
public class CropImageFXMLController extends Controller<CpxScene> {

    private final DoubleProperty imageHeight = new SimpleDoubleProperty(0.0d);
    private final DoubleProperty imageWidth = new SimpleDoubleProperty(0.0d);
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label labelImagePath;
    @FXML
    private Label labelSetSize;
    @FXML
    private Slider selectionSizeSlider;
    @FXML
    private HBox hBoxImageSelection;
    @FXML
    private Label labelSelection;

    private RubberBandSelection rubberBandSelection;
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageHeight.set(0.0d);
        imageWidth.set(0.0d);
        setupLabels();
        Group imageLayer = new Group();

        Image image;
//        image = ResourceLoader.getImage("/img/dummy_female_grey.jpg");
        image = ResourceLoader.getImage("/img/checkpoint_x_logo_480x480.png");

        imageView = new ImageView(image);
        imageLayer.getChildren().add(imageView);
        scrollPane.setContent(imageLayer);
        rubberBandSelection = new RubberBandSelection(imageLayer, selectionSizeSlider, imageWidth, imageHeight);

        setupSlider(image);
        setupSelectionImages();
    }

    private void setupLabels() {
        labelImagePath.setText("");
        labelSelection.setText("Aus Vorlage wählen:");
        labelSetSize.setText("Größe festlegen");
    }

    /*
    public Image getCroppedImage() {
        Bounds bounds = rubberBandSelection.getBounds();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));
        
        WritableImage wi = new WritableImage(width, height);
        imageView.snapshot(parameters, wi);
        
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
        
        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);
        
        graphics.dispose();
        
        return SwingFXUtils.toFXImage(bufImageRGB, wi);
    }
     */
    public Byte[] getCroppedImageData() throws IOException {
        Bounds bounds = rubberBandSelection.getBounds();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        imageView.snapshot(parameters, wi);

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        //BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //ImageIO.write(bufImageARGB, "png", new File("C:\\TEMP\\test.png"));
        ImageIO.write(bufImageARGB, "png", outputStream);
        byte[] data = outputStream.toByteArray();
        Byte[] data2 = ArrayUtils.toObject(data);
        return data2;
    }

    @FXML
    private void onOpenFile(ActionEvent pEvent) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Bilddatei auswählen");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Alle Bilddateien", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fileChooser.showOpenDialog(scrollPane.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toURL().toExternalForm());

            if (image.getWidth() < 240 || image.getHeight() < 180) {
                BasicMainApp.showErrorMessageDialog("Das Profilbild muss mindestens 240px breit und 180px hoch sein.");
            } else {
                //If photo is too big then resize 
                if (image.getHeight() * image.getWidth() >= 420000) {
                    imageView.setImage(image);

                    imageView.setFitHeight(600);
                    imageView.setFitWidth(600);
                    imageView.setPreserveRatio(true);
                    setupSliderBigImage(image);
                    labelImagePath.setText(file.getAbsolutePath());

                } else {
                    imageHeight.set(image.getHeight());
                    imageWidth.set(image.getWidth());

                    imageView.setImage(image);
                    imageView.setFitHeight(image.getHeight());
                    imageView.setFitWidth(image.getWidth());
                    imageView.setPreserveRatio(false);

                    setupSlider(image);
                    labelImagePath.setText(file.getAbsolutePath());
                }
            }
        }
    }

    private void setupSliderBigImage(Image image) {

        double ratio = image.getHeight() / image.getWidth();

        final double defaultRectWidth = 240.0;
//        final double defaultRectHeight = 240.0;
        final double defaultRectHeight = 180.0;

        final double defaultRatio = (defaultRectHeight / defaultRectWidth);

        if (ratio < defaultRatio) {
            selectionSizeSlider.setMajorTickUnit((int) (image.getHeight()));
            selectionSizeSlider.setMinorTickCount((int) (image.getHeight() * 0.1));
            selectionSizeSlider.setMax(imageView.getFitHeight() * ratio);
            selectionSizeSlider.setMin(180);
        } else {
            selectionSizeSlider.setMajorTickUnit((int) (image.getWidth()));
            selectionSizeSlider.setMinorTickCount((int) (image.getWidth() * 0.1));

            selectionSizeSlider.setMax(imageView.getFitHeight() / ratio);
            selectionSizeSlider.setMin(240);
        }

        rubberBandSelection.resetRect();
    }

    private void setupSlider(Image image) {
        double ratio = image.getHeight() / image.getWidth();

        final double defaultRectWidth = 240.0;
//        final double defaultRectHeight = 240.0;
        final double defaultRectHeight = 180.0;

        final double defaultRatio = (defaultRectHeight / defaultRectWidth);

        if (ratio < defaultRatio) {
            selectionSizeSlider.setMajorTickUnit((int) (image.getHeight()));
            selectionSizeSlider.setMinorTickCount((int) (image.getHeight() * 0.1));
            selectionSizeSlider.setMax(image.getHeight());
            selectionSizeSlider.setMin(180);
        } else {
            selectionSizeSlider.setMajorTickUnit((int) (image.getWidth()));
            selectionSizeSlider.setMinorTickCount((int) (image.getWidth() * 0.1));
            selectionSizeSlider.setMax(image.getWidth());
            selectionSizeSlider.setMin(240);
        }

        rubberBandSelection.resetRect();
    }

    private void setupSelectionImages() {
//        Image imageFemale;
//        imageFemale = ResourceLoader.getImage("/img/dummy_female_grey.jpg");
//
//        Image imageMale;
//        imageMale = ResourceLoader.getImage("/img/dummy_male_grey.jpg");

        Image avatar01 = ResourceLoader.getImage("/img/avatar_01.jpg");
        Image avatar02 = ResourceLoader.getImage("/img/avatar_02.jpg");
        Image avatar03 = ResourceLoader.getImage("/img/avatar_03.jpg");
        Image avatar04 = ResourceLoader.getImage("/img/avatar_04.jpg");
        Image avatar05 = ResourceLoader.getImage("/img/avatar_05.jpg");
        Image avatar06 = ResourceLoader.getImage("/img/avatar_06.jpg");
        Image avatar07 = ResourceLoader.getImage("/img/avatar_07.jpg");
        Image avatar08 = ResourceLoader.getImage("/img/avatar_08.jpg");
        Image avatar09 = ResourceLoader.getImage("/img/avatar_09.jpg");
        Image avatar10 = ResourceLoader.getImage("/img/avatar_10.jpg");
        Image avatar11 = ResourceLoader.getImage("/img/avatar_11.jpg");
        Image avatar12 = ResourceLoader.getImage("/img/avatar_12.jpg");
        Image avatar13 = ResourceLoader.getImage("/img/avatar_13.jpg");
        Image avatar14 = ResourceLoader.getImage("/img/avatar_14.jpg");
        Image avatar15 = ResourceLoader.getImage("/img/avatar_15.jpg");
        Image avatar16 = ResourceLoader.getImage("/img/avatar_16.jpg");
        Image avatar17 = ResourceLoader.getImage("/img/avatar_17.jpg");
        Image avatar18 = ResourceLoader.getImage("/img/avatar_18.jpg");
        Image avatar19 = ResourceLoader.getImage("/img/avatar_19.jpg");
        Image avatar20 = ResourceLoader.getImage("/img/avatar_20.jpg");
        Image avatar21 = ResourceLoader.getImage("/img/avatar_21.jpg");
        Image avatar22 = ResourceLoader.getImage("/img/avatar_22.jpg");
        Image avatar23 = ResourceLoader.getImage("/img/avatar_23.jpg");
        Image avatar24 = ResourceLoader.getImage("/img/avatar_24.jpg");
        Image avatar25 = ResourceLoader.getImage("/img/avatar_25.jpg");
        Image cpxLogo = ResourceLoader.getImage("/img/checkpoint_x_logo_480x480.png");

        hBoxImageSelection.getChildren().addAll(
                //                selectionImageView(imageFemale),
                //                selectionImageView(imageMale),
                selectionImageView(cpxLogo),
                selectionImageView(avatar01),
                selectionImageView(avatar02),
                selectionImageView(avatar03),
                selectionImageView(avatar04),
                selectionImageView(avatar05),
                selectionImageView(avatar06),
                selectionImageView(avatar07),
                selectionImageView(avatar08),
                selectionImageView(avatar09),
                selectionImageView(avatar10),
                selectionImageView(avatar11),
                selectionImageView(avatar12),
                selectionImageView(avatar13),
                selectionImageView(avatar14),
                selectionImageView(avatar15),
                selectionImageView(avatar16),
                selectionImageView(avatar17),
                selectionImageView(avatar18),
                selectionImageView(avatar19),
                selectionImageView(avatar20),
                selectionImageView(avatar21),
                selectionImageView(avatar22),
                selectionImageView(avatar23),
                selectionImageView(avatar24),
                selectionImageView(avatar25)
        );
    }

    private ImageView selectionImageView(Image image) {
        //double imageRatio = image.getHeight() / image.getWidth();
        ImageView newImageView = new ImageView(image);
        newImageView.setCursor(Cursor.HAND);
        newImageView.setFitHeight(100);
        newImageView.setPreserveRatio(true);
//        newImageView.setFitWidth(100*imageRatio);

//        newImageView.fitHeightProperty().bind(hBoxImageSelection.heightProperty());
//        newImageView.fitWidthProperty().bind(hBoxImageSelection.widthProperty().multiply(imageRatio));
        newImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                imageView.setImage(image);
                setupSlider(image);
                labelImagePath.setText("");
            }
        });

        return newImageView;
    }

    public static class RubberBandSelection {

        private final DragContext dragContext = new DragContext();
        private Rectangle rect = new Rectangle();
        //        final double defaultRectWidth = 240.0;
//        final double defaultRectHeight = 240.0; //Pna: to set square image
        private final double defaultRectWidth = 480.0;
        private final double defaultRectHeight = 480.0;
//        final double defaultRectHeight = 180.0;
        private final double defaultRatio = (defaultRectHeight / defaultRectWidth);
        private Group group;
        private final Slider slider;
        private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                if (event.getX() > (rect.getWidth() / 2)) {
                    if ((event.getX() + (rect.getWidth() / 2)) > group.getBoundsInLocal().getWidth()) {
                        rect.setX(group.getBoundsInLocal().getWidth() - rect.getWidth());
                    } else {
                        rect.setX(dragContext.mouseAnchorX - (rect.getWidth() / 2));
                    }
                } else {
                    rect.setX(0);
                }

                if (event.getY() > (rect.getHeight() / 2)) {
                    if ((event.getY() + (rect.getHeight() / 2)) > group.getBoundsInLocal().getHeight()) {
                        rect.setY(group.getBoundsInLocal().getHeight() - rect.getHeight());
                    } else {
                        rect.setY(dragContext.mouseAnchorY - (rect.getHeight() / 2));
                    }
                } else {
                    rect.setY(0);
                }
            }
        };
        private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                if (event.getX() > (rect.getWidth() / 2)) {
                    if ((event.getX() + (rect.getWidth() / 2)) > group.getBoundsInLocal().getWidth()) {
                        rect.setX(group.getBoundsInLocal().getWidth() - rect.getWidth());
                    } else {
                        rect.setX(event.getX() - (rect.getWidth() / 2));
                    }
                } else {
                    rect.setX(0);
                }

                if (event.getY() > (rect.getHeight() / 2)) {
                    if ((event.getY() + (rect.getHeight() / 2)) > group.getBoundsInLocal().getHeight()) {
                        rect.setY(group.getBoundsInLocal().getHeight() - rect.getHeight());
                    } else {
                        rect.setY(event.getY() - (rect.getHeight() / 2));
                    }
                } else {
                    rect.setY(0);
                }
            }
        };
        private final EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }
            }
        };
        private final DoubleProperty imageWidth;
        private final DoubleProperty imageHeight;

        public RubberBandSelection(Group group, Slider slider, DoubleProperty imageWidth, DoubleProperty imageHeight) {
            this.group = group;
            this.slider = slider;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;

            rect = new Rectangle(0, 0, defaultRectWidth, defaultRectHeight);
//            rect.setStroke(Color.GREEN);
//            rect.setStrokeWidth(1);
//            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTGREEN.deriveColor(0, 1.2, 1, 0.4));
            rect.setCursor(Cursor.MOVE);

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

                    if (new_val.doubleValue() > old_val.doubleValue()) {
                        if (rect.getWidth() + rect.getX() > group.getBoundsInLocal().getWidth() - 1) {
                            rect.setX(rect.getX() - (new_val.doubleValue() - old_val.doubleValue()));
                        }
                        if (rect.getHeight() + rect.getY() > group.getBoundsInLocal().getHeight() - 1) {
                            rect.setY(rect.getY() - (new_val.doubleValue() - old_val.doubleValue()));
                        }
                    }

                    double imageRatio = (group.getBoundsInLocal().getHeight() / group.getBoundsInLocal().getWidth());

                    if (imageRatio < defaultRatio) {
                        rect.setWidth(new_val.doubleValue() / defaultRatio);
                        rect.setHeight((new_val.doubleValue()));
                    } else {
                        rect.setWidth(new_val.doubleValue());
                        rect.setHeight((new_val.doubleValue()) * defaultRatio);
                    }
                }
            });

            this.group.getChildren().add(rect);
        }

        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        private void resetRect() {
            double boxHeight = Double.doubleToRawLongBits(imageHeight.get()) != Double.doubleToRawLongBits(0.0d) ? imageHeight.get() : 480;
            double boxWidth = Double.doubleToRawLongBits(imageWidth.get()) != Double.doubleToRawLongBits(0.0d) ? imageWidth.get() : 480;

            double boxSize = Math.min(boxHeight, boxWidth);

            rect.setX(0);
            rect.setY(0);
            rect.setWidth(boxSize);
            rect.setHeight(boxSize);

            this.slider.setValue(this.slider.getMin());
        }

        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;
        }
    }
}
