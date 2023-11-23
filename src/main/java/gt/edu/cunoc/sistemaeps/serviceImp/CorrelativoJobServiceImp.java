package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import gt.edu.cunoc.sistemaeps.service.CorrelativoService;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author edvin
 */
@Service
public class CorrelativoJobServiceImp{

    private final CorrelativoService correlativoService;

    public CorrelativoJobServiceImp(CorrelativoService correlativoService) {
        this.correlativoService = correlativoService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0 1 1 *")
    @Transactional
    public void actualizarCorrelativos() {
        List<Correlativo> correlativos=this.correlativoService.getAll();
        Integer currentYear = DateUtils.getCurrentDate().getYear();
        for(Correlativo correlativo:correlativos){
            if(correlativo.getUltimaActualizacion().getYear()<currentYear){
                correlativo.setUltimaActualizacion(DateUtils.getCurrentDate());
                correlativo.setNumeracionActual(0);
                this.correlativoService.save(correlativo);
            }
        }
    }
}
