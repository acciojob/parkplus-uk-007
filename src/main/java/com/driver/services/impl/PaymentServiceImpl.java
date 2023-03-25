package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();

        Spot spot = reservation.getSpot();

        int totalCost = reservation.getNumberOfHours() * spot.getPricePerHour();

        mode = mode.toUpperCase();
        boolean marker = true;
        switch (mode) {
            case "CASH":
                marker = false;
                break;
            case "CARD":
                marker = false;
                break;
            case "UPI":
                marker = false;
                break;
        }

        Payment payment = new Payment();

        try {
            if (totalCost > amountSent) {
                throw new Exception("Insufficient Amount");
            }
            if (marker) {
                throw new Exception("Payment mode not detected");
            }
        } catch (Exception e) {
//            payment.setPaymentCompleted(false);
//            paymentRepository2.save(payment);
            throw new Exception(e.getMessage());
        }


//        Payment payment = new Payment();
        payment.setPaymentMode(PaymentMode.valueOf(mode));
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);

        reservation.setPayment(payment);

        paymentRepository2.save(payment);

        return payment;

    }
}
