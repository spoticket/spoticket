//package com.spoticket.teamstadium.factory;
//
//import com.spoticket.teamstadium.domain.model.Seat;
//import com.spoticket.teamstadium.domain.model.Stadium;
//import java.util.UUID;
//
//public class SeatTestFactory {
//
//  public static Seat createWithId(
//      UUID seatId,
//      UUID gameId,
//      String section,
//      long quantity,
//      Integer price,
//      Stadium stadium
//  ) {
//    return Seat.builder()
//        .seatId(seatId)
//        .gameId(gameId)
//        .section(section)
//        .quantity(quantity)
//        .price(price)
//        .stadium(stadium)
//        .build();
//  }
//
//}
