package com.example.CampusConnectService.repository;

import com.example.CampusConnectService.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile,Long> {
    Optional<List<StudentProfile>> findByUniversity(String university);
}
