package skcc;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long>{

    List<Reservation> findByHospitalIdAndChkDate(String HospitalId, String ChkDate);

    List<Reservation> findAllByOrderByResvidAsc();

}