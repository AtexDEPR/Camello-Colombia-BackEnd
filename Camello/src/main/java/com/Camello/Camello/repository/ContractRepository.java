package com.Camello.Camello.repository;

import com.Camello.Camello.entity.Contract;
import com.Camello.Camello.entity.ContractorProfile;
import com.Camello.Camello.entity.FreelancerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    
    Page<Contract> findByFreelancerOrderByCreatedAtDesc(FreelancerProfile freelancer, Pageable pageable);
    
    Page<Contract> findByContractorOrderByCreatedAtDesc(ContractorProfile contractor, Pageable pageable);
    
    List<Contract> findByFreelancerAndStatus(FreelancerProfile freelancer, Contract.ContractStatus status);
    
    List<Contract> findByContractorAndStatus(ContractorProfile contractor, Contract.ContractStatus status);
    
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.freelancer = :freelancer AND c.status = 'COMPLETED'")
    Long countCompletedContractsByFreelancer(@Param("freelancer") FreelancerProfile freelancer);
    
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.contractor = :contractor AND c.status = 'COMPLETED'")
    Long countCompletedContractsByContractor(@Param("contractor") ContractorProfile contractor);
    
    @Query("SELECT SUM(c.agreedPrice) FROM Contract c WHERE c.freelancer = :freelancer AND c.status = 'COMPLETED'")
    Double getTotalEarningsByFreelancer(@Param("freelancer") FreelancerProfile freelancer);
    
    @Query("SELECT SUM(c.agreedPrice) FROM Contract c WHERE c.contractor = :contractor AND c.status = 'COMPLETED'")
    Double getTotalSpentByContractor(@Param("contractor") ContractorProfile contractor);
}