/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.listview;

import de.lb.cpx.wm.model.TWmEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.scene.transform.Translate;

/**
 * skin class to display history entries
 *
 * @author wilde
 * @param <T> event type
 */
public class HistoryListItemSkin<T extends TWmEvent> extends SkinBase<HistoryListItem<T>> {

    private static final Logger LOG = Logger.getLogger(HistoryListItemSkin.class.getName());
    private static final String PSEUDO_FADE_OUT_CLASS = "fadeOut";
    private Rectangle recUser;
    private Path containerPath;
    private HBox recIconContainer;
    private HBox headerContainer;
    private AnchorPane vLineContainer;
    private VBox contentContainer;
    private Path pathIconBottom;
    private Path pathIconTop;
    private Label lblUserName;
    private Label lblCreationDate;
    private Label lblContentTitle;
    private Label lblContentDetails;
    private HBox menuButtonContainer;
    private Pane paneFadeOut;
    private AnchorPane root;

    /**
     * default constructor
     *
     * @param pSkinnable skinnable control
     * @throws java.io.IOException if fxml can not be found
     */
    public HistoryListItemSkin(HistoryListItem<T> pSkinnable) throws IOException {
        super(pSkinnable);
        getChildren().add(getRoot());
        pSkinnable.iconFillProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setUserColor(newValue);
            }
        });
        pSkinnable.iconSizeProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue == null) {
                    LOG.warning("Can not set UserIconSize, new size is invalid!");
                    return;
                }
                setUserIconSize(newValue.intValue());
            }
        });
        pSkinnable.displayModeProperty().addListener(new ChangeListener<HistoryListItem.DisplayMode>() {
            @Override
            public void changed(ObservableValue<? extends HistoryListItem.DisplayMode> observable, HistoryListItem.DisplayMode oldValue, HistoryListItem.DisplayMode newValue) {
                setDisplayMode(newValue);
            }
        });
        pSkinnable.menuButtons().addListener(new ListChangeListener<Button>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Button> c) {
                //do not care, clear and set new if changes occure
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
                menuButtonContainer.getChildren().clear();
                if (!pSkinnable.isReadOnly()) {
                    menuButtonContainer.getChildren().addAll(c.getList());
                }
//                    }
//                });
            }
        });
        pSkinnable.readOnlyProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleReadOnly(newValue);
            }
        });
        handleReadOnly(pSkinnable.isReadOnly());
        pSkinnable.descriptionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleDescripton(newValue);
            }
        });
        handleDescripton(getSkinnable().getDescription());

        pSkinnable.compactProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                handleCompact(newValue);
            }
        });
        handleCompact(pSkinnable.isCompact());
    }

    private void handleDescripton(String newValue) {
        if (newValue == null || newValue.isEmpty()) {
            showDescription(false);
        } else {
            showDescription(true);
        }
    }

    private void handleReadOnly(boolean newValue) {
        if (newValue) {
            menuButtonContainer.getChildren().clear();
        } else {
            if (getSkinnable().menuButtons().isEmpty()) {
                return;
            }
            menuButtonContainer.getChildren().clear();
            menuButtonContainer.getChildren().addAll(getSkinnable().menuButtons());
        }
    }

    private Parent getRoot() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/HistoryEntry.fxml"));
        vLineContainer = (AnchorPane) root.lookup("#vLineContainer");
        AnchorPane vLineIconTop = (AnchorPane) root.lookup("#vLineIconTop");
        AnchorPane vLineIconBottom = (AnchorPane) root.lookup("#vLineIconBottom");
        recIconContainer = (HBox) root.lookup("#recIconContainer");
        headerContainer = (HBox) root.lookup("#headerContainer");
        contentContainer = (VBox) root.lookup("#contentContainer");
        menuButtonContainer = (HBox) root.lookup("#menuButtonContainer");
        paneFadeOut = (Pane) root.lookup("#paneOverlay");
        root.getChildren().remove(paneFadeOut);

        lblUserName = (Label) root.lookup("#lblUserName");
        lblUserName.textProperty().bind(getSkinnable().userNameProperty());
        lblCreationDate = (Label) root.lookup("#lblCreationDate");
        lblCreationDate.textProperty().bind(getSkinnable().creationDateProperty());

        lblContentTitle = (Label) root.lookup("#lblContentTitle");
        lblContentTitle.setStyle("-fx-font-size: 14px;");
        lblContentTitle.setWrapText(true);
        lblContentTitle.getStyleClass().add("cpx-history-title-label");
        lblContentTitle.textProperty().bind(getSkinnable().titleProperty());

        lblContentTitle.minWidthProperty().bind(getSkinnable().prefWidthProperty());
        lblContentTitle.prefWidthProperty().bind(getSkinnable().prefWidthProperty());
        lblContentTitle.maxWidthProperty().bind(getSkinnable().prefWidthProperty());

        lblContentDetails = (Label) root.lookup("#lblContentDetails");
        lblContentDetails.setWrapText(true);
        lblContentDetails.textProperty().bind(getSkinnable().descriptionProperty());

        lblContentDetails.minWidthProperty().bind(getSkinnable().prefWidthProperty());
        lblContentDetails.prefWidthProperty().bind(getSkinnable().prefWidthProperty());
        lblContentDetails.maxWidthProperty().bind(getSkinnable().prefWidthProperty());

        recUser = new Rectangle();
        setUserIconSize(getSkinnable().getIconSize());
        setUserColor(getSkinnable().getIconFill());

        recUser.getStyleClass().add("cpx-history-node");
        containerPath = addPathToAnchorPane(vLineContainer, 0.5);
        contentContainer.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        vLineContainer.setPrefHeight(newValue.doubleValue());
                    }
                });
            }
        });
        headerContainer.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        vLineIconTop.setMinHeight(newValue.doubleValue()/2);
                        vLineIconTop.setPrefHeight(newValue.doubleValue()/2);
                        vLineIconTop.setMaxHeight(newValue.doubleValue()/2);
                        vLineIconBottom.setMinHeight(newValue.doubleValue()/2);
                        vLineIconBottom.setPrefHeight(newValue.doubleValue()/2);
                        vLineIconBottom.setMaxHeight(newValue.doubleValue()/2);
                    }
                });
            }
        });
        vLineIconTop.setPrefHeight(headerContainer.getHeight()/2);
        vLineIconBottom.setPrefHeight(headerContainer.getHeight()/2);
        
        pathIconTop = addPathToAnchorPane(vLineIconTop, 0.5);
        pathIconTop.setSmooth(false);

        pathIconBottom = addPathToAnchorPane(vLineIconBottom, 0.5);
        recIconContainer.getChildren().add(recUser);

        setDisplayMode(getSkinnable().getDisplayMode());

        return root;
    }

    private void showDescription(boolean pShow) {
        if (contentContainer == null) {
            return;
        }
        if (pShow) {
            if (!contentContainer.getChildren().contains(lblContentDetails)) {
                contentContainer.getChildren().add(lblContentDetails);
            }
        } else {
            if (contentContainer.getChildren().contains(lblContentDetails)) {
                contentContainer.getChildren().remove(lblContentDetails);
            }
        }
    }

    private void setUserColor(String pColor) {
        if (pColor == null || pColor.isEmpty()) {
            LOG.warning("can not set user color, color is invalid!");
            return;
        }
        if (recUser == null) {
            LOG.log(Level.WARNING, "can not set user color: {0} UserNode is not yet initalized!", pColor);
            return;
        }
        recUser.setStyle("-fx-fill: " + pColor + ";");
    }

    private Path addPathToAnchorPane(AnchorPane pPane, double pOffset) {
        final Path path = new Path();

        // A square handling in a path.
        MoveTo mt = new MoveTo(4.5f, 0.0f); // X-axis parameter starts from half of the square width
        path.getElements().add(mt);
        path.getElements().add(new VLineTo(0.0f));
        path.getStyleClass().add("cpx-history-line");
        path.getElements().add(mt); // path is based on the listitem element.

        AnchorPane.setLeftAnchor(path, (1.5 + (getSkinnable().getIconSize() / 2)));
        mt.yProperty().bind(pPane.heightProperty().subtract(1));   // vertical property (height) of path is binded to the listitem height
        pPane.getChildren().add(0, path);
        Translate translateSquare = new Translate();
        translateSquare.setX(0);
        translateSquare.setY(pOffset);
        path.getTransforms().addAll(translateSquare);
        path.getElements().add(new ClosePath());
        return path;
    }

    private void setUserIconSize(Integer userIconSize) {
        if (recUser == null) {
            LOG.severe("Can not set user node size, UserNode is not yet initialized!");
            return;
        }
        if (userIconSize == null) {
            LOG.warning("Can not set user node size, Size is invalid!");
            return;
        }
        recUser.setWidth(userIconSize);
        recUser.setHeight(userIconSize);
    }

    private void setDisplayMode(HistoryListItem.DisplayMode pDisplayMode) {
        pDisplayMode = Objects.requireNonNullElse(pDisplayMode, HistoryListItem.DisplayMode.END);

        switch (pDisplayMode) {
            case SINGLE:
                setShowPaths(false);
                fadeOut(false);
                break;
            case END:
                setShowPathIconTop(false);
                setShowPathIconBottom(true);
                setShowPathContainer(true);
                fadeOut(false);
                break;
            case BEFORE_END:
            //intended fall through did not care much about this item
            case ITEM:
                setShowPaths(true);
                fadeOut(false);
                break;
            case AFTER_START:
                setShowPaths(true);
                fadeOut(false);
                break;
            case START_FATE_OUT:
                setShowPaths(true);
                fadeOut(true);
                break;
            case START:
                setShowPathIconTop(true);
                setShowPathIconBottom(false);
                setShowPathContainer(false);
                fadeOut(false);
                break;
            default:
                LOG.log(Level.FINEST, "Do nothing for displaymode: {0}", pDisplayMode.name());
        }
    }

    private void fadeOut(boolean pFade) {
        setShowOverlay(pFade);
        getSkinnable().pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_FADE_OUT_CLASS), pFade);
    }

    private void setShowOverlay(boolean pShow) {
        if (pShow) {
            if (!root.getChildren().contains(paneFadeOut)) {
                root.getChildren().add(paneFadeOut);
            }
        } else {
            if (root.getChildren().contains(paneFadeOut)) {
                root.getChildren().remove(paneFadeOut);
            }
        }
    }

    private void setShowPaths(boolean pShow) {
        setShowPathContainer(pShow);
        setShowPathIconTop(pShow);
        setShowPathIconBottom(pShow);
    }

    private void setShowPathContainer(boolean pShow) {
        if (containerPath != null) {
            containerPath.setVisible(pShow);
        }
    }

    private void setShowPathIconTop(boolean pShow) {
        if (pathIconTop != null) {
            pathIconTop.setVisible(pShow);
        }
    }

    private void setShowPathIconBottom(boolean pShow) {
        if (pathIconBottom != null) {
            pathIconBottom.setVisible(pShow);
        }
    }

    private void handleCompact(boolean pCompact) {
        if (pCompact) {
            //No limitation! Don't show excerpt from details text but whole text!
            lblContentDetails.setMaxHeight(Double.MAX_VALUE);
        } else {
            lblContentDetails.setMaxHeight(65);
        }
    }

}
