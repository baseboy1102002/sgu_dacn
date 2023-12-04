package com.sgu.schedulerApp.entity;

import com.sgu.schedulerApp.dto.StatisticDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sv_sk")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedNativeQuery(
    name = "statistic_query",
    query =
        "select m.mon as thang, count(distinct sk.id) as tong_sk, count(svsk.id_sv) as tong_dangky" +
            " from (select 1 as mon union all select 2 union all select 3 union all select 4 union all" +
            " select 5 union all select 6 union all select 7 union all select 8 union all" +
            " select 9 union all select 10 union all select 11 union all select 12)" +
            " m left outer join su_kien sk on m.mon = month(sk.ngay) and year(sk.ngay) =:yearNo" +
            " and (sk.ngay<:currentDate or (sk.ngay=:currentDate and sk.ket_thuc<:currentTime))" +
            " and (:facultyCode is null or sk.id_khoa=:facultyCode)" +
            " left outer join sv_sk svsk on sk.id=svsk.id_sk group by thang order by thang asc",
    resultSetMapping = "statistic_dto_mapping"
)
@SqlResultSetMapping(
    name = "statistic_dto_mapping",
    classes = @ConstructorResult(
        targetClass = StatisticDto.class,
        columns = {
            @ColumnResult(name = "thang", type = Integer.class),
            @ColumnResult(name = "tong_sk", type = Integer.class),
            @ColumnResult(name = "tong_dangky", type = Integer.class)
        }
    )
)
public class EventStudent {

    @EmbeddedId
    private EventStudentId eventStudentId;

    @ManyToOne
    @JoinColumn(name = "id_sk")
    @MapsId("eventId")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "id_sv")
    @MapsId("studentId")
    private StudentInfo studentInfo;

    @Column(name = "diem_danh")
    private Boolean checkAttended;

}
