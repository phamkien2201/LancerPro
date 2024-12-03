package com.cosmetics.order.configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPAYConfig {

    private static final String VN_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String MERCHANT_CODE = "TRAV6G6O";
    private static final String SECRET_KEY = "EEEDX2BHCFNGW93R8W6EFKYSG8F59XKY";
    private static final String CURRENCY_CODE = "VND";
    private static final String LOCALE = "vn";

    public static String createVNPayPaymentUrl(String txnRef, String orderInfo, float amount, String returnUrl) {
        // Tạo tham số yêu cầu gửi lên VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_TmnCode", MERCHANT_CODE);
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", CURRENCY_CODE);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_Locale", LOCALE);
        vnp_Params.put("vnp_ReturnUrl", returnUrl);

        // Tạo chữ ký bảo mật
        String vnp_SecureHash = generateSecureHash(vnp_Params, SECRET_KEY);
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        // Tạo URL thanh toán
        StringBuilder vnpUrlBuilder = new StringBuilder(VN_PAY_URL);
        vnpUrlBuilder.append("?");
        vnp_Params.forEach((key, value) -> {
            try {
                vnpUrlBuilder.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        return vnpUrlBuilder.toString();
    }

    public static String generateSecureHash(Map<String, String> vnp_Params, String secretKey) {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (String field : fieldNames) {
            String value = vnp_Params.get(field);
            if (value != null && !value.isEmpty()) {
                hashData.append(field).append("=").append(value).append("&");
            }
        }

        // Loại bỏ dấu "&" cuối cùng
        if (hashData.length() > 0) {
            hashData.deleteCharAt(hashData.length() - 1);
        }

        // Tạo chữ ký
        String data = hashData.toString();
        return HMAC_SHA512(secretKey, data);
    }

    private static String HMAC_SHA512(String secretKey, String data) {
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            byte[] hash = sha512_HMAC.doFinal(data.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating secure hash", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
