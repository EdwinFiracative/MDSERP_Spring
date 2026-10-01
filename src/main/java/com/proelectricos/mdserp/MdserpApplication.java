package com.proelectricos.mdserp;

import com.proelectricos.mdserp.emp001_inv.op1.Op1;
import com.proelectricos.mdserp.emp001_inv.op1.repository.Op1Repository;
import com.proelectricos.mdserp.emp004_inv.adic.Adic;
import com.proelectricos.mdserp.emp004_inv.adic.repository.AdicRepository;
import com.proelectricos.mdserp.repository.pdm.VariableRepository;
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

