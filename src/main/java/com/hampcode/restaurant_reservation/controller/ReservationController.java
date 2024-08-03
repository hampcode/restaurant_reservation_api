package com.hampcode.restaurant_reservation.controller;

import com.hampcode.restaurant_reservation.dto.request.ReservationRequestDTO;
import com.hampcode.restaurant_reservation.dto.response.ReservationResponseDTO;
import com.hampcode.restaurant_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping
  public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody @Validated ReservationRequestDTO reservationRequestDTO) {
    ReservationResponseDTO reservationResponse = reservationService.createReservation(reservationRequestDTO);
    return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);
  }

  @GetMapping("/my-reservations")
  public ResponseEntity<List<ReservationResponseDTO>> getMyReservations() {
    List<ReservationResponseDTO> reservations = reservationService.getReservationsByClientId();
    return ResponseEntity.ok(reservations);
  }

}
