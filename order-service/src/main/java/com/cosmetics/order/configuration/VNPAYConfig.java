package com.cosmetics.order.configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        try {
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_TmnCode", MERCHANT_CODE);
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_Amount", String.valueOf((long) (amount * 100)));
            vnp_Params.put("vnp_CurrCode", CURRENCY_CODE);
            vnp_Params.put("vnp_OrderInfo", orderInfo);
            vnp_Params.put("vnp_Locale", LOCALE);
            vnp_Params.put("vnp_ReturnUrl", returnUrl);

            String vnp_SecureHash = generateSecureHash(vnp_Params, SECRET_KEY);
            vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

            StringBuilder vnpUrlBuilder = new StringBuilder(VN_PAY_URL);
            vnpUrlBuilder.append("?");
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                vnpUrlBuilder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)).append("&");
            }
            return vnpUrlBuilder.substring(0, vnpUrlBuilder.length() - 1); // Remove trailing "&"
        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay URL", e);
        }
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
