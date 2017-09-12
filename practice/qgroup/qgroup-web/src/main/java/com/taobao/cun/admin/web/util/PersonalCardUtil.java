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
 * ��PersonalCardUtil.java��ʵ�����������֤������
 * 
 * @author huiran.wenghr 2011-9-28 ����06:24:59
 */

public class PersonalCardUtil {

    private static Logger        logger                = LoggerFactory.getLogger(PersonalCardUtil.class);
    private static final Pattern idCardNumberPattern18 = Pattern.compile("^([0-9]){15}$");
    private static final Pattern idCardNumberPattern15 = Pattern.compile("^([0-9]){17}[0-9Xx]{1}$");

    /**
     * �����֤�Ź��Ϊ18λ��
     * 
     * @param idCardNumber 15��18λ�����֤��
     * @return 18λ��񻯵����֤��
     */
    public static String getIdCardNumber18(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return null;
        }
        /*
         * BUGFIX:18λ���֤���һλӦ����X
         */
        if (idCardNumber.length() == 18) {
            return idCardNumber.toUpperCase();
        } else if (idCardNumber.length() != 15) {
            return null;
        }

        /* ���Ƚ����֤������չ��17λ: ����������չΪ19XX����ʽ */
        String idCardNumber17 = idCardNumber.substring(0, 6) + "19" + idCardNumber.substring(6);

        /* ����У���� */
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
         * BUGFIX:18λ���֤���һλӦ����X
         */
        if (nSum == 10) {
            return idCardNumber17 + "X";
        } else {
            return idCardNumber17 += nSum;
        }
    }

    /**
     * �����֤�Ź��Ϊ15λ��
     * 
     * @param idCardNumber 15��18λ�����֤��
     * @return 15λ��񻯵����֤��
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
     * �ж��������֤�����Ƿ�һ�£���������15��18���֣�ֱ�ӿ����ж����֤�Ƿ�һ��
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
     * �����֤����ȡ���䡣
     * 
     * @param idCardNumber 15��18λ�����֤��
     * @return �����֤����ȡ��������
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
     * �����֤���ж��Ƿ���꣱������
     * 
     * @param idCardNumber 15��18λ�����֤��
     * @return �����֤����ȡ��������
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
     * ������֤���Ƿ���Ч��
     * 
     * @param idCardNumber
     * @return
     */
    public static boolean checkIdCardNumber(String idCardNumber) {
        return checkIdCardNumber(idCardNumber, -1);
    }
    

    /**
     * ������֤���Ƿ���Ч��
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
     * ��������Ƿ���Ч��
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
     * ȡ�õ�ǰ��ݡ�
     * 
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = new GregorianCalendar();

        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }
}
