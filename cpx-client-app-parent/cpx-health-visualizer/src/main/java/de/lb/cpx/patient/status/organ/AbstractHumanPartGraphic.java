/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.patient.status.organ;

import de.lb.cpx.patient.status.organ.common.Body16;
import de.lb.cpx.patient.status.organ.common.Breathing1;
import de.lb.cpx.patient.status.organ.common.Cardiovascula9;
import de.lb.cpx.patient.status.organ.common.Endocrine3;
import de.lb.cpx.patient.status.organ.common.Ent10;
import de.lb.cpx.patient.status.organ.common.Eyes2;
import de.lb.cpx.patient.status.organ.common.Gastrointestinal4;
import de.lb.cpx.patient.status.organ.common.Hematology7;
import de.lb.cpx.patient.status.organ.common.Kidney12;
import de.lb.cpx.patient.status.organ.common.Musculoskeletal11;
import de.lb.cpx.patient.status.organ.common.Neurology15;
import de.lb.cpx.patient.status.organ.common.Psychosocial13;
import de.lb.cpx.patient.status.organ.common.Skin8;
import de.lb.cpx.patient.status.organ.common.Teeth14;
import de.lb.cpx.patient.status.organ.common.UrinaryOrgans5;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.dto.acg.IcdFarbeOrgan;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Abstract implementation of a human part (organ or body)
 *
 * @author niemeier
 */
public abstract class AbstractHumanPartGraphic implements HumanPartGraphic {

    private static final Logger LOG = Logger.getLogger(AbstractHumanPartGraphic.class.getName());

    public static final Effect GRAY = getMonochrome();

    public static final Lighting MINOR_EFFECT = getLighting(OrganSeverity.Minor.getColor(), 1.0);
    public static final Lighting MODERATE_EFFECT = getLighting(OrganSeverity.Moderate.getColor(), 1.0);
    public static final Effect SEVERE_EFFECT = getLighting(OrganSeverity.Severe.getColor(), 1.0);
    public static final Effect SEVERST_EFFECT = getLighting(OrganSeverity.Severst.getColor(), 1.0);

    public static final Lighting MINOR_HIGHLIGHTED_EFFECT = getLighting(OrganSeverity.Minor.getColor(), 0.7);
    public static final Lighting MODERATE_HIGHLIGHTED_EFFECT = getLighting(OrganSeverity.Moderate.getColor(), 0.7);
    public static final Effect SEVERE_HIGHLIGHTED_EFFECT = getLighting(OrganSeverity.Severe.getColor(), 0.7);
    public static final Effect SEVERST_HIGHLIGHTED_EFFECT = getLighting(OrganSeverity.Severst.getColor(), 0.7);

    public final ImageView imageView;
    public final BodyGraphic body;
    public final String id;
    public final String graphicFileName;
    public final String nameEnglish;
    public final String nameGerman;
    public final int number;
    public final double size;
    public final double positionX;
    public final double positionY;

    /**
     * Instance represents a part of human body
     *
     * @param pBody Related body graphic (e.g. male or female)
     * @param pId Unique identifier (should not be changed)
     * @param pNameEnglish Name in English
     * @param pNameGerman Name in German
     * @param pGraphicFileName Filename for graphical representation (SVG)
     * @param pNumber Unique number for this part of a human organ
     * @param pSize Size relative to the pane (for bodys) or body (for organs)
     * @param pPositionX Horizontal position relative to the middle (0, 0)
     * @param pPositionY Vertical position relative to the middle (0, 0)
     */
    public AbstractHumanPartGraphic(final BodyGraphic pBody, final String pId,
            final String pNameEnglish, final String pNameGerman, final String pGraphicFileName, final int pNumber,
            final double pSize, final double pPositionX, final double pPositionY) {
        body = pBody;
        id = pId;
        nameEnglish = pNameEnglish;
        nameGerman = pNameGerman;
        graphicFileName = pGraphicFileName;
        number = pNumber;
        size = pSize;
        positionX = pPositionX;
        positionY = pPositionY;
        imageView = new ImageView();
    }

    public static HumanPartGraphic getOrgan(final Integer pOrganNumber, final char pGender) {
        final int organNumber = pOrganNumber == null ? 0 : pOrganNumber;
        if (organNumber == 0) {
            LOG.log(Level.WARNING, "pOrganId is null or 0!");
            return null;
        }
        for (Map.Entry<Integer, HumanPartGraphic> entry : getHumanPartList(pGender).entrySet()) {
            int organNumberTmp = entry.getKey();
            //if (id.equalsIgnoreCase(organId)) {
            if (organNumberTmp == organNumber) {
                return entry.getValue();
            }
        }
        LOG.log(Level.WARNING, "No organ found with this id: " + organNumber);
        return null;
    }

    public static boolean isMale(final char pGender) {
        return 'm' == Character.toLowerCase(pGender);
    }

    public static boolean isFemale(final char pGender) {
        return 'w' == Character.toLowerCase(pGender) || 'f' == Character.toLowerCase(pGender);
    }

    public static boolean isUnknown(final char pGender) {
        return 'u' == Character.toLowerCase(pGender);
    }

    public static boolean isIndifferent(final char pGender) {
        return 'i' == Character.toLowerCase(pGender);
    }

    /**
     * Gives you a complete map of human body and organs. Please pay attention,
     * that the order of body parts is relevant for rendering (organs maybe hard
     * to identify if they are overlayed by other organs)!
     *
     * @param pGender Gender (m/w/u/i)
     * @return map (key = organ id, value = organ instance)
     */
    public static Map<Integer, HumanPartGraphic> getHumanPartList(final char pGender) {
        Map<Integer, HumanPartGraphic> organs = new LinkedHashMap<>();
        Body16 body = Body16.instance(pGender);
        organs.put(Body16.NUMBER, body);
        organs.put(Musculoskeletal11.NUMBER, Musculoskeletal11.instance(body, pGender));
        organs.put(Cardiovascula9.NUMBER, Cardiovascula9.instance(body, pGender));
        organs.put(Hematology7.NUMBER, Hematology7.instance(body, pGender));
        organs.put(Breathing1.NUMBER, Breathing1.instance(body, pGender));
        organs.put(Kidney12.NUMBER, Kidney12.instance(body, pGender));
        organs.put(Endocrine3.NUMBER, Endocrine3.instance(body, pGender));
        organs.put(Neurology15.NUMBER, Neurology15.instance(body, pGender));
        organs.put(Eyes2.NUMBER, Eyes2.instance(body, pGender));
        organs.put(Ent10.NUMBER, Ent10.instance(body, pGender));
        organs.put(Gastrointestinal4.NUMBER, Gastrointestinal4.instance(body, pGender));
        organs.put(UrinaryOrgans5.NUMBER, UrinaryOrgans5.instance(body, pGender));
        organs.put(Teeth14.NUMBER, Teeth14.instance(body, pGender));
        organs.put(Skin8.NUMBER, Skin8.instance(body, pGender));
        organs.put(Psychosocial13.NUMBER, Psychosocial13.instance(body, pGender));
        organs.put(UrinaryOrgans5.NUMBER, UrinaryOrgans5.instance(body, pGender));
        return organs;
    }

    @Override
    public Image loadImage(final double pHeight, final double pWidth) {
        final String path = "/img/" + getGraphicFileName();
        LOG.log(Level.FINE, "Load svg file for organ '" + this.getName() + " (number " + this.getNumber() + ")': " + path);
        try (final InputStream svgFile = getClass().getResourceAsStream(path)) {
            final boolean preserveRatio = false;
            final boolean smooth = true;
            final Image img = new Image(svgFile, pWidth, pHeight, preserveRatio, smooth);
            return img;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Was not able to load SVG file from " + path + " for organ '" + this.getName() + "' (number " + this.getNumber() + ")", ex);
            return null;
        }
    }

    @Override
    public double getRatio() {
        return 1;
    }

    protected static double calcViewHeight(final Pane pPane, final double pSize, final double pZoom) {
        double paneHeight = pPane.getHeight() * pZoom;
        double height = paneHeight * (pSize / 100);
        return height;
    }

    protected static double calcViewWidth(final Pane pPane, final double pSize, final double pZoom) {
        double paneWidth = pPane.getWidth() * pZoom;
        double width = paneWidth* (pSize / 100);
        return width;
    }

    @Override
    public ImageView getImageView() {
        return this.imageView;
    }

    public static List<IcdFarbeOrgan> getOrganInfo(final AcgPatientData pAcgPatientData, final HumanPartGraphic pOrgan) {
        List<IcdFarbeOrgan> tmp = new ArrayList<>();
        if (pAcgPatientData == null) {
            LOG.log(Level.FINE, "pAcgPatientData is null!");
            return tmp;
        }
        if (pOrgan == null) {
            LOG.log(Level.WARNING, "pOrgan is null!");
            return tmp;
        }
        for (IcdFarbeOrgan icdFarbeOrgan : pAcgPatientData.icd_code) {
            if (pOrgan.getNumber() == icdFarbeOrgan.organNr) {
                tmp.add(icdFarbeOrgan);
            }
        }
        //Sort by ICD
        Collections.sort(tmp, new Comparator<IcdFarbeOrgan>() {
            @Override
            public int compare(final IcdFarbeOrgan lhs, final IcdFarbeOrgan rhs) {
                return lhs.icd.compareTo(rhs.icd);
            }
        });
        return tmp;
    }

    public static OrganSeverity getMaxSeverity(final List<IcdFarbeOrgan> pIcdFarbeOrgans) {
        if (pIcdFarbeOrgans == null) {
            LOG.log(Level.WARNING, "pIcdFarbeOrgans is null!");
            return OrganSeverity.getSeverityByCcl(0);
        }
        if (pIcdFarbeOrgans.isEmpty()) {
            LOG.log(Level.FINER, "Warning: pIcdFarbeOrgans is empty!");
            return OrganSeverity.getSeverityByCcl(0);
        }
        int maxSeverity = 0;
        for (IcdFarbeOrgan icdFarbeOrgan : pIcdFarbeOrgans) {
            if (icdFarbeOrgan == null) {
                continue;
            }
            if (icdFarbeOrgan.farbeNr > maxSeverity) {
                maxSeverity = icdFarbeOrgan.farbeNr;
            }
        }
        return OrganSeverity.getSeverityByCcl(maxSeverity);
    }

    @Override
    public synchronized ImageView addToPane(final StackPane pPane, final double pZoom, final List<IcdFarbeOrgan> pIcds) {
        //pPane.setPrefWidth(pPane.getPrefWidth() * pZoom);
        //pPane.setPrefHeight(pPane.getPrefHeight() * pZoom);
        //pPane.setScaleX(pZoom);
        //pPane.setScaleY(pZoom);
//        if (pPane.getParent() != null && pPane.getParent().getParent() != null && pPane.getParent().getParent().getParent() != null &&
//                ScrollPane.class.equals(pPane.getParent().getParent().getParent().getClass())) {
//           ScrollPane scrollPane = (ScrollPane) pPane.getParent().getParent().getParent();
//           double newWidth = scrollPane.getWidth() * pZoom;
//           double newHeight = scrollPane.getHeight() * pZoom;
//           LOG.log(Level.INFO, "new width: {0}, new height: {1}", new Object[]{newWidth, newHeight});
//           pPane.setPrefWidth(newWidth);
//           pPane.setPrefHeight(newHeight);
//        }
        double height = calcViewHeight(pPane, getSize(), pZoom);
        if (height <= 0D) {
            return null;
        }
        double width;
//        if (getNumber() == Body16.NUMBER) {
            double minWidthDivHeightFactor = 2.71D;
            width = height / minWidthDivHeightFactor;
            pPane.setMaxWidth(width);
//        } else {
//            width = calcViewWidth(pPane, getSize(), pZoom);
//        }
//        double width = calcViewWidth(pPane, getSize(), pZoom); //height * getRatio();
//        double minWidthDivHeightFactor = 3.5D;
//        if (height / width > minWidthDivHeightFactor) {
//            //respect ratio between body height and width (approx. factor 2.5)
//            width = height / minWidthDivHeightFactor;
//        }
        LOG.log(Level.FINER, "zoom: " + pZoom + ", new width: " + width + ", new height: " + height);
        Image img = loadImage(height, width);
        width = img.getWidth();
        height = img.getHeight();

        final boolean isNewImageView = imageView.getImage() == null;
        if (!isNewImageView) {
            LOG.log(Level.FINE, "ImageView already exists. Reuse it to not lose listener!");
        }
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setImage(img);

        pPane.getChildren().add(imageView);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        if (body == null) {
            //Body is centered
            imageView.setTranslateX(getPositionX());
            imageView.setTranslateY(getPositionY());
        } else {
            //All other elements are relative to the body
            //iv.setManaged(false);
            double bodyHeight = calcViewHeight(pPane, body.getSize(), pZoom);
            double bodyWidth = bodyHeight * body.getRatio();
            height = bodyHeight * (getSize() / 100);
            width = height * getRatio();
            double positionX = (bodyWidth / 2) * (getPositionX() / 100);
            double positionY = (bodyHeight / 2) * (getPositionY() / 100);
            imageView.setTranslateX(positionX);
            imageView.setTranslateY(positionY);
        }

        if (!isNewImageView) {
            return imageView;
        }

        OrganSeverity severity = getMaxSeverity(pIcds);
        Set<String> icdList = new TreeSet<>();
        Set<String> edcList = new TreeSet<>();
        for (IcdFarbeOrgan icdFarbeOrgan : pIcds) {
            if (icdFarbeOrgan.icd != null && !icdFarbeOrgan.icd.isEmpty()) {
                icdList.add(icdFarbeOrgan.icd);
            }
            if (icdFarbeOrgan.edcText != null && !icdFarbeOrgan.edcText.isEmpty()) {
                edcList.add(icdFarbeOrgan.edcText);
            }
        }

        StringBuilder icds = new StringBuilder();
        StringBuilder edcs = new StringBuilder();
        for (String icd : icdList) {
            if (icds.length() > 0) {
                icds.append(", ");
            }
            icds.append(icd);
        }
        for (String edc : edcList) {
            if (edcs.length() > 0) {
                edcs.append(",\n");
            }
            edcs.append(edc);
        }
        //OrganSeverity severity = OrganSeverity.getSeverityByCcl(maxSeverity);

        //Add events, tooltips and other actions
        final String tooltipText = getNameGerman()
                + (!icds.toString().isEmpty() ? "\r\nSchweregrad: " + severity.getNameGerman() : "")
                + (!edcs.toString().isEmpty() ? "\r\nKrankheiten: " + edcs.toString() : "")
                + (!icds.toString().isEmpty() ? "\r\nICD: " + icds.toString() : "");
        final Tooltip t = new Tooltip();
        //right, here comes a shitty solution to apply same tooltip stylesheet layout as in CPX Client!
        t.setStyle("    -fx-background: rgba(30,30,30);\n"
                + "    -fx-text-fill: black;\n"
                + "    -fx-font-size: 12px;\n"
                + "    -fx-background-color: white;\n"
                + "    -fx-background-radius: 6px;\n"
                + "    -fx-background-insets: 0;\n"
                + "    -fx-padding: 0.667em 0.75em 0.667em 0.75em;\n"
                + "    -fx-background-radius: 0 0 0 0;\n"
                + "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.5) , 10, 0.0 , 0 , 3 );"
        );
        t.setText(tooltipText);
        Tooltip.install(imageView, t);

        Effect effectTmp = null;
        Effect effectBrightTmp = null;
        //final OrganSeverity severity = pIcds == null ? OrganSeverity.None : severity;
        switch (severity) {
            case Minor:
                effectTmp = MINOR_EFFECT;
                effectBrightTmp = MINOR_HIGHLIGHTED_EFFECT;
                break;
            case Moderate:
                effectTmp = MODERATE_EFFECT;
                effectBrightTmp = MODERATE_HIGHLIGHTED_EFFECT;
                break;
            case Severe:
                effectTmp = SEVERE_EFFECT;
                effectBrightTmp = SEVERE_HIGHLIGHTED_EFFECT;
                break;
            case Severst:
                effectTmp = SEVERST_EFFECT;
                effectBrightTmp = SEVERST_HIGHLIGHTED_EFFECT;
                break;
            default:
                effectTmp = GRAY;
                break;
        }

        final Effect effect = effectTmp;
        final Effect effectBright = effectBrightTmp;

        imageView.setEffect(effect);

        if (isNewImageView) {
            imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    LOG.log(Level.FINE, "On mouse enter fired for " + id);
                    if (effectBright != null) {
                        imageView.setEffect(effectBright);
                    }
                }
            });

            imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    LOG.log(Level.FINE, "On mouse exit fired for " + id);
                    imageView.setEffect(effect);
                }
            });
        }

        LOG.log(Level.FINE, getName() + ": ID = " + getId() + ", Graphic: " + getGraphicFileName() + ", Width = " + width + ", Height = " + height + ", Position X = " + getPositionX() + ", Position Y = " + getPositionY());
        return imageView;
    }

    @Override
    public Effect getCurrentEffect() {
        if (imageView == null) {
            return null;
        }
        Effect tmp = imageView.getEffect();
        return tmp;
    }

    @Override
    public Effect setEffect(final Effect pEffect) {
        if (imageView == null) {
            return null;
        }
        Effect oldEffect = getCurrentEffect();
        imageView.setEffect(pEffect);
        return oldEffect;
    }

    @Override
    public Effect showNoneColored() {
        if (imageView == null) {
            return null;
        }
        return setEffect(GRAY);
    }

    protected static Effect getMonochrome() {
        return getMonochrome(-1.0d, 0.0d, 0.0d, 0.0d);
    }

    protected static Effect getMonochrome(final double pSaturation, final double pBrightness, final double pContrast, final double pHue) {
        final ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(pSaturation);
        monochrome.setBrightness(pBrightness);
        monochrome.setContrast(pContrast);
        monochrome.setHue(pHue);
        return monochrome;
    }

    protected static Lighting getLighting(final Color pColor) {
        return getLighting(pColor, 1.0d);
    }

    protected static Lighting getLighting(final Color pColor, final double pBrightness) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(pBrightness);
        lighting.setSpecularConstant(pBrightness);
        lighting.setSpecularExponent(pBrightness);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, pColor));
        return lighting;
    }

    @Override
    public double getSize() {
        return this.size;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public double getPositionX() {
        return this.positionX;
    }

    @Override
    public double getPositionY() {
        return this.positionY;
    }

    @Override
    public String getGraphicFileName() {
        return this.graphicFileName;
    }

    @Override
    public String getNameEnglish() {
        return this.nameEnglish;
    }

    @Override
    public String getNameGerman() {
        return this.nameGerman;
    }

    @Override
    public String getName() {
        return getNameEnglish() + "/" + getNameGerman();
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", name: " + getName() + ", number: " + getNumber() + ", graphic: " + getGraphicFileName();
    }

    @Override
    public BodyGraphic getBody() {
        return this.body;
    }

    @Override
    public String getId() {
        return id;
    }

}
