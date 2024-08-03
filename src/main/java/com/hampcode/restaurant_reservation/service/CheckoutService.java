package com.hampcode.restaurant_reservation.service;

import com.hampcode.restaurant_reservation.domain.entity.Reservation;
import com.hampcode.restaurant_reservation.dto.paypal.OrderCaptureResponse;
import com.hampcode.restaurant_reservation.dto.paypal.OrderResponse;
import com.hampcode.restaurant_reservation.dto.response.PaypalCaptureResponse;
import com.hampcode.restaurant_reservation.dto.response.PaypalOrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {

    private PaypalService paypalService;
    private ReservationService reservationService;

    public PaypalOrderResponse createPaypalPaymentUrl(Long reservationId, String returnUrl, String cancelUrl) {
        OrderResponse orderResponse = paypalService.createOrder(reservationId, returnUrl, cancelUrl);

        String paypalUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();

        return new PaypalOrderResponse(paypalUrl);
    }

    public PaypalCaptureResponse capturePaypalPayment(String orderId) {
        OrderCaptureResponse orderCaptureResponse = paypalService.captureOrder(orderId);
        boolean completed = orderCaptureResponse.getStatus().equals("COMPLETED");

        PaypalCaptureResponse paypalCaptureResponse = new PaypalCaptureResponse();
        paypalCaptureResponse.setCompleted(completed);

        if (completed) {
            String purchaseIdStr = orderCaptureResponse.getPurchaseUnits().getFirst().getReferenceId();
            Reservation reservation = reservationService.confirmReservationPayment(Long.parseLong(purchaseIdStr));
            paypalCaptureResponse.setReservationId(reservation.getId());
        }
        return paypalCaptureResponse;
    }

}