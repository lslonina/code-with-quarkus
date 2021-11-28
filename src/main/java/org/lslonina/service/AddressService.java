package org.lslonina.service;

import javax.enterprise.context.ApplicationScoped;

import org.lslonina.entity.Address;
import org.lslonina.service.dto.AddressDto;

@ApplicationScoped
public class AddressService {
    public static Address createFromDto(AddressDto addressDto) {
        return new Address(addressDto.getAddress1(),
                addressDto.getAddress2(),
                addressDto.getCity(),
                addressDto.getPostcode(),
                addressDto.getCountry());
    }

    public static AddressDto maptoDto(Address address) {
        return new AddressDto(address.getAddress1(),
                address.getAddress2(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }
}
