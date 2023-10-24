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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author wilde
 * @param <T> event type
 */
public class HistoryListItem<T extends TWmEvent> extends Control {

    private static final Logger LOG = Logger.getLogger(HistoryListItem.class.getName());
    private static final boolean USER_COLOR_BY_NAME = false;
    private static final String DEFAULT_STYLE_CLASS = "history-list-item";
    private static final String DEFAULT_ICON_FILL = "-root03";
    private static final String SAME_USER_ICON_FILL = "-white01";
    private static final Integer DEFAULT_ICON_SIZE = 13;

    public HistoryListItem(T pEvent) {
        this();
        setEventEntity(pEvent);
    }

    public HistoryListItem() {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        eventEntityProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                if (newValue == null) {
                    LOG.severe("Can not display event when it is null!");
                    return;
                }
//                //maybe it should be moved to properties but can be a lot of load when first search is executed
//                setTitle(createTitle(newValue));
//                setDescription(createDescription(newValue));
                setIconFill(getUserColor()/*isSameUser(newValue)?SAME_USER_ICON_FILL:DEFAULT_ICON_FILL*/);
                setUser(newValue.getCreationUser());
                setCreationDate(newValue.getCreationDate());
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new HistoryListItemSkin<>(this);
        } catch (IOException ex) {
            Logger.getLogger(HistoryListItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    private ObjectProperty<T> eventEntityProperty;

    public final ObjectProperty<T> eventEntityProperty() {
        if (eventEntityProperty == null) {
            eventEntityProperty = new SimpleObjectProperty<>();
        }
        return eventEntityProperty;
    }

    public final void setEventEntity(T pEntity) {
        eventEntityProperty().set(pEntity);
    }

    public T getEventEntity() {
        return eventEntityProperty().get();
    }

    private StringProperty titleProperty;

    public StringProperty titleProperty() {
        if (titleProperty == null) {
            titleProperty = new SimpleStringProperty();
        }
        return titleProperty;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    protected final void setTitle(String pTitle) {
        titleProperty().set(pTitle);
    }

    private StringProperty descriptionProperty;

    public StringProperty descriptionProperty() {
        if (descriptionProperty == null) {
            descriptionProperty = new SimpleStringProperty();
        }
        return descriptionProperty;
    }

    public String getDescription() {
        return descriptionProperty().get();
    }

    protected final void setDescription(String pDescription) {
        descriptionProperty().set(pDescription);
    }

    private StringProperty iconFillProperty;

    public StringProperty iconFillProperty() {
        if (iconFillProperty == null) {
            iconFillProperty = new SimpleStringProperty(DEFAULT_ICON_FILL);
        }
        return iconFillProperty;
    }

    public void setIconFill(String pFill) {
        iconFillProperty().set(pFill);
    }

    public String getIconFill() {
        return iconFillProperty().get();
    }
    private ObjectProperty<DisplayMode> displayModeProperty;

    public ObjectProperty<DisplayMode> displayModeProperty() {
        if (displayModeProperty == null) {
            displayModeProperty = new SimpleObjectProperty<>(DisplayMode.ITEM);
        }
        return displayModeProperty;
    }

    public void setDisplayMode(DisplayMode pMode) {
        displayModeProperty().set(pMode);
    }

    public DisplayMode getDisplayMode() {
        return displayModeProperty().get();
    }

    private IntegerProperty iconSizeProperty;

    public IntegerProperty iconSizeProperty() {
        if (iconSizeProperty == null) {
            iconSizeProperty = new SimpleIntegerProperty(DEFAULT_ICON_SIZE);
        }
        return iconSizeProperty;
    }

    public Integer getIconSize() {
        return iconSizeProperty().get();
    }

    public void setIconSize(int pSize) {
        iconSizeProperty().set(pSize);
    }

    public boolean isSameUser(T newValue) {
        if (newValue == null) {
            return false;
        }
        return isSameUser(newValue.getCreationUser());
    }

    public boolean isSameUser(Long pUserId) {
        if (pUserId == null) {
            return false;
        }
        return Session.instance().getCpxUserId() == pUserId;
    }

    public String getUserColor() {
        return USER_COLOR_BY_NAME ? getUserColorByName() : getDefaultUserColor();
    }

    public String getUserColorByName() {
        if (isSameUser(getEventEntity())) {
            return MenuCache.instance().getUserMapEntryForId(Session.instance().getCpxUserId()).getHexColor();
        } else {
            UserDTO user = MenuCache.instance().getUserMapEntryForId(getEventEntity().getCreationUser());
            return user != null ? user.getHexColor() : DEFAULT_ICON_FILL;
        }
    }

    public String getDefaultUserColor() {
        if (isSameUser(getEventEntity())) {
            return SAME_USER_ICON_FILL;
        } else {
            return DEFAULT_ICON_FILL;
        }
    }
    private StringProperty userNameProperty;

    public StringProperty userNameProperty() {
        if (userNameProperty == null) {
            userNameProperty = new SimpleStringProperty(getEventEntity() != null ? createUserName(getEventEntity()) : null);
        }
        return userNameProperty;
    }

    public void setUser(long pId) {
        setUserName(createUserName(pId));
    }

    public void setUserName(String pUserName) {
        userNameProperty().set(pUserName);
    }

    public String getUserName() {
        return userNameProperty().get();
    }

    private StringProperty creationDateProperty;

    public StringProperty creationDateProperty() {
        if (creationDateProperty == null) {
            creationDateProperty = new SimpleStringProperty(getEventEntity() != null ? Lang.toDate(getEventEntity().getCreationDate()) : null);
        }
        return creationDateProperty;
    }

    public void setCreationDate(Date pDate) {
        setCreationDate(Lang.toDate(pDate));
    }

    public void setCreationDate(String pUserName) {
        creationDateProperty().set(pUserName);
    }

    public String getCreationDate() {
        return creationDateProperty().get();
    }

    private ObservableList<Button> menuButtons;

    protected final ObservableList<Button> menuButtons() {
        if (menuButtons == null) {
            menuButtons = FXCollections.observableArrayList();
        }
        return menuButtons;
    }

    public void addMenuButton(int pIndex, Button pButton) {
        menuButtons().add(pIndex > 0 ? pIndex : 0, pButton);
    }

    public void addMenuButton(Button pButton) {
        addMenuButton(0, pButton);
    }

    public void removeMenuButton(Button pButton) {
        menuButtons().remove(pButton);
    }

    public void addButtons(List<Button> pButtons) {
        menuButtons().addAll(pButtons);
    }

    public void setButtons(List<Button> pButtons) {
        menuButtons().setAll(pButtons);
    }

    private String createUserName(T pEvent) {
        if (pEvent == null) {
            return null;
        }
        return createUserName(pEvent.getCreationUser());
    }

    private String createUserName(long pUserId) {
        String userName = MenuCache.instance().getUserFullNameForId(pUserId);
//        if (isSameUser(pUserId)) {
//            userName = userName.concat(" (Ich)");
//        }
        return userName;
    }

    private BooleanProperty readOnlyProperty;

    public BooleanProperty readOnlyProperty() {
        if (readOnlyProperty == null) {
            readOnlyProperty = new SimpleBooleanProperty(false);
        }
        return readOnlyProperty;
    }

    public void setReadOnly(boolean pReadOnly) {
        readOnlyProperty().set(pReadOnly);
    }

    public boolean isReadOnly() {
        return readOnlyProperty().get();
    }

    private BooleanProperty compactProperty;

    public BooleanProperty compactProperty() {
        if (compactProperty == null) {
            compactProperty = new SimpleBooleanProperty(Session.instance().isShowHistoryEventDetails());
        }
        return compactProperty;
    }

    public void setCompact(boolean pCompact) {
        compactProperty().set(pCompact);
    }

    public boolean isCompact() {
        return compactProperty().get();
    }

    protected enum DisplayMode {
        SINGLE, START_FATE_OUT, START, AFTER_START, ITEM, BEFORE_END, END;
    }
}
