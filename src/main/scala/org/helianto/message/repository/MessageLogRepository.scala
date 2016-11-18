package org.helianto.message.repository

import org.helianto.message.domain.MessageLog
import org.springframework.data.jpa.repository.JpaRepository

trait MessageLogRepository extends JpaRepository[MessageLog, Integer] {

}
