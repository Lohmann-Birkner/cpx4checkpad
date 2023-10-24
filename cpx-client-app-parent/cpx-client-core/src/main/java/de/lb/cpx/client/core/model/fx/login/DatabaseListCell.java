/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.login;

import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.service.information.CpxDatabase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author wilde
 */
public class DatabaseListCell extends ListCell<CpxDatabase> {

    private final Image imgOracle = ResourceLoader.getImage("/img/oracle-16x16.png");
    private final Image imgSqlSrv = ResourceLoader.getImage("/img/mssql-16x16.png");
    private final Image imgUnknown = ResourceLoader.getImage("/img/unknown-db-16x16.png");

    private final Label name = new Label();
    private final Label icon = new Label();
    private final HBox cell;

    public DatabaseListCell() {
        super();
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        cell = new HBox();
        HBox.setMargin(name, new Insets(0, 1, 0, 5));

        //HERE, ADD YOUR PRE-MADE HBOX CODE
        name.setAlignment(Pos.CENTER_LEFT);
        icon.setAlignment(Pos.CENTER_LEFT);
        icon.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(name, Priority.ALWAYS);
        cell.getChildren().add(icon);
        cell.getChildren().add(name);
//    {
//        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//        cell = new HBox();
//        HBox.setMargin(name, new Insets(0, 1, 0, 5));
//
//        //HERE, ADD YOUR PRE-MADE HBOX CODE
//        name.setAlignment(Pos.CENTER_LEFT);
//        icon.setAlignment(Pos.CENTER_LEFT);
//        icon.setMaxWidth(Double.MAX_VALUE);
//        HBox.setHgrow(name, Priority.ALWAYS);
//        cell.getChildren().add(icon);
//        cell.getChildren().add(name);

    }

    @Override
    protected void updateItem(CpxDatabase item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            name.setText(item.toString());
            if (item.unknown) {
                //mark invalid database on gui
                cell.setStyle("-fx-background-color: #ffd9d9;");
                Tooltip.install(cell, new Tooltip("Database does not seem to exist!"));
            }
            ImageView img = getDbImage(item);
            //if (img != null) {
            icon.setGraphic(img);
            //}
            setGraphic(cell);
            //HERE IS WHERE YOU GET THE LABEL AND NAME
        }
    }

    private ImageView getDbImage(CpxDatabase cpxDatabase) {
        if (cpxDatabase.isOracle()) {
            return new ImageView(imgOracle);
        } else if (cpxDatabase.isSqlSrv()) {
            return new ImageView(imgSqlSrv);
        } else if (!cpxDatabase.getPersistenceUnit().isEmpty()) {
            return new ImageView(imgUnknown);
        }
        return null;
    }
}
