package com.taobao.cun.admin.web.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;

/**
 * 类PersonalCardUtil.java的实现描述：身份证工具类
 * 
 * @author huiran.wenghr 2011-9-28 下午06:24:59
 */

public class PersonalCardUtil {

    private static Logger        logger                = LoggerFactory.getLogger(PersonalCardUtil.class);
    private static final Pattern idCardNumberPattern18 = Pattern.compile("^([0-9]){15}$");
    private static final Pattern idCardNumberPattern15 = Pattern.compile("^([0-9]){17}[0-9Xx]{1}$");

    /**
     * 将身份证号规格化为18位。
     * 
     * @param idCardNumber 15或18位的身份证号
     * @return 18位规格化的身份证号
     */
    public static String getIdCardNumber18(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return null;
        }
        /*
         * BUGFIX:18位身份证最后一位应该是X
         */
        if (idCardNumber.length() == 18) {
            return idCardNumber.toUpperCase();
        } else if (idCardNumber.length() != 15) {
            return null;
        }

        /* 首先将身份证号码扩展至17位: 将出生年扩展为19XX的形式 */
        String idCardNumber17 = idCardNumber.substring(0, 6) + "19" + idCardNumber.substring(6);

        /* 计算校验码 */
        int nSum = 0;

        try {
            for (int nCount = 0; nCount < 17; nCount++) {
                nSum += (Integer.parseInt(idCardNumber17.substring(nCount, nCount + 1)) * (Math.pow(2, 17 - nCount) % 11));
            }
        } catch (Exception e) {
        }

        nSum %= 11;

        if (nSum <= 1) {
            nSum = 1 - nSum;
        } else {
            nSum = 12 - nSum;
        }
        /*
         * BUGFIX:18位身份证最后一位应该是X
         */
        if (nSum == 10) {
            return idCardNumber17 + "X";
        } else {
            return idCardNumber17 += nSum;
        }
    }

    /**
     * 将身份证号规格化为15位。
     * 
     * @param idCardNumber 15或18位的身份证号
     * @return 15位规格化的身份证号
     */
    public static String getIdCardNumber15(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return null;
        }

        if (idCardNumber.length() == 15) {
            return idCardNumber;
        } else if (idCardNumber.length() == 18) {
            return idCardNumber.substring(0, 6) + idCardNumber.substring(8, 17);
        }

        return null;
    }

    /**
     * 判断两个身份证号码是否一致，可以屏蔽15＆18区分，直接可以判断身份证是否一致
     * 
     * @param newCertNo
     * @param oldCertNo
     * @return
     */
    public static boolean checkCertNoEquals(String newCertNo, String oldCertNo) {
        if (StringUtil.isBlank(oldCertNo) || StringUtil.isBlank(newCertNo)) {
            return false;
        }

        if (!checkIdCardNumber(newCertNo) || !checkIdCardNumber(oldCertNo)) {
            return false;
        }

        if (StringUtil.equalsIgnoreCase(oldCertNo, getIdCardNumber15(newCertNo))
            || StringUtil.equalsIgnoreCase(oldCertNo, getIdCardNumber18(newCertNo))) {
            return true;
        }

        return false;
    }

    /**
     * 从身份证号析取年龄。
     * 
     * @param idCardNumber 15或18位的身份证号
     * @return 在身份证中析取出的年龄
     */
    public static int getAgeFromIdCardNumber(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return 0;
        }

        String strYear = null;

        if (idCardNumber.length() == 15) {
            strYear = "19" + idCardNumber.substring(6, 8);
        } else if (idCardNumber.length() == 18) {
            strYear = idCardNumber.substring(6, 10);
        } else {
            return 0;
        }

        int year = 0;

        try {
            year = Integer.parseInt(strYear);
        } catch (Exception e) {
            logger.error("Failed to parse year from idCardNumber " + idCardNumber + " due to exception.", e);
            return 0;
        }

        int thisYear = (new GregorianCalendar()).get(Calendar.YEAR);

        return (thisYear - year);
    }

    /**
     * 从身份证号判断是否成年１８周岁
     * 
     * @param idCardNumber 15或18位的身份证号
     * @return 在身份证中析取出的年龄
     */
    public static boolean checkAgeIsAdult(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return false;
        }

        String birthYear = null;
        String birthDateAnMouth = null;

        if (idCardNumber.length() == 15) {
            birthYear = "19" + idCardNumber.substring(6, 8);

            birthDateAnMouth = idCardNumber.substring(8, 12);
        } else if (idCardNumber.length() == 18) {
            birthYear = idCardNumber.substring(6, 10);
            birthDateAnMouth = idCardNumber.substring(10, 14);
        } else {
            return false;
        }

        birthYear = Integer.parseInt(birthYear) + 18 + "";

        String adultDay = birthYear + birthDateAnMouth;

        String today = DateUtil.toLocaleString(new Date(), "yyyyMMdd");

        boolean a = !(Integer.parseInt(adultDay) > Integer.parseInt(today));
        // DateUtil.parseDateNoTime(sDate)

        return a;
    }

    /**
     * 检查身份证号是否有效。
     * 
     * @param idCardNumber
     * @return
     */
    public static boolean checkIdCardNumber(String idCardNumber) {
        return checkIdCardNumber(idCardNumber, -1);
    }
    

    /**
     * 检查身份证号是否有效。
     * 
     * @param idCardNumber
     * @param age
     * @return
     */
    public static boolean checkIdCardNumber(String idCardNumber, int age) {
        Matcher matcher = null;

        matcher = idCardNumberPattern15.matcher(idCardNumber);

        if (!matcher.find()) {
            matcher = idCardNumberPattern18.matcher(idCardNumber);

            if (!matcher.find()) {
                return false;
            }
        }

        String idCardNumber18 = getIdCardNumber18(idCardNumber);

        if (idCardNumber18 == null) {
            return false;
        }

        try {
            int year = Integer.parseInt(idCardNumber18.substring(6, 10));
            int month = Integer.parseInt(idCardNumber18.substring(10, 12));
            int day = Integer.parseInt(idCardNumber18.substring(12, 14));

            if (!checkDate(year, month, day)) {
                return false;
            }

            if (age > (getCurrentYear() - year)) {
                return false;
            }
        } catch (Exception e) {
            logger.warn("Failed to parse integer due to exception.", e);
            return false;
        }

        if (idCardNumber.length() == 18) {
            if (!idCardNumber.equalsIgnoreCase(getIdCardNumber18(getIdCardNumber15(idCardNumber)))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查日期是否有效。
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean checkDate(int year, int month, int day) {
        if ((year < 1900) || (year > getCurrentYear())) {
            return false;
        }

        if ((month < 1) || (month > 12)) {
            return false;
        }

        Calendar cal = new GregorianCalendar();

        cal.set(year, month - 1, 1);

        if ((day < 1) || (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))) {
            return false;
        }

        return true;
    }

    /**
     * 取得当前年份。
     * 
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = new GregorianCalendar();

        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }
}
