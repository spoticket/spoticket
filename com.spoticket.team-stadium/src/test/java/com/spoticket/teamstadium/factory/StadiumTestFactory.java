//package com.spoticket.teamstadium.factory;
//
//import com.spoticket.teamstadium.domain.model.Stadium;
//import java.util.UUID;
//import org.locationtech.jts.geom.Point;
//
//public class StadiumTestFactory {
//
//  public static Stadium createWithId(
//      UUID stadiumId,
//      String name,
//      String address,
//      Point latLng,
//      String seatImage,
//      String descripion
//  ) {
//    return Stadium.builder()
//        .stadiumId(stadiumId)
//        .name(name)
//        .address(address)
//        .latLng(latLng)
//        .seatImage(seatImage)
//        .description(descripion)
//        .build();
//  }
//}
