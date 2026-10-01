package com.proelectricos.mdserp.erpdb.measurunit.repository;

import com.proelectricos.mdserp.erpdb.measurunit.MeasurUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurUnitRepository extends JpaRepository<MeasurUnit, Long> {
}
