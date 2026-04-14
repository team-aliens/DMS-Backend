package team.aliens.dms.persistence.teacher.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Id
import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.util.UUID

@Table(name = "tbl_teacher")
@Entity
class TeacherJpaEntity(

    @Id
    @Column(name = "user_id")
    val id: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: UserJpaEntity?,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

)