package com.sh.maleki.mankalah.web.mapper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateMapper {
    public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        else {
            OffsetDateTime offsetDateTime = OffsetDateTime.of(
                    timestamp.toLocalDateTime().getYear()
                    , timestamp.toLocalDateTime().getMonthValue()
                    , timestamp.toLocalDateTime().getDayOfMonth()
                    , timestamp.toLocalDateTime().getHour()
                    , timestamp.toLocalDateTime().getMinute()
                    , timestamp.toLocalDateTime().getSecond()
                    , timestamp.toLocalDateTime().getNano()
                    , ZoneOffset.UTC);

            return offsetDateTime;
        }
    }

    public Timestamp asTimeStamp(OffsetDateTime offsetDateTime) {

        if (offsetDateTime == null) {
            return null;
        } else {
            Timestamp timestamp = Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
            return timestamp;
        }
    }

}
