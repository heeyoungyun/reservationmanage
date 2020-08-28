package skcc;

import skcc.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyHandler{

    @Autowired
    ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRequested_ReservationRegister(@Payload Requested requested){

        //  검진 요청으로 인한 예약 확정
        System.out.println("##### 검진 요청으로 인한 예약 확정: " + requested.toJson());
        if(requested.isMe()){
            Reservation temp = new Reservation();
            temp.setStatus("REQUEST_COMPLETED");
            temp.setCustNm(requested.getCustNm());
            temp.setHospitalId(requested.getHospitalId());
            temp.setChkDate(requested.getChkDate());
            temp.setScreeningId(requested.getScreeningId());
            reservationRepository.save(temp);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverScreeningChanged_ReservationCange(@Payload ScreeningChanged screeningChanged){

        //  검진 변경으로 인한 변경

        if(screeningChanged.isMe()){

        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeleted_ForcedReservationCancel(@Payload Deleted deleted){

        if(deleted.isMe()){
            //  병원일정 삭제로 인한 , 스케쥴 상태 변경
            System.out.println("##### 검진 일정 삭제로 인한, 강제 예약취소 : " + deleted.toJson());

            List<Reservation> list = reservationRepository.findByHospitalIdAndChkDate(deleted.getHospitalId(),deleted.getChkDate());
            for(Reservation temp : list){
                temp.setStatus("ForcedCanceled");
                reservationRepository.save(temp);
            }

        }
    }

}
