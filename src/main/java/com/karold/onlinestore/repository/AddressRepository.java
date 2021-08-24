package com.karold.onlinestore.repository;

import com.karold.onlinestore.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
