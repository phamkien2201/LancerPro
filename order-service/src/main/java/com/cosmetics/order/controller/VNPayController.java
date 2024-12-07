package com.cosmetics.order.controller;

import com.cosmetics.order.configuration.VNPAYConfig;
import com.cosmetics.order.dto.response.OrderResponse;
import com.cosmetics.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class VNPayController {
    private final OrderService orderService;
    private final VNPAYConfig vnpayConfig;

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
        String secretKey = "EEEDX2BHCFNGW93R8W6EFKYSG8F59XKY";
        vnp_Params.remove("vnp_SecureHash");

        String generatedHash = VNPAYConfig.generateSecureHash(vnp_Params, secretKey);

        // Lấy orderId từ request
        String orderId = vnp_Params.get("vnp_TxnRef");
        String responseCode = vnp_Params.get("vnp_ResponseCode");

        // Kiểm tra chữ ký và mã phản hồi
        if (secureHash.equals(generatedHash) && "00".equals(responseCode)) {
            // Xác nhận thanh toán thành công
            OrderResponse orderResponse = orderService.confirmVNPayPayment(orderId, true);
            return ResponseEntity.ok("Thanh toán thành công cho đơn hàng: " + orderId);
        } else {
            // Thanh toán thất bại
            orderService.confirmVNPayPayment(orderId, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán thất bại");
        }
    }
}