package com.hampcode.restaurant_reservation.dto.response;

import lombok.Data;

@Data
public class AuthResponseDTO {
  private String token;
  private UserProfileResponseDTO user;
}