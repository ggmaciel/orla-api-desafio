package com.ggmaciel.orlaapi.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

    Project findByName(String name);
}
