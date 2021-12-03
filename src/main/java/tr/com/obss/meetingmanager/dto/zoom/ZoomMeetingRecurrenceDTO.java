package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ZoomMeetingRecurrenceDTO implements Serializable {

    private static final long serialVersionUID = 6583876748358302578L;
    private Integer type;

    private Integer repeat_interval;

    private String weekly_days;

    private Integer monthly_day;

    private Integer monthly_week;

    private Integer monthly_week_day;

    private Integer end_times;

    private String end_date_time;
}
