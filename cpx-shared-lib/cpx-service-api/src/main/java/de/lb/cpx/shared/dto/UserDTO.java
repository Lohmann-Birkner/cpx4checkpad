/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import de.lb.cpx.str.utils.StrUtils;
import java.awt.Color;
import java.io.Serializable;
import java.util.Date;

/**
 * DTO, used to transfer user data to the client
 *
 * @author wilde
 */
public class UserDTO implements Serializable, MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    private final String fullName;
    private final String userName;
    private final Long id;
    private final Date validFrom;
    private final Date validTo;
    private final boolean valid;
    private final boolean deleted;

//    public UserDTO(final Long id, final String fullName, final String userName) {
//        this(id, fullName, userName);
//    }
    public UserDTO(final Long pId, final String pFullName, final String pUserName,
            final Date pValidFrom, final Date pValidTo, boolean pValid,
            boolean pDeleted) {
        this.id = pId;
        this.fullName = pFullName;
        this.userName = pUserName;
        this.validFrom = pValidFrom;
        this.validTo = pValidTo;
        this.valid = pValid;
        this.deleted = pDeleted;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getId() {
        return id;
    }

    /**
     * @return indicator if user is valied, default false
     */
    @Override
    public boolean isValid() {
        return valid;
    }

    /**
     * @return indicator if user is deleted, default false
     */
    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean isInActive() {
        return isInActive(new Date());
    }

    @Override
    public boolean isInActive(final Date pDate) {
        return isDeleted() || !isValid(pDate);
    }

    @Override
    public String toString() {
        return userName;
    }

    /**
     * @return the validFrom
     */
    @Override
    public Date getValidFrom() {
        return validFrom;
    }

    /**
     * @return the validTo
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }

    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, userName);
    }

    @Override
    public Long getMenuCacheId() {
        return id;
    }

    @Override
    public int getSort() {
        return 0; //does not exist for user!
    }

    @Override
    public boolean isValid(Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    public String getHexColor() {
        return getHexColor(userName);
    }

    private static String getHexColor(final String pValue) {
        if (pValue == null) {
            return null;
        }
        String value = pValue.trim(); //.toLowerCase();
        String hash = StrUtils.getHash(value);
        StringBuilder sb = new StringBuilder();
        long mod = 16777216L + 1;
        for (int i = 0; i < hash.length(); i++) {
            sb.append((int) hash.charAt(i));
            long val = Long.parseLong(sb.toString());
            long rest = val % mod;
            sb = new StringBuilder(rest + "");
        }
        int rgb = Integer.parseInt(sb.toString());

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb >> 0) & 0xFF;

        //adjust saturation (s) and brightness (b) with the help of HSV color model to get good looking user colors
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);

        float threshold = 0.03F;
        if (hsb[1] <= threshold && hsb[2] <= threshold) {
            //make it real black!
            hsb[1] = 0F;
            hsb[2] = 0F;
        } else if (hsb[1] <= threshold && hsb[2] >= 1F - threshold) {
            //make it real white!
            hsb[1] = 0F;
            hsb[2] = 1F;
        } else {
//            Random r = new SecureRandom();
            float minVal = 0.5F;
            float step = 0.1F;
            while (hsb[1] < minVal) {
                //float range = 1F - hsv[1];
//                float val = r.nextFloat();
                if (step + hsb[1] <= 1F) {
                    hsb[1] += step;
                }
            }
            while (hsb[2] < minVal) {
                //float range = 1F - hsv[1];
//                float val = r.nextFloat();
                if (step + hsb[2] <= 1F) {
                    hsb[2] += step;
                }
            }
            if (hsb[1] > 1F) {
                hsb[1] = 1F;
            }
            if (hsb[2] > 1F) {
                hsb[2] = 1F;
            }
        }

        rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        red = (rgb >> 16) & 0xFF;
        green = (rgb >> 8) & 0xFF;
        blue = rgb & 0xFF;

        return "#"
                + StrUtils.leftPad(Integer.toHexString(red), 2, '0')
                + StrUtils.leftPad(Integer.toHexString(green), 2, '0')
                + StrUtils.leftPad(Integer.toHexString(blue), 2, '0');
        //return "#" + StrUtils.leftPad(Integer.toHexString(rgb), 6, '0');
//        SecureRandom rand = new SecureRandom(salt.getBytes());
//        int r = rand.nextInt(255);
//        int g = rand.nextInt(255);
//        int b = rand.nextInt(255);
//        String rHex = Integer.toHexString(r);
//        String gHex = Integer.toHexString(g);
//        String bHex = Integer.toHexString(b);
//        String hexCode = "#" + rHex + gHex + bHex;
//        return hexCode;
    }

//    public static void main(String[] args) {
//        List<String> users = new ArrayList<>();
//        users.add("aaa");
//        users.add("abc");
//        users.add("Adameck");
//        users.add("admin");
//        users.add("amin");
//        users.add("batchJob");
//        users.add("bean");
//        users.add("bean_II");
//        users.add("bean_q");
//        users.add("bhaTest");
//        users.add("bohm");
//        users.add("chack_noris");
//        users.add("chack_noris_2");
//        users.add("chuck_norris_kicks_your_balls_to_nothing");
//        users.add("ddd");
//        users.add("gaun");
//        users.add("gerschmann");
//        users.add("halabieh");
//        users.add("Hamu");
//        users.add("hasse");
//        users.add("husser");
//        users.add("imortJobDB");
//        users.add("imortJobP21");
//        users.add("imortJobSAP");
//        users.add("Julia");
//        users.add("JUnit");
//        users.add("kannengiesser");
//        users.add("khaled-test");
//        users.add("kruckow");
//        users.add("kurumi");
//        users.add("kurumiALT");
//        users.add("lohmann");
//        users.add("max_muster");
//        users.add("messe");
//        users.add("migration");
//        users.add("migrationUser");
//        users.add("milow");
//        users.add("mister_x");
//        users.add("Mtest");
//        users.add("müllerS1");
//        users.add("Muster");
//        users.add("Mustermann");
//        users.add("mustermensch");
//        users.add("nandola");
//        users.add("niemeier");
//        users.add("ninio");
//        users.add("PEPP");
//        users.add("PEPP1");
//        users.add("PEPP3");
//        users.add("Philipp");
//        users.add("presentation");
//        users.add("presentation1");
//        users.add("shahin");
//        users.add("sklarow");
//        users.add("smadi");
//        users.add("smadi_Test");
//        users.add("Smadi_test_01");
//        users.add("smaditest2");
//        users.add("smadi2");
//        users.add("steiner");
//        users.add("test");
//        users.add("Test");
//        users.add("TestBenutzer");
//        users.add("test_bha");
//        users.add("testentfern");
//        users.add("teste_password");
//        users.add("tester");
//        users.add("TestPerson");
//        users.add("test_user");
//        users.add("testuser");
//        users.add("testUser");
//        users.add("test_user_bha");
//        users.add("test_user_o");
//        users.add("testuser_xii");
//        users.add("testuser_xxx");
//        users.add("testuser_1002");
//        users.add("testuser1002");
//        users.add("testuser_1003");
//        users.add("testuser_1004");
//        users.add("testuser_1005");
//        users.add("testuser_112");
//        users.add("Test111");
//        users.add("test_12");
//        users.add("test_122");
//        users.add("test_15");
//        users.add("test17");
//        users.add("Test311");
//        users.add("theile");
//        users.add("t_role");
//        users.add("ttest");
//        users.add("urbach");
//        users.add("user");
//        users.add("user_iii");
//        users.add("wilde");
//        users.add("Wilhelm");
//        users.add("yezhov");
//        users.add("yezhov2");
//        users.add("4242");
//        for (String user : users) {
//            String color = getHexColor(user);
//            System.out.println(StrUtils.rightPad(user, 20) + " -> " + color + " (" + color.length() + ")");
//        }
//}
}
