package com.cosmetics.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.cosmetics.order.configuration.VNPAYConfig.generateSecureHash;

@RestController
@RequestMapping("/vnpay")
public class VNPayController {

    @PostMapping("/return")
    public ResponseEntity<?> vnpayReturn(HttpServletRequest request) {
        // Nhận các tham số từ VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = enumeration.nextElement();
            vnp_Params.put(paramName, request.getParameter(paramName));
        }

        // Kiểm tra chữ ký và xác thực kết quả
        String secureHash = vnp_Params.get("vnp_SecureHash");
        String secretKey = "EEEDX2BHCFNGW93R8W6EFKYSG8F59XKY";  // Thay thế với Secret Key
        vnp_Params.remove("vnp_SecureHash");

        String generatedHash = generateSecureHash(vnp_Params, secretKey);

        if (secureHash.equals(generatedHash)) {
            // Thanh toán thành công
            // Xử lý logic thanh toán thành công
            return ResponseEntity.ok("Thanh toán thành công");
        } else {
            // Thanh toán thất bại
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán thất bại");
        }
    }
}
