package team.aliens.dms.persistence.school.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_neis_school_info")
class NeisSchoolInfoJpaEntity(

    @Id
    val schoolId: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity,

    @Column(columnDefinition = "CHAR(3)", nullable = false)
    val sdSchoolCode: Char,

    @Column(columnDefinition = "CHAR(7)", nullable = false)
    val schoolCode: Char

)