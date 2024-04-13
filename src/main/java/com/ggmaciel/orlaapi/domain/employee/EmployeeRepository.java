package com.ggmaciel.orlaapi.domain.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByCpf(String cpf);
    Employee findByEmail(String email);
}
