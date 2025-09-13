package com.kkumgeurimi.kopring.config

import com.kkumgeurimi.kopring.domain.program.entity.RegistrationStatus
import com.kkumgeurimi.kopring.domain.program.repository.ProgramRegistrationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class ProgramStatusScheduler(
    private val programRegistrationRepository: ProgramRegistrationRepository
) {
    
     // endDate가 지난 REGISTERED 상태의 프로그램들을 COMPLETED로 업데이트 
    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 실행
    @Transactional
    fun updateCompletedPrograms() {
        val currentDate = LocalDate.now()
        
        programRegistrationRepository.updateRegistrationStatusByEndDate(
            currentStatus = RegistrationStatus.REGISTERED,
            newStatus = RegistrationStatus.COMPLETED,
            currentDate = currentDate
        )
    }
}
