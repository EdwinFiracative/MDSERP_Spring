package com.proelectricos.mdserp.erpdb.vendor.repository;

import com.proelectricos.mdserp.erpdb.vendor.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
