package com.proelectricos.mdserp;

import com.proelectricos.mdserp.model.entity.sqlfactory.emp004_inv.Adic;
import com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv.Op1;
import com.proelectricos.mdserp.repository.pdm.VariableRepository;
import com.proelectricos.mdserp.repository.sqlfactory.emp004_inv.AdicRepository;
import com.proelectricos.mdserp.repository.sqlfactory.emp001_inv.Op1Repository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class MdserpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MdserpApplication.class, args);
    }



}

