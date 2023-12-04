package com.sgu.schedulerApp.entity;

import com.sgu.schedulerApp.dto.FacultyDto;
import com.sgu.schedulerApp.dto.StatisticDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "khoa")
@NoArgsConstructor
@Getter
@Setter
@NamedNativeQuery(
    name = "statistic_top7_faculty_query",
    query =
        "select k.ten_khoa, k.ma_khoa, count(sk.id) tong_sk from khoa k left outer join su_kien sk on sk.id_khoa = k.id" +
        " and year(sk.ngay)=:yearNo and (sk.ngay<:currentDate or (sk.ngay=:currentDate and sk.ket_thuc<:currentTime))" +
        " group by k.id order by tong_sk desc limit 7",
    resultSetMapping = "faculty_dto_mapping"
)
@SqlResultSetMapping(
    name = "faculty_dto_mapping",
    classes = @ConstructorResult(
        targetClass = FacultyDto.class,
        columns = {
            @ColumnResult(name = "ten_khoa", type = String.class),
            @ColumnResult(name = "ma_khoa", type = String.class),
            @ColumnResult(name = "tong_sk", type = Integer.class),
        }
    )
)
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ten_khoa")
    private String name;

    @Column(name = "ma_khoa")
    private String code;

    @Column(name = "trang_thai")
    private boolean status;

    @OneToMany(mappedBy = "faculty")
    private List<Event> events;

    @OneToMany(mappedBy = "faculty")
    private List<Classroom> classrooms;
}
