package user.com.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import user.com.user.model.PaymentStatusRecord;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusRecord, Long>{
    
}
